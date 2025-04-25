package com.example.chamsocthucung2.ui.screens.doctor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chamsocthucung2.data.model.doctor.Appointment
import com.example.chamsocthucung2.data.repository.DoctorAppointmentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DoctorAppointmentViewModel(private val repository: DoctorAppointmentRepository = DoctorAppointmentRepository()) : ViewModel() {

    private val _appointments = MutableStateFlow<List<Appointment>>(emptyList())
    val appointments: StateFlow<List<Appointment>> = _appointments

    private val _showFeedbackDialog = MutableStateFlow(false)
    val showFeedbackDialog: StateFlow<Boolean> = _showFeedbackDialog

    private val _currentAppointmentForFeedback = MutableStateFlow<Appointment?>(null)
    val currentAppointmentForFeedback: StateFlow<Appointment?> = _currentAppointmentForFeedback

    private val _feedbackInput = MutableStateFlow("")
    val feedbackInput: StateFlow<String> = _feedbackInput

    private val _showDetailsDialog = MutableStateFlow(false)
    val showDetailsDialog: StateFlow<Boolean> = _showDetailsDialog

    private val _currentAppointmentDetails = MutableStateFlow<Appointment?>(null)
    val currentAppointmentDetails: StateFlow<Appointment?> = _currentAppointmentDetails

    init {
        loadAppointments()
    }

    private fun loadAppointments() {
        viewModelScope.launch {
            repository.getAllAppointments().collectLatest {
                _appointments.value = it
            }
        }
    }

    fun onFeedbackIconClick(appointment: Appointment) {
        _currentAppointmentForFeedback.value = appointment
        _feedbackInput.value = appointment.feedback ?: ""
        _showFeedbackDialog.value = true
    }

    fun onFeedbackInputChange(newFeedback: String) {
        _feedbackInput.value = newFeedback
    }

    fun onSaveFeedback() {
        _currentAppointmentForFeedback.value?.let { appointment ->
            viewModelScope.launch {
                repository.updateAppointmentFeedback(appointment.id, _feedbackInput.value)
                val updatedList = _appointments.value.map {
                    if (it.id == appointment.id) {
                        it.copy(feedback = _feedbackInput.value)
                    } else {
                        it
                    }
                }
                _appointments.value = updatedList
                _showFeedbackDialog.value = false
                _currentAppointmentForFeedback.value = null
                _feedbackInput.value = ""
            }
        }
    }

    fun onDismissFeedbackDialog() {
        _showFeedbackDialog.value = false
        _currentAppointmentForFeedback.value = null
        _feedbackInput.value = ""
    }

    fun onAppointmentClick(appointment: Appointment) {
        _currentAppointmentDetails.value = appointment
        _showDetailsDialog.value = true
    }

    fun onDismissDetailsDialog() {
        _showDetailsDialog.value = false
        _currentAppointmentDetails.value = null
    }
}