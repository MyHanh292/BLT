package com.example.chamsocthucung2.viewmodel.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chamsocthucung2.data.model.user.Chat.ChatMessage
import com.example.chamsocthucung2.data.repository.user.GeminiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val geminiRepo: GeminiRepository
) : ViewModel() {
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    fun sendMessage(text: String) {
        val newMessage = ChatMessage(
            text = text,
            time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()),
            isFromUser = true
        )
        _messages.update { it + newMessage }
        simulateReply()
    }

    fun addMessage(messageText: String, isUser: Boolean, imageUri: String? = null) {
        val newMessage = ChatMessage(
            text = messageText,
            time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()),
            isFromUser = isUser,
            imageUri = imageUri
        )
        _messages.update { it + newMessage }
    }

    private fun addMessageTyping(): String {
        val id = UUID.randomUUID().toString()
        val newMessage = ChatMessage(
            id = id,
            text = "Đang nhập...",
            time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()),
            isFromUser = false
        )

        _messages.update { it + newMessage }
        return id;
    }

    fun sendMessageToGemini(messageText: String) {
        addMessage(messageText, isUser = true)

        val typingId = addMessageTyping()

        viewModelScope.launch {
            try {
                val res = geminiRepo.getGeminiResponse(_messages.value).trim()
                _messages.update { list ->
                    list.map {
                        if (it.id == typingId) it.copy(text = res)
                        else it
                    }
                }
            } catch (e: Exception) {
                _messages.update { list ->
                    list.map {
                        if (it.id == typingId) it.copy(text = "Đã xảy ra lỗi: ${e.message}")
                        else it
                    }
                }
            }
        }
    }

    fun simulateReply() {
        viewModelScope.launch {
            val replyText = listOf(
                "Cảm ơn bạn đã liên hệ!",
                "Tôi đang xem xét yêu cầu của bạn.",
                "Bạn có cần thêm thông tin gì không?"
            ).random()
            val reply = ChatMessage(
                text = replyText,
                time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()),
                isFromUser = false
            )
            _messages.update { it + reply }
        }
    }

    fun clearTempImageUri() {

    }
}