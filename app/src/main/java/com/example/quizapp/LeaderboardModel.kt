package com.example.quizapp

data class LeaderboardModel(var name: String, var score: Int, var isMe: Boolean) {
    constructor() : this("", 0, false)
}
