package com.example.chamsocthucung2.data.repository.user

import com.example.chamsocthucung2.data.model.user.Chat.ChatMessage
import com.example.chamsocthucung2.data.model.user.gemini.Content
import com.example.chamsocthucung2.data.model.user.gemini.GeminiRequest
import com.example.chamsocthucung2.data.model.user.gemini.Part
import com.example.chamsocthucung2.data.remote.ApiService
import javax.inject.Inject

class GeminiRepository @Inject constructor(
    private val apiService: ApiService
) {
    // ApiKey free thôi nên demo test thôi :)))))))
    private val apiKey = "AIzaSyAXe2IkIZPvNVF0flpLDIcdgNbZg3EVVFg"

    suspend fun getGeminiResponse(messages: List<ChatMessage>): String {
        val request = buildGeminiRequest(messages)

        val res = apiService.generateContent(apiKey, request)
        if (res.isSuccessful) {
            val content = res.body()
            return content?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text!! // Để ViewModel bắt
        } else {
            throw Exception("Api Response Error: ${res.code()} - ${res.message()}")
        }
    }

    private fun buildGeminiRequest(messages: List<ChatMessage>): GeminiRequest {
        val systemPrompt = Part(
            text = "Bạn là Bác sĩ chó, mục tiêu của bạn là hỗ trợ người dùng trong ứng dụng Pet Care."
        )
        val systemContent = Content(
            parts = listOf(systemPrompt),
            role = "user"
        )
        val contextMessages = messages.takeLast(10).map {
            Content(
                parts = listOf(Part(text = it.text)),
                role = if (it.isFromUser) "user" else "model"
            )
        }
        val contents = listOf(systemContent) + contextMessages
        return GeminiRequest(contents = contents)
    }
}
