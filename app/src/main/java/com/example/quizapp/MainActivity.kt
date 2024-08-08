package com.example.quizapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.quizapp.databinding.ActivityMainBinding
import render.animations.Render
import render.animations.Zoom

class MainActivity : AppCompatActivity() {
    private val binding by lazy{
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


        val list = ArrayList((0..547).shuffled().take(10)) // take 10 random indexes for showing the questions from database (our database contains 548 questions)

        binding.startButton.setOnClickListener{
            val intent = Intent(this@MainActivity, QuestionActivity::class.java)
            intent.putIntegerArrayListExtra("indexes", list)
            intent.putExtra("pos", 0)
            intent.putExtra("score", 0)
            startActivity(intent)
            finish()
        }

        binding.rulesButton.setOnClickListener{
            val currentFragmentCount = supportFragmentManager.backStackEntryCount
            if(currentFragmentCount == 0) {
                // means Rules is not opened currently
                // so start RulesFragment
                val trans = supportFragmentManager.beginTransaction()
                trans.replace(R.id.frame, RulesFragment())
                trans.addToBackStack(null)
                trans.commit()

                // set the animation
                val render = Render(this)
                render.setAnimation(Zoom().In(binding.frame))
                render.setDuration(500)
                render.start()
            }
        }
    }
}