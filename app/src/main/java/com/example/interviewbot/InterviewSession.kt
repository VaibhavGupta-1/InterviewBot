package com.example.interviewbot

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "interview_sessions")
data class InterviewSession(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val role: String,
    val mode: String,
    val date: Long = System.currentTimeMillis(),
    val chatHistory: List<ChatMessage> // Room will convert this complex type
)