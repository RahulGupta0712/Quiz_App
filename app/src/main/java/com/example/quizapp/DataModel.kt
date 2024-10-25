package com.example.quizapp

data class DataModel(var name: String, var score: Int) {
    constructor() : this("", 0)
}
