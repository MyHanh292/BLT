package com.example.chamsocthucung2.view.user

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FolderShared
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
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
import com.example.chamsocthucung2.viewmodel.user.ProfileInfo
import com.google.firebase.auth.FirebaseAuth

@Composable
fun PetInfoScreen(navController: NavController, petViewModel: PetViewModel = viewModel()) {
    var petName by remember { mutableStateOf("") }
    var petType by remember { mutableStateOf("") }
    var petFeature by remember { mutableStateOf("") }
    var petWeight by remember { mutableStateOf("") }
    var petAge by remember { mutableStateOf("") }
    var ownerName by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    val context = LocalContext.current // Get context for Toast
    var showErrorDialog by remember { mutableStateOf(false) }
    var phoneError by remember { mutableStateOf("") }
    var weightError by remember { mutableStateOf("") }
    var ageError by remember { mutableStateOf("") }

    fun validatePhone(number: String): String {
        return if (number.length != 10 || !number.all { it.isDigit() }) {
            "Vui lòng nhập đúng định dạng SĐT (phải đủ 10 số)."
        } else {
            ""
        }
    }

    fun validateNumber(text: String): String {
        return if (!text.all { it.isDigit() }) {
            "Chỉ được nhập số thôi nha!"
        } else {
            ""
        }
    }
    fun validateAge(age: String): String {
        val error = validateNumber(age)
        if (error.isNotEmpty()) {
            return error
        }
        return if (age.isNotEmpty()) {
            val ageInt = age.toIntOrNull()
            if (ageInt == null || ageInt <= 0 || ageInt >= 30) {
                "Tuổi phải lớn hơn 0 và nhỏ hơn 30 nha!"
            } else {
                ""
            }
        } else {
            ""
        }
    }

    fun validateWeight(weight: String): String {
        val error = validateNumber(weight)
        if (error.isNotEmpty()) {
            return error
        }
        return if (weight.isNotEmpty()) {
            val weightDouble = weight.toDoubleOrNull()
            if (weightDouble == null || weightDouble <= 0 || weightDouble >= 50) {
                "Cân nặng phải lớn hơn 0 và nhỏ hơn 50 kg nha!"
            } else {
                ""
            }
        } else {
            ""
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nhập thông tin thú cưng", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                backgroundColor = Color.White,
                elevation = 0.dp,
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Quay lại")
                    }
                }
            )
        },
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            Text("Chọn loại thú cưng", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                PetTypeOption("Chó", R.drawable.cat_dog, petType) { petType = "Chó" }
                PetTypeOption("Mèo", R.drawable.cat_dog, petType) { petType = "Mèo" }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, shape = RoundedCornerShape(12.dp)),
                backgroundColor = Color.White,
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    PetInputField(label = "Tên thú cưng", value = petName, onValueChange = { petName = it })
                    PetInputField(label = "Đặc điểm", value = petFeature, onValueChange = { petFeature = it })
                    PetInputField(
                        label = "Cân nặng (kg)",
                        value = petWeight,
                        onValueChange = {
                            weightError = validateWeight(it)
                            petWeight = it
                        },
                        errorMessage = weightError
                    )
                    PetInputField(
                        label = "Tuổi",
                        value = petAge,
                        onValueChange = {
                            ageError = validateAge(it)
                            petAge = it
                        },
                        errorMessage = ageError
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text("Thông tin chủ nhân", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            PetInputField(label = "Họ tên", value = ownerName, onValueChange = { ownerName = it })
            PetInputField(label = "Địa chỉ", value = address, onValueChange = { address = it })
            PetInputField(
                label = "SĐT",
                value = phone,
                onValueChange = {
                    phoneError = validatePhone(it)
                    phone = it
                },
                errorMessage = phoneError
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val isPhoneValid = validatePhone(phone).isEmpty()
                    val isWeightValid = validateWeight(petWeight).isEmpty()
                    val isAgeValid = validateAge(petAge).isEmpty()

                    if (petName.isNotBlank() && petType.isNotBlank() && ownerName.isNotBlank() && phone.isNotBlank() && isPhoneValid && isWeightValid && isAgeValid) {
                        Log.d("PetInfoScreen", "PetInfo to save: petInfo")
                        Log.d("PetInfoScreen", "PetInfo in ViewModel before save: ${petViewModel.profileInfo.value}")
                        val petProfile = ProfileInfo(
                            petName = petName,
                            petType = petType,
                            petFeature = petFeature,
                            petWeight = petWeight,
                            petAge = petAge,
                            ownerName = ownerName,
                            address = address,
                            phone = phone
                        )
                        petViewModel.setProfileInfo(petProfile) // Cập nhật ProfileInfo trong ViewModel
                        petViewModel.savePetInfoToFireStore(petViewModel.profileInfo.value) // Lưu lên Firestore
                        Toast.makeText(context, "Thông tin đã được lưu!", Toast.LENGTH_SHORT).show()
                        navController.navigate("pet_profiledetail")
                    } else {
                        showErrorDialog = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .shadow(6.dp, shape = RoundedCornerShape(12.dp)),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFFA500))
            ) {
                Text("Lưu và xem hồ sơ", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            }

            if (showErrorDialog) {
                AlertDialog(
                    onDismissRequest = { showErrorDialog = false },
                    title = { Text("Lỗi") },
                    text = {
                        val message = StringBuilder("Vui lòng điền đầy đủ thông tin thú cưng và chủ nhân.\n")
                        if (phoneError.isNotEmpty()) {
                            message.append(phoneError).append("\n")
                        }
                        if (weightError.isNotEmpty()) {
                            message.append(weightError).append("\n")
                        }
                        if (ageError.isNotEmpty()) {
                            message.append(ageError)
                        }
                        Text(message.toString())
                    },
                    confirmButton = {
                        Button(onClick = { showErrorDialog = false }) {
                            Text("Đồng ý")
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun PetTypeOption(label: String, iconRes: Int, selectedType: String, onSelect: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (selectedType == label) Color(0xFFFFC107) else Color.LightGray) // 🌟 Vàng khi chọn
            .padding(12.dp)
            .clickable { onSelect() }
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = label,
            modifier = Modifier.size(60.dp)
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = if (selectedType == label) Color.Black else Color.DarkGray
        )
    }
}

@Composable
fun PetInputField(label: String, value: String, onValueChange: (String) -> Unit, errorMessage: String = "") {
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )
        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = Color.Red, fontSize = 12.sp)
        }
    }
}

