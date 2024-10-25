package com.example.quizapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.quizapp.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.shashank.sony.fancytoastlib.FancyToast
import render.animations.Render
import render.animations.Zoom

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.mainFrame, FragmentHome())
        transaction.commit()

        binding.bottomBar.onItemSelected = {
            when (it) {
                0 -> {
                    val fragment = FragmentHome()
                    val trans = supportFragmentManager.beginTransaction()
                    trans.replace(R.id.mainFrame, fragment)
                    trans.commit()
                }

                1 -> {
                    val fragment = FragmentLeaderboard()
                    val trans = supportFragmentManager.beginTransaction()
                    trans.replace(R.id.mainFrame, fragment)
                    trans.commit()
                }
            }
        }


    }
}