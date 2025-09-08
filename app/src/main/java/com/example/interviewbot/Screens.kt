package com.example.interviewbot

sealed class Screen(val route: String) {
    object Home : Screen("home_screen")
    object Setup : Screen("setup_screen")
    object Chat : Screen("chat_screen")
    object Summary : Screen("summary_screen/{sessionId}")
}