package com.example.cloneinstagram

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.cloneinstagram.databinding.ActivityFindIdBinding
import com.example.cloneinstagram.model.FindIdModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FindIdActivity : AppCompatActivity() {
    lateinit var binding : ActivityFindIdBinding
    lateinit var firestore : FirebaseFirestore
    lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_find_id)
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        binding.findIdButton.setOnClickListener {
            readMyId()
        }
        binding.findPasswordButton.setOnClickListener {
            var number = binding.edittextEmail.text.toString()
            auth.sendPasswordResetEmail(number)
        }
        binding.closeButton.setOnClickListener {
            finish()
        }
    }

    fun readMyId(){
        var number = binding.edittextPhonenumber.text.toString()
        firestore.collection("findids").whereEqualTo("phoneNumber",number).get().addOnCompleteListener {
            task->
            if(task.isSuccessful){
                var findIdModel = task.result?.documents?.first()!!.toObject(FindIdModel::class.java)
                Toast.makeText(this,findIdModel!!.id,Toast.LENGTH_LONG).show()
            }
        }
    }
}