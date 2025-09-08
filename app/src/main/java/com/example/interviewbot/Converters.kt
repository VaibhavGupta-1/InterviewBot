package com.example.interviewbot

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    /**
     * Converts a list of ChatMessage objects into a JSON string for database storage.
     */
    @TypeConverter
    fun fromChatMessageList(value: List<ChatMessage>): String {
        val gson = Gson()
        return gson.toJson(value)
    }

    /**
     * Converts a JSON string from the database back into a list of ChatMessage objects.
     */
    @TypeConverter
    fun toChatMessageList(value: String): List<ChatMessage> {
        val gson = Gson()
        // We need to use TypeToken to tell Gson the specific type of the list (List<ChatMessage>)
        val type = object : TypeToken<List<ChatMessage>>() {}.type
        return gson.fromJson(value, type)
    }
}