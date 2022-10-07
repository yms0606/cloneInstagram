package com.example.cloneinstagram

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.cloneinstagram.databinding.ActivityLoginBinding
import com.facebook.CallbackManager
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.common.collect.Lists.asList
import com.google.common.primitives.Booleans.asList
import com.google.common.primitives.Bytes.asList
import com.google.common.primitives.Chars.asList
import com.google.common.primitives.Ints.asList
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import java.lang.Exception
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import java.util.Arrays.asList
import kotlin.math.log

class LoginActivity : AppCompatActivity() {

    lateinit var auth : FirebaseAuth
    lateinit var binding : ActivityLoginBinding
    lateinit var googlesigninclinet : GoogleSignInClient
    lateinit var callBaockManager : CallbackManager
    var TAG = "LoginActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding  = DataBindingUtil.setContentView(this,R.layout.activity_login)
        auth = FirebaseAuth.getInstance()
        binding.emailLoginButton.setOnClickListener {
            signinAndSignUp()
        }
        binding.findButton.setOnClickListener {
            startActivity(Intent(this,FindIdActivity::class.java))
        }

        binding.googleLoginButton.setOnClickListener {
            googleLogin()
        }

        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .requestProfile()
            .build()
        googlesigninclinet = GoogleSignIn.getClient(this,gso)
        callBaockManager = CallbackManager.Factory.create()
        binding.fackbookLoginButton.setOnClickListener {
            fackbookLogin()
        }
        moveMain(auth.currentUser)
        //printHashKey(this)
    }
    var googleLoginResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result->
        // 구글 로그인 성공했을 때 token 으로 email 값이 넘어옴
        var data = result.data
        var task = GoogleSignIn.getSignedInAccountFromIntent(data)
        val account = task.getResult(ApiException::class.java)
        firebaseAuthWithGoogle(account.idToken)
    }
    fun fackbookLogin(){
        var loginManager = LoginManager.getInstance()
        loginManager.logInWithReadPermissions(this, Arrays.asList("public_profile","email"))
    }


    fun firebaseAuthWithGoogle(idToken : String?) {
        var credential = GoogleAuthProvider.getCredential(idToken,null)
        auth.signInWithCredential(credential).addOnCompleteListener {
                task->
            if(task.isSuccessful) {
                if(auth.currentUser!!.isEmailVerified){
                    //이메일 인증이 됐을 때
                    moveMain(auth.currentUser)
                }
                else {
                    saveFindIdData()
                }
            }
        }
    }
    fun googleLogin(){
        var i = googlesigninclinet.signInIntent
        googleLoginResult.launch(i)
    }
    fun saveFindIdData(){
        finish()
        startActivity(Intent(this, InputNumberActivity::class.java))
    }
    fun signinAndSignUp(){
        var id = binding.edittextId.text.toString()
        var password = binding.edittextPassword.text.toString()
        auth.createUserWithEmailAndPassword(id,password).addOnCompleteListener {
            task ->
            if(task.isSuccessful){
                // id 생성 -> 메인화면 이동
                //moveMain(task.result?.user)
                saveFindIdData()

            }else{
                // if 아이디가 있을 경우 등등
                signinEmail()
            }
        }

    }
    fun moveMain(user : FirebaseUser?) {
        if (user != null) {
            if(user.isEmailVerified) {
                finish()
                startActivity(Intent(this, MainActivity::class.java))
            }
            else{
                user.sendEmailVerification()
            }
        }
    }
    fun signinEmail(){
        var id = binding.edittextId.text.toString()
        var password = binding.edittextPassword.text.toString()
        auth.signInWithEmailAndPassword(id,password).addOnCompleteListener {
                task ->
            if(task.isSuccessful){
                // id 생성 -> 메인화면 이동
                moveMain(task.result?.user)

            }
        }
    }

    fun printHashKey(pContext: Context) { // fackbook login hash key
        // 1IuARf4V4ABLgINRar/CtzLDQuI=
        try {
            val info: PackageInfo = pContext.getPackageManager()
                .getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val hashKey: String = String(Base64.encode(md.digest(), 0))
                Log.i(TAG, "printHashKey() Hash Key: $hashKey")
            }
        } catch (e: NoSuchAlgorithmException) {
            Log.e(TAG, "printHashKey()", e)
        } catch (e: Exception) {
            Log.e(TAG, "printHashKey()", e)
        }
    }

}