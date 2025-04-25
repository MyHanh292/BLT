package com.example.chamsocthucung2.viewmodel.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chamsocthucung2.data.model.user.Chat.ChatMessage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ChatViewModel : ViewModel() {
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
        TODO("Not yet implemented")
    }
}

fun simulateReply() {
    TODO("Not yet implemented")
}
