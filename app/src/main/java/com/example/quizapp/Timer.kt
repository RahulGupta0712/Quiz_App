package com.example.quizapp

import android.os.CountDownTimer
import android.util.Log
import com.example.quizapp.databinding.ActivityQuestionBinding

class Timer(totalTime: Long, interval: Long, private val binding: ActivityQuestionBinding) :
    CountDownTimer(totalTime, interval) {
    override fun onTick(millisUntilFinished: Long) {
        // show remaining time in seconds
        val remainingTime = millisUntilFinished / 1000
        binding.timer.text = remainingTime.toString()
    }

    override fun onFinish() {
        binding.timer.setText(R.string.time_up)
        QuestionActivity.isTimerFinished = true
        binding.nextQuestionButton.performClick()   // proceed ahead to next question
    }
}