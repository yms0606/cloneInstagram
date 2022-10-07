package com.example.cloneinstagram

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.example.cloneinstagram.databinding.ActivityAddPhotoBinding
import com.example.cloneinstagram.model.ContentModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*

class AddPhotoActivity : AppCompatActivity() {
    lateinit var binding : ActivityAddPhotoBinding
    lateinit var storage : FirebaseStorage
    var photoUri : Uri? = null
    lateinit var auth : FirebaseAuth
    lateinit var firestore : FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_photo)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_add_photo)
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        var i = Intent(Intent.ACTION_PICK)
        i.type = "image/*"
        photoResult.launch(i)
        binding.addphotoUploadBtn.setOnClickListener(){
            contentUpload()
        }

    }
    var photoResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        // 사진 받는 부분
            result->
        photoUri = result.data?.data
        binding.uploadImageview.setImageURI(result.data?.data)
    }
    fun contentUpload(){
        var timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        var imageFilename = "IMAGE"+timestamp+".png"
        var storagePath = storage.reference?.child("images")?.child(imageFilename)

        storagePath?.putFile(photoUri!!)?.continueWithTask{
            return@continueWithTask storagePath?.downloadUrl

        }?.addOnCompleteListener {
            downloadUrl->

            var contentModel = ContentModel()
            contentModel.imageurl = downloadUrl.result.toString()
            contentModel.explain = binding.addphotoEditEdittext.text.toString()
            contentModel.uid = auth?.uid
            contentModel.userId = auth?.currentUser?.email
            contentModel.timestamp = System.currentTimeMillis()

            firestore.collection("images").document().set(contentModel)
            Toast.makeText(this,"complete Upload",Toast.LENGTH_SHORT).show()
            finish()
        }
    }

}