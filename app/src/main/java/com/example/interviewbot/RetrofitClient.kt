package com.example.interviewbot

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "https://api.groq.com/" // Groq's base URL

    // Create a logger to see request and response details in Logcat
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Create an OkHttpClient and add the logger
    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    val instance: GroqApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            // --- THIS IS THE CRUCIAL FIX ---
            // This line tells Retrofit how to convert your Kotlin data classes to JSON
            .addConverterFactory(GsonConverterFactory.create())
            // --- END OF FIX ---
            .client(client) // Set the custom client to enable logging
            .build()

        retrofit.create(GroqApi::class.java)
    }
}