package com.example.chamsocthucung2.view.user

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.chamsocthucung2.R
import com.example.chamsocthucung2.ui.components.BottomNavigationBar
import com.example.chamsocthucung2.viewmodel.user.PetViewModel
import java.util.Calendar
@Composable
fun BookingScreen(navController: NavController, petViewModel: PetViewModel = viewModel()) {
    Log.d("BookingScreen", "Tên thú cưng khi BookingScreen được tạo: ${petViewModel.profileInfo.value.petName}") // Log hiện tại
    val context = LocalContext.current
    var selectedDate by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var enteredPetName by remember { mutableStateOf(petViewModel.profileInfo.value.petName ?: "") } // State cho tên thú cưng nhập
    val calendar = Calendar.getInstance()

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.image1),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.3f
        )

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Đặt lịch khám", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(painterResource(id = R.drawable.ic_back), contentDescription = "Quay lại")
                        }
                    },
                    backgroundColor = Color(0xFFFFC107),
                    elevation = 8.dp
                )
            },

            bottomBar = { BottomNavigationBar(navController) }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = enteredPetName,
                    onValueChange = { enteredPetName = it },
                    label = { Text("Tên thú cưng") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text("Chọn ngày khám", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                        .clickable {
                            DatePickerDialog(
                                context,
                                { _, year, month, dayOfMonth -> selectedDate = "$dayOfMonth/${month + 1}/$year" },
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH)
                            ).show()
                        },
                    shape = RoundedCornerShape(12.dp),
                    elevation = 4.dp,
                    backgroundColor = Color.White
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(painterResource(id = R.drawable.ic_home), contentDescription = null, tint = Color(0xFFFFC107))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = if (selectedDate.isEmpty()) "Chọn ngày" else selectedDate, fontSize = 16.sp)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text("Chọn giờ khám", fontSize = 18.sp, fontWeight = FontWeight.Bold)

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                        .clickable {
                            TimePickerDialog(
                                context,
                                { _, hour, minute -> selectedTime = "%02d:%02d".format(hour, minute) },
                                calendar.get(Calendar.HOUR_OF_DAY),
                                calendar.get(Calendar.MINUTE),
                                true
                            ).show()
                        },
                    shape = RoundedCornerShape(12.dp),
                    elevation = 4.dp,
                    backgroundColor = Color.White
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(painterResource(id = R.drawable.ic_home), contentDescription = null, tint = Color(0xFFFFC107))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = if (selectedTime.isEmpty()) "Chọn giờ" else selectedTime, fontSize = 16.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Ghi chú thêm (tùy chọn)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = {
                        Log.d("BookingScreen", "Giá trị petName trước khi lưu: $enteredPetName")
                        if (selectedDate.isNotEmpty() && selectedTime.isNotEmpty() && enteredPetName.isNotEmpty()) {
                            petViewModel.saveAppointment(selectedDate, selectedTime, note, enteredPetName) // Sử dụng enteredPetName
                            Toast.makeText(context, "Đã đặt lịch hẹn cho $enteredPetName!", Toast.LENGTH_SHORT).show()
                            navController.popBackStack() // Quay lại màn hình trước sau khi đặt lịch
                        } else {
                            Toast.makeText(
                                context,
                                "Vui lòng chọn ngày, giờ và nhập tên thú cưng",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFFA500)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        "Đặt lịch cho $enteredPetName",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}



@Composable
fun DateTimePickerButton(label: String, selectedValue: String, iconRes: Int, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color.Gray),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White.copy(alpha = 0.8f)) // Nền trắng mờ
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = label,
            tint = Color(0xFFFFA500) // Màu cam
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = if (selectedValue.isEmpty()) label else selectedValue, fontSize = 16.sp, fontWeight = FontWeight.Medium)
    }
}
@Composable
fun CustomOutlinedTextField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            backgroundColor = Color.White.copy(alpha = 0.8f),
            focusedBorderColor = Color(0xFFFFA500),
            unfocusedBorderColor = Color.Gray
        )
    )
}
@Composable
fun AppointmentDetailScreen(
    navController: NavController,
    petViewModel: PetViewModel = viewModel()
) {
    val appointments by petViewModel.appointments.collectAsState()

    Log.i("AAA", appointments.toString())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("📅 Lịch hẹn đã đặt", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(painterResource(id = R.drawable.ic_back), contentDescription = "Quay lại")
                    }
                },
                backgroundColor = Color(0xFFFFA500),
                contentColor = Color.White
            )
        },
        bottomBar = { BottomNavigationBar(navController) },
        floatingActionButton = { // Thêm FloatingActionButton
            FloatingActionButton (
                onClick = {

                    navController.navigate("BOOKING")
                },
                backgroundColor = Color(0xFFFFA500),
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, "Thêm lịch hẹn")
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(Color(0xFFFFE4B5), Color.White))) // 🌟 Hình nền gradient
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("🗓 Danh sách lịch hẹn", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))

                if (appointments.isEmpty()) {
                    Text("Không có lịch hẹn nào.", fontSize = 18.sp, color = Color.Gray)
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(appointments) { appointment ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .shadow(6.dp, shape = RoundedCornerShape(16.dp)),
                                backgroundColor = Color.White,
                                elevation = 4.dp
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    val petImage = appointment.petImage ?: R.drawable.cat_dog
                                    Image(
                                        painter = painterResource(id = R.drawable.cat_dog),
                                        contentDescription = "Hình thú cưng",
                                        modifier = Modifier
                                            .size(80.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFFFA500))
                                    )

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Column {
                                        Text(
                                            "🐶 Tên thú cưng: ${appointment.petName}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp
                                        )
                                        Text("📅 Ngày: ${appointment.date}", fontSize = 16.sp)
                                        Text("⏰ Giờ: ${appointment.time}", fontSize = 16.sp)
                                        Text(
                                            "📝 Ghi chú: ${if (appointment.note.isEmpty()) "Không có" else appointment.note}",
                                            fontSize = 16.sp,
                                            color = Color.Gray
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewBookingScreen() {
    val navController = rememberNavController()

    BookingScreen(navController = navController)
}
@Preview(showBackground = true)
@Composable
fun PreviewAppointmentDetailScreen() {
    val navController = rememberNavController()

    BookingScreen(navController = navController)
}