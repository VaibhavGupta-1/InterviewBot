package com.example.interviewbot

import com.google.gson.annotations.SerializedName // Make sure this import is present
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

// --- Groq API Interface ---
interface GroqApi {
    @POST("openai/v1/chat/completions")
    suspend fun createChatCompletion(
        @Body request: GroqRequest,
        @Header("Authorization") apiKey: String = "Bearer ${BuildConfig.GROQ_API_KEY}"
    ): GroqResponse
}

// --- Data Classes for the Groq Request ---
data class GroqRequest(
    val messages: List<GroqMessage>,
    val model: String,

    // --- THIS IS THE FIX ---
    @SerializedName("response_format") // Add this annotation
    val responseFormat: ResponseFormat? = null // Change to camelCase
)

data class ResponseFormat(
    val type: String // This will be "json_object"
)

data class GroqMessage(
    val role: String,
    val content: String
)

// --- Data Classes for the Groq Response ---
data class GroqResponse(
    val choices: List<GroqChoice>
)

data class GroqChoice(
    val message: GroqMessage
)