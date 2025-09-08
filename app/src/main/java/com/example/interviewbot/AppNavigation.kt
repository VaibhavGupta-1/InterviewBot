package com.example.interviewbot

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.example.interviewbot.ui.screens.ChatScreen
import com.example.interviewbot.ui.screens.HomeScreen
import com.example.interviewbot.ui.screens.SetupScreen
import com.example.interviewbot.ui.screens.SummaryScreen

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberAnimatedNavController()
    val context = LocalContext.current.applicationContext
    val dao = AppDatabase.getDatabase(context).sessionDao()
    val viewModelFactory = InterviewViewModelFactory(dao)
    val viewModel: InterviewViewModel = viewModel(factory = viewModelFactory)

    AnimatedNavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(navController = navController, viewModel = viewModel)
        }
        composable(Screen.Setup.route) {
            SetupScreen(navController = navController, viewModel = viewModel)
        }
        composable(Screen.Chat.route) {
            val messages by viewModel.chatMessages.collectAsState()
            val isBotTyping by viewModel.isBotTyping.collectAsState()
            ChatScreen(
                navController = navController,
                messages = messages,
                isBotTyping = isBotTyping,
                onSendMessage = { message -> viewModel.sendMessage(message) },
                onEndInterview = { viewModel.saveInterviewSession() },
                onSkipQuestion = { viewModel.getNewQuestion() } // Connect the new function
            )
        }
        composable(Screen.Summary.route) { backStackEntry ->
            val sessionId = backStackEntry.arguments?.getString("sessionId")?.toIntOrNull()
            val sessions by viewModel.allSessions.collectAsState()
            val session = sessions.find { it.id == sessionId }
            val summary by viewModel.summaryState.collectAsState()
            if (session != null) {
                SummaryScreen(
                    session = session,
                    summary = summary,
                    onGenerateSummary = { viewModel.generateSummary(session) }
                )
            }
        }
    }
}