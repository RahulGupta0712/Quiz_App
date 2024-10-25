package com.example.quizapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.quizapp.databinding.ActivityScoreBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ScoreActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityScoreBinding.inflate(layoutInflater)
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

        val score = intent.getIntExtra("score", 0)
        binding.showScore.text = getString(R.string.score_, score.toString())

        val auth = FirebaseAuth.getInstance()
        val databaseRef = FirebaseDatabase.getInstance().reference
        val user = auth.currentUser
        user?.let {
            databaseRef.child("users").child(user.uid).child("score").get().addOnSuccessListener {
                val oldScore = it.getValue(Int::class.java)
                oldScore?.let {
                    if (score > oldScore) {
                        databaseRef.child("users").child(user.uid).child("score").setValue(score)
                    }
                }
            }
        }

        binding.goToHomeButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}