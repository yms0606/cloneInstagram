package com.example.cloneinstagram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.cloneinstagram.databinding.ActivityInputNumberBinding
import com.example.cloneinstagram.model.FindIdModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class InputNumberActivity : AppCompatActivity() {
    lateinit var fireStore : FirebaseFirestore
    lateinit var auth : FirebaseAuth
    lateinit var binding : ActivityInputNumberBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_input_number)
        fireStore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        binding.applyButton.setOnClickListener {
            savePhoneNumber()
        }
    }
    fun savePhoneNumber(){
        var findIdModel = FindIdModel()
        findIdModel.id = auth.currentUser?.email
        findIdModel.phoneNumber = binding.edittextPhonenumber.text.toString()
        fireStore.collection("findids").document().set(findIdModel).addOnCompleteListener {
            task->
            if(task.isSuccessful){
                finish()
                auth.currentUser?.sendEmailVerification()
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }
    }
}