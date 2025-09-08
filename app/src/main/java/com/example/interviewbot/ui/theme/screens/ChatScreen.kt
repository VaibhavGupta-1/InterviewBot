package com.example.interviewbot.ui.screens

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.interviewbot.*
import com.example.interviewbot.ui.BouncingDotsTypingIndicator

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    navController: NavController,
    messages: List<ChatMessage>,
    isBotTyping: Boolean,
    onSendMessage: (String) -> Unit,
    onEndInterview: () -> Unit,
    onSkipQuestion: () -> Unit
) {
    val context = LocalContext.current
    val voiceToTextParser = remember(context) { VoiceToTextParser(context.applicationContext as Application) }
    val voiceState by voiceToTextParser.voiceState.collectAsState()
    var textState by remember { mutableStateOf(TextFieldValue("")) }
    var textBeforeSpeaking by remember { mutableStateOf("") }
    var hasAudioPermission by remember { mutableStateOf(ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted -> hasAudioPermission = isGranted }
    )

    LaunchedEffect(voiceState.isSpeaking, voiceState.spokenText) {
        if (voiceState.isSpeaking) {
            val newText = (textBeforeSpeaking + " " + voiceState.spokenText).trim()
            textState = TextFieldValue(text = newText, selection = TextRange(newText.length))
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mock Interview") },
                actions = {
                    TextButton(onClick = {
                        onEndInterview()
                        navController.popBackStack(Screen.Home.route, inclusive = false)
                    }) {
                        Text("End & Save", color = MaterialTheme.colorScheme.secondary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            LazyColumn(
                modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
                reverseLayout = true
            ) {
                // We use a reversed list and add items from the "bottom" up
                val reversedMessages = messages.reversed()

                // Display the chat messages
                items(reversedMessages) { message ->
                    MessageBubble(message = message)
                }

                // --- THIS IS THE NEW LOGIC FOR THE SKIP BUTTON ---
                // After the messages, check if the last message is from the bot and it's not typing
                if (messages.lastOrNull()?.sender == Sender.BOT && !isBotTyping) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Button(
                                onClick = onSkipQuestion,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            ) {
                                Text("Ask New Question")
                            }
                        }
                    }
                }

                // Show the typing indicator
                if (isBotTyping) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ){
                            BouncingDotsTypingIndicator()
                        }
                    }
                }
            }

            // The input row is unchanged
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = textState,
                    onValueChange = { textState = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Your answer...") },
                    enabled = !isBotTyping
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = {
                        if (textState.text.isNotBlank()) {
                            onSendMessage(textState.text)
                            textState = TextFieldValue("")
                            textBeforeSpeaking = ""
                        }
                    },
                    modifier = Modifier.size(56.dp).background(MaterialTheme.colorScheme.primary, CircleShape),
                    enabled = !isBotTyping
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = MaterialTheme.colorScheme.onPrimary)
                }
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = {
                        if (hasAudioPermission) {
                            if (voiceState.isSpeaking) {
                                voiceToTextParser.stopListening()
                            } else {
                                textBeforeSpeaking = textState.text
                                voiceToTextParser.startListening()
                            }
                        } else {
                            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                        }
                    },
                    modifier = Modifier.size(56.dp).background(
                        if (voiceState.isSpeaking) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                        CircleShape
                    ),
                    enabled = !isBotTyping
                ) {
                    val micIconColor = if (voiceState.isSpeaking) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                    Icon(imageVector = Icons.Default.Mic, contentDescription = "Record audio", tint = micIconColor)
                }
            }
        }
    }
}

@Composable
fun MessageBubble(message: ChatMessage) {
    val isUser = message.sender == Sender.USER
    val alignment = if (isUser) Alignment.CenterEnd else Alignment.CenterStart
    val bubbleColor = if (isUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
    val textColor = if (isUser) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        contentAlignment = alignment
    ) {
        Text(
            text = message.text,
            color = textColor,
            modifier = Modifier
                .background(color = bubbleColor, shape = RoundedCornerShape(12.dp))
                .padding(12.dp)
        )
    }
}