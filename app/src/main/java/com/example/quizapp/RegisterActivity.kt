package com.example.quizapp

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.KeyEvent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.quizapp.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.shashank.sony.fancytoastlib.FancyToast

class RegisterActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference
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
        databaseRef = FirebaseDatabase.getInstance().reference

        // showing/hiding password in editText
        binding.showPasswordButton.setOnClickListener {
            // Toggle the password visibility based on the current state
            if (binding.passwordEditText.transformationMethod is PasswordTransformationMethod) {
                // Password is currently hidden, make it visible
                binding.passwordEditText.transformationMethod = null
                binding.showPasswordButton.setImageResource(R.drawable.hide_password) // Replace with your hide password icon
            } else {
                // Password is currently visible, make it hidden
                binding.passwordEditText.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                binding.showPasswordButton.setImageResource(R.drawable.show_password) // Replace with your show password icon
            }

            // Force the EditText to redraw to reflect the change
            binding.passwordEditText.setSelection(binding.passwordEditText.length())
        }

        binding.signUpButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val name = binding.nameEditText.text.toString()

            if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
                FancyToast.makeText(
                    this,
                    "Incomplete credentials",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.WARNING,
                    false
                ).show()
            } else {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        val user = auth.currentUser
                        // set the name in database
                        databaseRef.child("users").child(user!!.uid).setValue(DataModel(name, 0))
                        FancyToast.makeText(
                            this,
                            "Sign Up successful",
                            FancyToast.LENGTH_SHORT,
                            FancyToast.SUCCESS,
                            false
                        ).show()
                        startActivity(Intent(this, SignupVerifyActivity::class.java))
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
            }
        }

        binding.signInButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(Intent(this, AuthenticationScreen::class.java))
            finishAffinity()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}