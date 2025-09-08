package com.example.interviewbot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import retrofit2.HttpException

class InterviewViewModel(private val dao: SessionDao) : ViewModel() {

    companion object {
        private const val GROQ_MODEL = "gemma2-9b-it"
    }

    private val gson = GsonBuilder()
        .registerTypeAdapter(InterviewSummary::class.java, InterviewSummaryAdapter())
        .create()

    private val api = RetrofitClient.instance
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatMessages = _chatMessages.asStateFlow()
    private var lastQuestion = ""
    var selectedRole = "Software Engineer"
    var interviewMode = "Behavioral"
    var selectedDomain: String? = null
    var selectedStyle: String? = null
    val allSessions = dao.getAllSessions().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    private val _summaryState = MutableStateFlow<InterviewSummary?>(null)
    val summaryState = _summaryState.asStateFlow()
    private val _isBotTyping = MutableStateFlow(false)
    val isBotTyping = _isBotTyping.asStateFlow()

    fun startInterview() {
        _chatMessages.value = emptyList()
        _isBotTyping.value = true
        viewModelScope.launch {
            try {
                val systemPrompt = buildString {
                    append("You are an expert interviewer for a '$selectedRole' position. ")
                    append("Your task is to generate exactly one opening question for a '$interviewMode' style interview. ")
                    selectedDomain?.let {
                        append("The interview is focused on the '$it' domain. ")
                    }
                    selectedStyle?.let {
                        append("The question should be in the style of a '$it' interview. ")
                    }
                    append("Your entire response must contain ONLY the question text. Do NOT add any greetings, context, or explanations.")
                }

                val request = GroqRequest(
                    model = GROQ_MODEL,
                    messages = listOf(GroqMessage(role = "system", content = systemPrompt))
                )
                val response = api.createChatCompletion(request)
                val botResponseText = response.choices.firstOrNull()?.message?.content ?: "Sorry, I couldn't think of a question."
                lastQuestion = botResponseText
                _chatMessages.value = listOf(ChatMessage(botResponseText, Sender.BOT))
            } catch (e: Exception) {
                _chatMessages.value = listOf(ChatMessage("Error: ${e.message}", Sender.BOT))
            } finally {
                _isBotTyping.value = false
            }
        }
    }

    fun getNewQuestion() {
        _chatMessages.value = _chatMessages.value + ChatMessage("...", Sender.BOT)
        _isBotTyping.value = true
        viewModelScope.launch {
            _chatMessages.value = _chatMessages.value.dropLast(1)
            try {
                val systemPrompt = buildString {
                    append("You are an expert interviewer for a '$selectedRole' position. ")
                    append("Your task is to generate a new and different question for a '$interviewMode' style interview. ")
                    selectedDomain?.let { append("The focus is '$it'. ") }
                    selectedStyle?.let { append("The style is '$it'. ") }
                    append("Your entire response must contain ONLY the question text.")
                }
                val request = GroqRequest(
                    model = GROQ_MODEL,
                    messages = listOf(GroqMessage(role = "system", content = systemPrompt))
                )
                val response = api.createChatCompletion(request)
                val botResponseText = response.choices.firstOrNull()?.message?.content ?: "Sorry, I couldn't think of another question."
                lastQuestion = botResponseText
                _chatMessages.value = _chatMessages.value + ChatMessage(botResponseText, Sender.BOT)
            } catch (e: Exception) {
                _chatMessages.value = _chatMessages.value + ChatMessage("Error: ${e.message}", Sender.BOT)
            } finally {
                _isBotTyping.value = false
            }
        }
    }

    fun sendMessage(userMessage: String) {
        _chatMessages.value = _chatMessages.value + ChatMessage(userMessage, Sender.USER)
        _isBotTyping.value = true
        viewModelScope.launch {
            try {
                val systemPrompt = "You are an interview evaluator. The candidate was asked: '$lastQuestion'. Your task is to provide a short, constructive paragraph of feedback on their answer."
                val request = GroqRequest(
                    model = GROQ_MODEL,
                    messages = listOf(
                        GroqMessage(role = "system", content = systemPrompt),
                        GroqMessage(role = "user", content = userMessage)
                    )
                )
                val response = api.createChatCompletion(request)
                val botFeedback = response.choices.firstOrNull()?.message?.content ?: "I'm not sure how to respond to that."
                _chatMessages.value = _chatMessages.value + ChatMessage(botFeedback, Sender.BOT)
            } catch (e: Exception) {
                _chatMessages.value = _chatMessages.value + ChatMessage("Error getting feedback: ${e.message}", Sender.BOT)
            } finally {
                _isBotTyping.value = false
            }
        }
    }

    fun saveInterviewSession() {
        if (_chatMessages.value.size > 1) {
            val session = InterviewSession(role = selectedRole, mode = interviewMode, chatHistory = _chatMessages.value)
            viewModelScope.launch {
                dao.insertSession(session)
            }
        }
    }

    fun deleteSession(session: InterviewSession) {
        viewModelScope.launch {
            dao.deleteSession(session)
        }
    }

    fun generateSummary(session: InterviewSession) {
        _summaryState.value = null
        viewModelScope.launch {
            try {
                val transcript = session.chatHistory.joinToString("\n") { "${it.sender}: ${it.text}" }
                val systemPrompt = """
                    You are an expert interview coach. Analyze the following interview transcript for a '${session.role}' in '${session.mode}' mode.
                    Based on the transcript, provide a final summary in a JSON format. The JSON object must have these exact keys: "finalScore", "areasOfStrength", "areasToImprove", and "suggestedResources".
                    Your response must be ONLY the raw JSON object, with no other text or markdown.
                """.trimIndent()
                val request = GroqRequest(
                    model = GROQ_MODEL,
                    messages = listOf(
                        GroqMessage(role = "system", content = systemPrompt),
                        GroqMessage(role = "user", content = transcript)
                    ),
                    responseFormat = ResponseFormat(type = "json_object")
                )
                val response = api.createChatCompletion(request)
                val jsonResponse = response.choices.firstOrNull()?.message?.content ?: "{}"
                val summary = gson.fromJson(jsonResponse, InterviewSummary::class.java)
                _summaryState.value = summary
            } catch (e: Exception) {
                _summaryState.value = InterviewSummary(finalScore = 0, areasToImprove = "Error generating summary: ${e.message}")
            }
        }
    }
}