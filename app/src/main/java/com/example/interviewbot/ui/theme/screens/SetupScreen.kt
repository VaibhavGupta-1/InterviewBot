package com.example.interviewbot.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.DesignServices
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.interviewbot.InterviewViewModel
import com.example.interviewbot.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetupScreen(navController: NavController, viewModel: InterviewViewModel) {
    val roles = mapOf(
        "Software Engineer" to Icons.Default.Code,
        "Product Manager" to Icons.Default.Apps,
        "Data Analyst" to Icons.Default.BarChart,
        "UX Designer" to Icons.Default.DesignServices
    )
    var selectedRole by remember { mutableStateOf(roles.keys.first()) }

    val modes = listOf("Technical", "Behavioral")
    var selectedMode by remember { mutableStateOf(modes.last()) }

    val domains = listOf("Frontend", "Backend", "Machine Learning", "System Design")
    var selectedDomain by remember { mutableStateOf<String?>(null) } // Nullable for optional selection

    val styles = listOf("FAANG-style", "STAR-based", "Situational")
    var selectedStyle by remember { mutableStateOf<String?>(null) } // Nullable for optional selection

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text("Interview Setup", style = MaterialTheme.typography.headlineMedium)
        Text("Tailor your practice session", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(24.dp))

        Text("Select Your Role", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        roles.forEach { (role, icon) ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { selectedRole = role },
                colors = CardDefaults.cardColors(
                    containerColor = if (selectedRole == role) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = icon, contentDescription = role)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(role, style = MaterialTheme.typography.titleMedium)
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        Text("Select Interview Mode", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        SegmentedButton(modes, selectedMode) { selectedMode = it }
        Spacer(modifier = Modifier.height(24.dp))

        Text("Optionally, specify a domain", style = MaterialTheme.typography.titleMedium)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(domains) { domain ->
                FilterChip(
                    selected = (selectedDomain == domain),
                    onClick = {
                        selectedDomain = if (selectedDomain == domain) null else domain
                    },
                    label = { Text(domain) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.secondary
                    )
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        Text("Optionally, choose a style", style = MaterialTheme.typography.titleMedium)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(styles) { style ->
                FilterChip(
                    selected = (selectedStyle == style),
                    onClick = {
                        selectedStyle = if (selectedStyle == style) null else style
                    },
                    label = { Text(style) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.secondary
                    )
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = {
                viewModel.selectedRole = selectedRole
                viewModel.interviewMode = selectedMode
                viewModel.selectedDomain = selectedDomain
                viewModel.selectedStyle = selectedStyle
                viewModel.startInterview()
                navController.navigate(Screen.Chat.route)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Begin Interview", fontSize = 16.sp)
        }
    }
}

@Composable
fun SegmentedButton(options: List<String>, selectedOption: String, onOptionSelect: (String) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth()) {
        options.forEach { option ->
            Button(
                onClick = { onOptionSelect(option) },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedOption == option) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.surfaceVariant
                )
            ) { Text(option) }
        }
    }
}