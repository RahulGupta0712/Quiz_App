package com.example.quizapp

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.ViewTreeObserver
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.quizapp.databinding.ActivityQuestionBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.shashank.sony.fancytoastlib.FancyToast
import kotlin.math.max

class QuestionActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityQuestionBinding.inflate(layoutInflater)
    }

    private lateinit var databaseReference: DatabaseReference
    private var selectedAns = "E"
    private var correctAns = "F"
    private lateinit var timer: Timer

    private val defaultBackgroundColor = R.color.dark_blue
    private val defaultTextColor = R.color.yellow

    private val selectedBackgroundColor = R.color.yellow
    private val selectedTextColor = R.color.black

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // retrieving data from previous activity
        val qNo = intent.getIntExtra("pos", 11) // index of question in list (not database)
        val questionIndices = intent.getIntegerArrayListExtra("indexes")
        var score = intent.getIntExtra("score", 0)

        // initialise database reference
        databaseReference = FirebaseDatabase.getInstance().reference

        // initialise timer
        val time = 30L  // in seconds
        timer = Timer(time * 1000, 1000L, binding)

        // display question and options and save correct option and marked option
        if (qNo < 10) {
            val questionNo = questionIndices!![qNo].toString()  // Question number in the realtime database

            // extracting data from database using question No.
            databaseReference.child(questionNo).addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val questionInfo = snapshot.getValue(QuestionsModel::class.java)    // got the data in required format i.e. QuestionsModel
                        questionInfo?.let {
                            // set the data in textViews
                            binding.question.text = getString(R.string.question, (qNo + 1).toString(), questionInfo.question)
                            binding.optionA.text = getString(R.string.options, "A", questionInfo.A)
                            binding.optionB.text = getString(R.string.options, "B", questionInfo.B)
                            binding.optionC.text = getString(R.string.options, "C", questionInfo.C)
                            binding.optionD.text = getString(R.string.options, "D", questionInfo.D)

                            // save correct option
                            correctAns = questionInfo.answer

                            // show the questions and answers when that data is loaded and set
                            binding.group2.isVisible = true

                            // start the timer
                            timer.start()

                            // make height of all options equal
                            var maxHeight = 0
                            val viewTreeObserver = binding.root.viewTreeObserver
                            viewTreeObserver.addOnGlobalLayoutListener(object :
                                ViewTreeObserver.OnGlobalLayoutListener {
                                override fun onGlobalLayout() {
                                    maxHeight = max(maxHeight, binding.optionA.height)
                                    maxHeight = max(maxHeight, binding.optionB.height)
                                    maxHeight = max(maxHeight, binding.optionC.height)
                                    maxHeight = max(maxHeight, binding.optionD.height)

                                    binding.optionA.height = maxHeight
                                    binding.optionB.height = maxHeight
                                    binding.optionC.height = maxHeight
                                    binding.optionD.height = maxHeight

                                    binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)    // free the resource
                                }
                            })
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {}})
        }

        // handle onClick behaviour on options
        binding.optionA.setOnClickListener {
            selectedAns = "A"

            // selected option
            changeColor(binding.optionA, true)

            // unselected options
            changeColor(binding.optionB)
            changeColor(binding.optionC)
            changeColor(binding.optionD)
        }
        binding.optionB.setOnClickListener {
            selectedAns = "B"

            // selected option
            changeColor(binding.optionB, true)

            // unselected options
            changeColor(binding.optionA)
            changeColor(binding.optionC)
            changeColor(binding.optionD)
        }
        binding.optionC.setOnClickListener {
            selectedAns = "C"

            // selected option
            changeColor(binding.optionC, true)

            // unselected options
            changeColor(binding.optionA)
            changeColor(binding.optionB)
            changeColor(binding.optionD)
        }
        binding.optionD.setOnClickListener {
            selectedAns = "D"

            // selected option
            changeColor(binding.optionD, true)

            // unselected options
            changeColor(binding.optionA)
            changeColor(binding.optionB)
            changeColor(binding.optionC)
        }

        // move to next question, if an option is selected
        binding.nextQuestionButton.setOnClickListener {
            if (selectedAns == "E") {
                FancyToast.makeText(this,getString(R.string.no_options_selected),FancyToast.LENGTH_LONG,FancyToast.INFO,false).show()
            }
            else {
                // show correct answer with green background
                showOptionStatus(correctAns, R.color.green)

                if (selectedAns == correctAns) {
                    // correct answer, so update score
                    score++
                }
                else{
                    // incorrect answer, so show red color
                    showOptionStatus(selectedAns, R.color.red)
                }

                // since we are moving to next question, so end the current timer
                timer.cancel()

                if (qNo < 9) {
                    // go to next question [re-using the same activity]
                    val intent = Intent(this, QuestionActivity::class.java)
                    intent.putIntegerArrayListExtra("indexes", questionIndices)
                    intent.putExtra("pos", qNo + 1)
                    intent.putExtra("score", score)
                    startActivity(intent)
                    finish()
                } else {
                    // this is the last question,so now show the score in another activity
                    val intent = Intent(this, ScoreActivity::class.java)
                    intent.putExtra("score", score)
                    startActivity(intent)
                    finish()
                }
            }
        }

        binding.exitQuizButton.setOnClickListener {
            showExitDialog()
        }
    }

    private fun showExitDialog() {
        SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
            .setTitleText(getString(R.string.leave_quiz))
            .setCustomImage(R.drawable.app_logo)
            .setCancelButton(getString(R.string.yes)) {
                FancyToast.makeText(this,getString(R.string.exited_the_quiz),FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show()
                startActivity(Intent(this, MainActivity::class.java))
                it.dismiss()
                timer.cancel()  // end the current timer
                finish()
            }
            .setConfirmButton(getString(R.string.no)) {
                it.dismiss()
            }
            .show()
    }

    private fun showOptionStatus(answer: String, colorToShow: Int) {
        when (answer){
            "A" -> binding.optionA.setBackgroundResource(colorToShow)
            "B" -> binding.optionB.setBackgroundResource(colorToShow)
            "C" -> binding.optionC.setBackgroundResource(colorToShow)
            "D" -> binding.optionD.setBackgroundResource(colorToShow)
        }
    }

    private fun changeColor(view: TextView, isSelected:Boolean = false) {
        view.setBackgroundResource(if(isSelected) selectedBackgroundColor else defaultBackgroundColor)
        view.setTextColor(ContextCompat.getColor(this, if(isSelected) selectedTextColor else defaultTextColor))
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            showExitDialog()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}