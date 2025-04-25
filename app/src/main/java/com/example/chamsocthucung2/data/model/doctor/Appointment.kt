package com.example.chamsocthucung2.data.model.doctor

data class Appointment(
    val id: String,
    val petName: String,
    val ownerName: String,
    val time: String,
    val feedback: String? = null,
    val details: String
)