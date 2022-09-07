package com.example.cloneinstagram

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.cloneinstagram.databinding.ActivityMainBinding
import com.example.cloneinstagram.fragment.AlarmFragment
import com.example.cloneinstagram.fragment.DetailViewFragment
import com.example.cloneinstagram.fragment.GridFragment
import com.example.cloneinstagram.fragment.UserFragment
import com.google.android.gms.dynamic.SupportFragmentWrapper
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        binding.bottomNavigation.setOnNavigationItemSelectedListener(this)


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
        }
        return false
    }

}