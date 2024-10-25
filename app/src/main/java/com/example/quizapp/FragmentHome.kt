package com.example.quizapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.quizapp.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.shashank.sony.fancytoastlib.FancyToast
import render.animations.Render
import render.animations.Zoom

class FragmentHome : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val auth = FirebaseAuth.getInstance()
        val databaseRef = FirebaseDatabase.getInstance().reference

        val user = auth.currentUser

        user?.let {
            // show user's name
            databaseRef.child("users").child(user.uid).child("name").get().addOnSuccessListener {
                binding.name.text = it.getValue(String::class.java)
            }
        }

        binding.logoutButton.setOnClickListener {
            SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Logout")
                .setContentText("Are you sure ?")
                .setConfirmButton("YES") {
                    auth.signOut()
                    FancyToast.makeText(
                        requireActivity(),
                        "Sign out successful",
                        FancyToast.LENGTH_SHORT,
                        FancyToast.SUCCESS,
                        false
                    ).show()
                    startActivity(Intent(context, AuthenticationScreen::class.java))
                    it.dismiss()
                }
                .setCancelButton("NO") {
                    it.dismiss()
                }
                .show()
        }

        binding.startButton.setOnClickListener {
            val intent = Intent(context, QuestionActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        binding.rulesButton.setOnClickListener {
            val manager = requireActivity().supportFragmentManager
            val currentFragmentCount = manager.backStackEntryCount
            if (currentFragmentCount == 0) {
                // means Rules is not opened currently
                // so start RulesFragment
                val trans = manager.beginTransaction()
                trans.replace(R.id.frame, RulesFragment())
                trans.addToBackStack(null)
                trans.commit()

                // set the animation
                val render = Render(requireActivity())
                render.setAnimation(Zoom().In(binding.frame))
                render.setDuration(500)
                render.start()
            }
        }
    }
}