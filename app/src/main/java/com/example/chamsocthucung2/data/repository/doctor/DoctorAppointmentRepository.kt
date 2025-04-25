package com.example.chamsocthucung2.data.repository

import com.example.chamsocthucung2.data.model.doctor.Appointment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class DoctorAppointmentRepository {

    fun getAllAppointments(): Flow<List<Appointment>> {
        return flowOf(emptyList())
    }

    suspend fun updateAppointmentFeedback(appointmentId: String, feedback: String) {
        println("Updating feedback for appointment ID: $appointmentId with feedback: $feedback")
    }
}