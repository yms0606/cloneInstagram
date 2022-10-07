package com.example.cloneinstagram

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.cloneinstagram.databinding.ActivityMainBinding
import com.example.cloneinstagram.fragment.AlarmFragment
import com.example.cloneinstagram.fragment.DetailViewFragment
import com.example.cloneinstagram.fragment.GridFragment
import com.example.cloneinstagram.fragment.UserFragment
import com.google.android.gms.dynamic.SupportFragmentWrapper
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import java.util.jar.Manifest

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        binding.bottomNavigation.setOnNavigationItemSelectedListener(this)
        binding.bottomNavigation.selectedItemId = R.id.action_home
        ActivityCompat.requestPermissions(this,
                            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),0)


    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_home->{
                var f = DetailViewFragment()
                supportFragmentManager.beginTransaction().replace(R.id.main_content,f).commit()
                return true
            }
            R.id.action_favorite_alram->{
                var f = AlarmFragment()
                supportFragmentManager.beginTransaction().replace(R.id.main_content,f).commit()
                return true
            }
            R.id.action_account->{
                var f = UserFragment()
                supportFragmentManager.beginTransaction().replace(R.id.main_content,f).commit()
                return true
            }
            R.id.action_search ->{
                var f = GridFragment()
                supportFragmentManager.beginTransaction().replace(R.id.main_content,f).commit()
                return true
            }
            R.id.action_add_photo ->{

                if(ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
                {
                    //READ_EXTERNAL_STORAGE 있을 때
                    startActivity(Intent(this,AddPhotoActivity::class.java))
                }
                else{
                    Toast.makeText(this,"You don't have read permission",Toast.LENGTH_SHORT).show()

                }

                return true
            }
        }
        return false
    }

}