package com.example.interviewbot.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.interviewbot.InterviewSession
import com.example.interviewbot.InterviewViewModel
import com.example.interviewbot.Screen
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeScreen(navController: NavController, viewModel: InterviewViewModel) {
    val sessions by viewModel.allSessions.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                imageVector = Icons.Default.Psychology,
                contentDescription = "App Icon",
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Interview Bot", style = MaterialTheme.typography.headlineLarge)
            Text("Ace Your Next FAANG Interview", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = { navController.navigate(Screen.Setup.route) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Start New Mock Interview", style = MaterialTheme.typography.titleMedium)
            }
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Your Progress",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (sessions.isEmpty()) {
                Text("Complete an interview to see your history.", modifier = Modifier.padding(16.dp))
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(sessions) { session ->
                        // Pass the viewModel instance to the card
                        SessionHistoryCard(session = session, navController = navController, viewModel = viewModel)
                    }
                }
            }
        }
        Row(
            modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Made with ", color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f))
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Heart",
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(16.dp)
            )
            Text(" by Vaibhav Gupta", color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f), fontWeight = FontWeight.Bold)
        }
    }
}

// --- THIS IS THE UPDATED CARD ---
@Composable
fun SessionHistoryCard(
    session: InterviewSession,
    navController: NavController,
    viewModel: InterviewViewModel // Add ViewModel parameter
) {
    val formattedDate = remember(session.date) {
        SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(session.date))
    }
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 8.dp), // Adjust padding for buttons
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // This column takes up all available space, pushing the buttons to the right
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(session.role, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "${session.mode} Interview - $formattedDate",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }

            // --- ADD THE DELETE BUTTON ---
            IconButton(onClick = { viewModel.deleteSession(session) }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Session",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f) // Subtle color
                )
            }

            // The navigation arrow is now an IconButton for a consistent click area
            IconButton(onClick = { navController.navigate("summary_screen/${session.id}") }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = "View Details"
                )
            }
        }
    }
}