@Composable
fun PetProfileHeader(profileInfo: ProfileInfo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF2F9FA)
        )
    ) {
        Row(
            modifier = Modifier
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Pet Image
            val petImageResource = if (profileInfo.petType?.lowercase() == "chó") {
                R.drawable.cat_dog
            } else if (profileInfo.petType?.lowercase() == "mèo") {
                R.drawable.image1
            } else {
                R.drawable.ic_profile
            }

            Image(
                painter = painterResource(id = petImageResource),
                contentDescription = "Ảnh thú cưng",
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape),
                contentScale = androidx.compose.ui.layout.ContentScale.Crop,
            )
            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    text = profileInfo.petName ?: "Chưa có tên",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = profileInfo.petType ?: "Chưa rõ loại",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
        }
    }
}


@Composable
fun PetProfileDetailScreen(navController: NavController, petViewModel: PetViewModel = viewModel()) {
    val profileInfo by petViewModel.profileInfo.collectAsState()
    val userId = FirebaseAuth.getInstance().currentUser?.uid

    LaunchedEffect(userId) {
        if (userId != null) {
            petViewModel.getPetInfo()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            Icons.Filled.FolderShared,
                            contentDescription = "Hồ sơ thú cưng",
                            tint = Color(0xFFE91E63),
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            "Hồ sơ thú cưng",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.weight(1f))

                        Icon(
                            Icons.Filled.Add,
                            contentDescription = "Thêm mới",
                            tint = Color(0xFFFDFCFC),
                            modifier = Modifier
                                .size(32.dp)
                                .clickable { navController.navigate("TAO_HO_SO") }
                        )
                    }
                },
                backgroundColor = Color(0xFFFFC107),
                elevation = 4.dp,
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Quay lại",
                            tint = Color.White,
                            modifier = Modifier.size(29.dp)
                        )
                    }
                }
            )
        },
        bottomBar = { BottomNavigationBar(navController) },
    ) { paddingValues ->
        // Phần nội dung khác của màn hình

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            PetProfileHeader(profileInfo = profileInfo)

            Spacer(modifier = Modifier.height(30.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Thông tin thú cưng",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF444444),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Divider(color = Color.LightGray, thickness = 1.dp)

                InfoRow(label = "Đặc điểm", value = profileInfo.petFeature ?: "")
                InfoRow(label = "Tuổi", value = profileInfo.petAge?.let { "$it tuổi" } ?: "")
                InfoRow(label = "Cân nặng", value = profileInfo.petWeight?.let { "$it kg" } ?: "")

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Người chăm sóc",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF444444),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Divider(color = Color.LightGray, thickness = 1.dp)

                InfoRow(label = "Họ tên", value = profileInfo.ownerName ?: "Nông Thị Mỹ Hạnh")
                InfoRow(label = "Địa chỉ", value = profileInfo.address ?: "")
                InfoRow(label = "SĐT", value = profileInfo.phone ?: "0273/422249")
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    petViewModel.getPetInfo()
                    navController.navigate("booking_screen")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFFA500))
            ) {
                Text("Hoàn tất hồ sơ", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 16.sp, color = Color.Gray)
        Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    Text(text = "Bottom Navigation Bar", modifier = Modifier.fillMaxWidth().padding(16.dp))
}

@Composable
fun CreatePetProfileScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tạo hồ sơ thú cưng mới", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                backgroundColor = Color.White,
                elevation = 0.dp,
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Quay lại")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "Màn hình tạo hồ sơ thú cưng (các trường nhập liệu sẽ ở đây)",
                fontSize = 16.sp,
                color = Color.Gray
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPetInfoScreen() {
    val navController = rememberNavController()
    val petViewModel = PetViewModel()
    PetInfoScreen(navController = navController, petViewModel = petViewModel)
}

@Preview(showBackground = true)
@Composable
fun PreviewPetProfileDetailScreen() {
    val navController = rememberNavController()
    val petViewModel = PetViewModel()
    PetProfileDetailScreen(navController = navController)}