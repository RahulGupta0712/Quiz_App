package com.example.quizapp

data class QuestionsModel(var question : String, var A : String, var B : String, var C:String, var D : String, var answer : String) {
    constructor():this("", "", "", "", "", "")
}