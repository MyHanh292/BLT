package com.example.chamsocthucung2.ui.screens.doctor

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chamsocthucung2.data.model.doctor.Appointment
import com.example.chamsocthucung2.ui.components.DoctorBottomNavigationBar
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun DoctorScheduleScreen(viewModel: DoctorAppointmentViewModel = viewModel()) {
    Scaffold(
        topBar = {
            androidx.compose.material3.Text("Lịch Hẹn Bác Sĩ")
        },
        bottomBar = { DoctorBottomNavigationBar() }
    ) { paddingValues ->
        DoctorScheduleContent(modifier = Modifier.padding(paddingValues), viewModel = viewModel)
        DoctorFeedbackDialog(viewModel = viewModel)
        DoctorDetailsDialog(viewModel = viewModel)
    }
}

@Composable
fun DoctorScheduleContent(modifier: Modifier = Modifier, viewModel: DoctorAppointmentViewModel) {
    val appointments by viewModel.appointments.collectAsState()

    val groupedAppointments = appointments.groupBy { it.time.substringBefore(",").trim() } // Ví dụ nhóm theo phần đầu của chuỗi thời gian

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Lịch hẹn",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            OutlinedTextField(
                value = "",
                onValueChange = {},
                leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = "Tìm kiếm") },
                placeholder = { Text("Tra cứu lịch hẹn") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        groupedAppointments.forEach { (day, appointmentsForDay) ->
            item {
                Text(
                    text = day,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            items(appointmentsForDay) { appointment ->
                AppointmentItem(
                    appointment = appointment,
                    onFeedbackClick = viewModel::onFeedbackIconClick,
                    onAppointmentClick = viewModel::onAppointmentClick
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentItem(
    appointment: Appointment,
    onFeedbackClick: (Appointment) -> Unit,
    onAppointmentClick: (Appointment) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onAppointmentClick(appointment) },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.Event, contentDescription = "Lịch hẹn", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Bác sĩ ${appointment.ownerName}", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
                Text(text = appointment.details, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(horizontalAlignment = Alignment.End) {
                Text(text = appointment.time, style = MaterialTheme.typography.bodySmall)
                IconButton(onClick = { onFeedbackClick(appointment) }, modifier = Modifier.size(24.dp)) {
                    Icon(Icons.Filled.Comment, contentDescription = "Phản hồi", tint = Color.Green, modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}

@Composable
fun DoctorFeedbackDialog(viewModel: DoctorAppointmentViewModel) {
    val showDialog by viewModel.showFeedbackDialog.collectAsState()
    val currentAppointment by viewModel.currentAppointmentForFeedback.collectAsState()
    val feedbackInput by viewModel.feedbackInput.collectAsState()

    if (showDialog && currentAppointment != null) {
        AlertDialog(
            onDismissRequest = viewModel::onDismissFeedbackDialog,
            title = { Text("Phản hồi lịch hẹn cho ${currentAppointment?.petName}") },
            text = {
                TextField(
                    value = feedbackInput,
                    onValueChange = viewModel::onFeedbackInputChange,
                    label = { Text("Nhập phản hồi") }
                )
            },
            confirmButton = {
                Button(onClick = viewModel::onSaveFeedback) {
                    Text("Gửi phản hồi")
                }
            },
            dismissButton = {
                Button(onClick = viewModel::onDismissFeedbackDialog) {
                    Text("Hủy")
                }
            }
        )
    }
}

@Composable
fun DoctorDetailsDialog(viewModel: DoctorAppointmentViewModel) {
    val showDialog by viewModel.showDetailsDialog.collectAsState()
    val appointmentDetails by viewModel.currentAppointmentDetails.collectAsState()

    if (showDialog && appointmentDetails != null) {
        AlertDialog(
            onDismissRequest = viewModel::onDismissDetailsDialog,
            title = { Text("Chi tiết lịch hẹn") },
            text = {
                appointmentDetails?.let { details ->
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Mascot: ${details.petName}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        Text("Chủ nuôi: ${details.ownerName}", style = MaterialTheme.typography.bodyMedium)
                        Text("Thời gian: ${details.time}", style = MaterialTheme.typography.bodyMedium)
                        Text("Chi tiết: ${details.details}", style = MaterialTheme.typography.bodyMedium)
                        if (!details.feedback.isNullOrEmpty()) {
                            Text("Phản hồi: ${details.feedback}", style = MaterialTheme.typography.bodyMedium, color = Color.Green)
                        }
                    }
                } ?: Text("Không có thông tin chi tiết.", style = MaterialTheme.typography.bodyMedium)
            },
            confirmButton = {
                Button(onClick = viewModel::onDismissDetailsDialog) {
                    Text("Đóng")
                }
            }
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewDoctorScheduleScreen() {
    DoctorScheduleScreen()
}