package com.example.chamsocthucung2.data.model.user.Chat

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val conversationId: String,
    val text: String,
    val time: Long,
    val isFromUser: Boolean
)