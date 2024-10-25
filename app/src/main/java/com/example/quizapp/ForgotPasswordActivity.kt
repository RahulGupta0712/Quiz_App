package com.example.quizapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.quizapp.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.shashank.sony.fancytoastlib.FancyToast

class ForgotPasswordActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityForgotPasswordBinding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()

        binding.passwordResetButton.setOnClickListener {
            val email = binding.emailEditText3.text.toString()
            if (email.isEmpty()) {
                FancyToast.makeText(
                    this,
                    "Please provide Email",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.INFO,
                    false
                ).show()
            } else {
                auth.createUserWithEmailAndPassword(email, "dummyPassword")
                    .addOnSuccessListener {
                        // user didn't exist
                        val user = auth.currentUser!!
                        user.delete()
                        FancyToast.makeText(
                            this,
                            "User not registered",
                            FancyToast.LENGTH_SHORT,
                            FancyToast.ERROR,
                            false
                        ).show()
                    }
                    .addOnFailureListener { exception ->
                        if (exception is FirebaseAuthUserCollisionException) {
                            // user already exists, so send password reset link
                            auth.sendPasswordResetEmail(email)
                                .addOnSuccessListener {
                                    FancyToast.makeText(
                                        this,
                                        "Password Reset link sent to $email",
                                        FancyToast.LENGTH_LONG,
                                        FancyToast.SUCCESS,
                                        false
                                    ).show()
                                    startActivity(Intent(this, LoginActivity::class.java))
                                    finish()
                                }
                                .addOnFailureListener { e ->
                                    FancyToast.makeText(
                                        this,
                                        "Error : ${e.message}",
                                        FancyToast.LENGTH_LONG,
                                        FancyToast.ERROR,
                                        false
                                    ).show()
                                }
                        } else {
                            // user does not exists and there was some error, do nothing
                        }
                    }
            }
        }

        binding.registerButton3.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
    }
}