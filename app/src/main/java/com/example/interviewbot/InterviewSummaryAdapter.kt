package com.example.interviewbot

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

class InterviewSummaryAdapter : TypeAdapter<InterviewSummary>() {

    override fun write(out: JsonWriter, value: InterviewSummary?) {
        // We only read from the API, so we don't need to implement writing.
    }

    override fun read(`in`: JsonReader): InterviewSummary {
        var finalScore = 0
        var areasOfStrength = ""
        var areasToImprove = ""
        var suggestedResources = ""

        `in`.beginObject()
        while (`in`.hasNext()) {
            when (`in`.nextName()) {
                "finalScore" -> finalScore = `in`.nextInt()
                "areasOfStrength" -> areasOfStrength = readStringOrArray(`in`)
                "areasToImprove" -> areasToImprove = readStringOrArray(`in`)
                "suggestedResources" -> suggestedResources = readStringOrArray(`in`)
                else -> `in`.skipValue() // Ignore any unexpected fields
            }
        }
        `in`.endObject()

        return InterviewSummary(finalScore, areasOfStrength, areasToImprove, suggestedResources)
    }

    private fun readStringOrArray(reader: JsonReader): String {
        // If the next token is a String, read it directly.
        if (reader.peek() == JsonToken.STRING) {
            return reader.nextString()
        }
        // If it's an Array, read it and join the elements into a single string.
        if (reader.peek() == JsonToken.BEGIN_ARRAY) {
            val list = mutableListOf<String>()
            reader.beginArray()
            while (reader.hasNext()) {
                list.add(reader.nextString())
            }
            reader.endArray()
            return list.joinToString("\n") { "- $it" } // Format as a bulleted list
        }
        // As a fallback, skip the value and return an empty string.
        reader.skipValue()
        return ""
    }
}