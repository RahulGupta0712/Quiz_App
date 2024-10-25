package com.example.quizapp

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.KeyEvent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.lifecycleScope
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.quizapp.databinding.ActivityLoginBinding
import com.example.quizapp.databinding.SetNameDialogBinding
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.shashank.sony.fancytoastlib.FancyToast
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
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
        databaseReference = FirebaseDatabase.getInstance().reference

        binding.loginButton2.setOnClickListener {
            val email = binding.emailEditText2.text.toString()
            val password = binding.passwordEditText2.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                FancyToast.makeText(
                    this,
                    "Incomplete credentials",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.WARNING,
                    false
                ).show()
            } else {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        val user = auth.currentUser!!
                        if (user.isEmailVerified) {
                            FancyToast.makeText(
                                this,
                                "Login Successful",
                                FancyToast.LENGTH_SHORT,
                                FancyToast.SUCCESS,
                                false
                            ).show()
                            startActivity(Intent(this, MainActivity::class.java))
                            finishAffinity()
                        } else {
                            FancyToast.makeText(
                                this,
                                "Email not verified",
                                FancyToast.LENGTH_SHORT,
                                FancyToast.ERROR,
                                false
                            ).show()
                            auth.signOut()
                        }

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

        // showing/hiding password in editText
        binding.viewPasswordButton2.setOnClickListener {
            // Toggle the password visibility based on the current state
            if (binding.passwordEditText2.transformationMethod is PasswordTransformationMethod) {
                // Password is currently hidden, make it visible
                binding.passwordEditText2.transformationMethod = null
                binding.viewPasswordButton2.setImageResource(R.drawable.hide_password) // Replace with your hide password icon
            } else {
                // Password is currently visible, make it hidden
                binding.passwordEditText2.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                binding.viewPasswordButton2.setImageResource(R.drawable.show_password) // Replace with your show password icon
            }

            // Force the EditText to redraw to reflect the change
            binding.passwordEditText2.setSelection(binding.passwordEditText2.length())
        }

        binding.registerButton2.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }

        binding.forgotPasswordButton.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }


        // google sign in
        binding.googleSignInButton.setOnClickListener {
            val WEB_CLIENT_ID = ContextCompat.getString(this, R.string.web_client_id)
            val signInWithGoogleOption: GetSignInWithGoogleOption =
                GetSignInWithGoogleOption.Builder(WEB_CLIENT_ID)
                    .build()

            val request = GetCredentialRequest(listOf(signInWithGoogleOption))

            val credentialManager = CredentialManager.create(this)

            lifecycleScope.launch {
                try {
                    val result = credentialManager.getCredential(
                        context = this@LoginActivity,
                        request = request
                    ) // responsible for fetching all google accounts
                    handleSignIn(result)
                } catch (e: GetCredentialException) {
                    Log.d("error", "Exception occurred")
                }
            }
        }
    }

    fun handleSignIn(result: GetCredentialResponse) {
        // Handle the successfully returned credential.
        val credential = result.credential
        when (credential) {// GoogleIdToken credential
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        // Use googleIdTokenCredential and extract the ID to validate and authenticate on your server.
                        val googleIdTokenCredential = GoogleIdTokenCredential
                            .createFrom(credential.data)

                        val idToken = googleIdTokenCredential.idToken
                        auth.signInWithCredential(GoogleAuthProvider.getCredential(idToken, null))
                            .addOnSuccessListener {
                                val user = auth.currentUser
                                databaseReference.child("users").child(user!!.uid).child("name")
                                    .addListenerForSingleValueEvent(object :
                                        ValueEventListener {
                                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                                            if (!dataSnapshot.exists()) {
                                                // New Entry
                                                // New user, set initial properties
                                                val dialogBinding =
                                                    SetNameDialogBinding.inflate(layoutInflater)
                                                AlertDialog.Builder(this@LoginActivity)
                                                    .setTitle("Set Name")
                                                    .setView(dialogBinding.root)
                                                    .setPositiveButton("Save") { dia, which ->
                                                        val name =
                                                            dialogBinding.nameEditText2.text.toString()
                                                        if (name.isEmpty()) {
                                                            FancyToast.makeText(
                                                                this@LoginActivity,
                                                                "Empty name",
                                                                FancyToast.LENGTH_SHORT,
                                                                FancyToast.ERROR,
                                                                false
                                                            ).show()
                                                            return@setPositiveButton // exit this dialog code below so that dialog is not dismissed with name being empty
                                                        } else {
                                                            databaseReference.child("users")
                                                                .child(user.uid)
                                                                .setValue(DataModel(name, 0))
                                                            dia.dismiss()
                                                            startActivity(
                                                                Intent(
                                                                    this@LoginActivity,
                                                                    MainActivity::class.java
                                                                )
                                                            )
                                                            finish()
                                                        }
                                                    }
                                                    .setCancelable(false)
                                                    .create()
                                                    .show()
                                            } else {
                                                startActivity(
                                                    Intent(
                                                        this@LoginActivity,
                                                        MainActivity::class.java
                                                    )
                                                )
                                                finish()
                                            }

                                        }

                                        override fun onCancelled(error: DatabaseError) {}
                                    })
                            }
                            .addOnFailureListener { e ->
                                Log.d("error", "Account sign in failed", e)
                            }

                    } catch (e: GoogleIdTokenParsingException) {
                        Log.d("error", "Received an invalid google id token response", e)
                    }
                } else {
                    // Catch any unrecognized custom credential type here.
                    Log.d("error", "Unexpected type of credential - 1")
                }
            }

            else -> {
                // Catch any unrecognized credential type here.
                Log.d("error", "Unexpected type of credential - 2")
            }
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