package com.example.interviewbot.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.interviewbot.InterviewSession
import com.example.interviewbot.InterviewSummary
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun SummaryScreen(session: InterviewSession, summary: InterviewSummary?, onGenerateSummary: () -> Unit) {
    LaunchedEffect(key1 = session) { onGenerateSummary() }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Performance Report", style = MaterialTheme.typography.headlineMedium)
        Text("${session.role} (${session.mode})", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
        Spacer(modifier = Modifier.height(24.dp))

        if (summary == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                item {
                    Text("Final Score: ${summary.finalScore}/100", style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.secondary)
                }
                item {
                    SummaryCard(
                        title = "Areas of Strength",
                        content = summary.areasOfStrength,
                        icon = Icons.Default.CheckCircle,
                        iconColor = Color(0xFF4CAF50) // Green
                    )
                }
                item {
                    SummaryCard(
                        title = "Areas to Improve",
                        content = summary.areasToImprove,
                        icon = Icons.Default.Build,
                        iconColor = Color(0xFFFFC107) // Amber
                    )
                }
                item {
                    SummaryCard(
                        title = "Suggested Resources",
                        content = summary.suggestedResources,
                        icon = Icons.Default.Lightbulb,
                        iconColor = Color(0xFF2196F3) // Blue
                    )
                }
            }
        }
    }
}

@Composable
fun SummaryCard(title: String, content: String, icon: ImageVector, iconColor: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = icon, contentDescription = title, tint = iconColor)
                Spacer(modifier = Modifier.width(8.dp))
                Text(title, style = MaterialTheme.typography.titleLarge)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(content, style = MaterialTheme.typography.bodyMedium)
        }
    }
}