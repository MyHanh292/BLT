package com.example.chamsocthucung2.data.remote

import com.example.chamsocthucung2.data.model.user.gemini.GeminiRequest
import com.example.chamsocthucung2.data.model.user.gemini.GeminiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query


interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("v1beta/models/gemini-2.0-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): Response<GeminiResponse>
}