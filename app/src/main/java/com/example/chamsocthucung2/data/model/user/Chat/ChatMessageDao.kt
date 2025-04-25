package com.example.chamsocthucung2.data.model.user.Chat

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatMessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessageEntity)

    @Query("SELECT * FROM chat_messages WHERE conversationId = :conversationId ORDER BY time ASC")
    fun getAllMessagesForConversation(conversationId: String): Flow<List<ChatMessageEntity>>

    @Query("DELETE FROM chat_messages WHERE conversationId = :conversationId")
    suspend fun deleteAllMessagesForConversation(conversationId: String)
}