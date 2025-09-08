package com.example.interviewbot.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun BouncingDotsTypingIndicator() {
    val dotCount = 3
    val dotSize = 8.dp
    val dotSpacing = 4.dp
    val bounceHeight = 10.dp

    val animations = List(dotCount) { index ->
        var animationState by remember { mutableStateOf(0f) }
        LaunchedEffect(key1 = Unit) {
            delay(index * 150L) // Stagger the start of each dot's animation
            animate(
                initialValue = 0f,
                targetValue = 0f, // This is a dummy target, the magic is in the animation spec
                animationSpec = infiniteRepeatable(
                    animation = keyframes {
                        durationMillis = 800
                        0f at 0
                        -bounceHeight.value at 200
                        0f at 400
                    },
                    repeatMode = RepeatMode.Restart
                )
            ) { value, _ -> animationState = value }
        }
        animationState
    }

    Row(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dotSpacing)
    ) {
        animations.forEach { animation ->
            Box(
                modifier = Modifier
                    .size(dotSize)
                    .offset(y = animation.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f))
            )
        }
    }
}