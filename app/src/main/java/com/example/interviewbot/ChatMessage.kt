package com.example.interviewbot

data class ChatMessage(
    val text: String,
    val sender: Sender
)

enum class Sender {
    USER,
    BOT
}