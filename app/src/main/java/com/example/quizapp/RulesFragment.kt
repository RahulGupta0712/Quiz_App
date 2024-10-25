package com.example.quizapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.quizapp.databinding.FragmentRulesBinding
import render.animations.Render
import render.animations.Zoom


class RulesFragment : Fragment() {
    private lateinit var binding: FragmentRulesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRulesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.closeButton.setOnClickListener {
            // set animation while closing RulesFragment
            val render = Render(requireContext())
            render.setAnimation(Zoom().Out(binding.root))
            render.setDuration(500)
            render.start()

            // when animation is done, then remove the fragment from backStack
            Handler(Looper.getMainLooper()).postDelayed({
                requireActivity().supportFragmentManager.popBackStack()
            }, 500)
        }
    }

    companion object {

    }
}