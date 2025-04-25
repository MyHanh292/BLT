package com.example.chamsocthucung2.data.model.user.Chat

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val text: String,
    val time: String = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()),
    val isFromUser: Boolean,
    val isRead: Boolean = false,
    val imageUri: String? = null // Thêm thuộc tính để lưu URI của hình ảnh
)