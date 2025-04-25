//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.*
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Add
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material.icons.filled.Search
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.draw.shadow
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavController
//import com.example.chamsocthucung2.R
//import com.example.chamsocthucung2.navigation.Routes
//import com.example.chamsocthucung2.ui.components.BottomNavigationBar
//import com.example.chamsocthucung2.viewmodel.user.PetInfo // Import PetInfo
//import com.example.chamsocthucung2.viewmodel.user.PetViewModel
//
//@Composable
//fun PetListScreen(navController: NavController, petViewModel: PetViewModel = viewModel()) {
//    val petList by petViewModel.petList.collectAsState()
//
//    // Gọi getPetList khi màn hình được tạo hoặc khi có thay đổi userId (nếu cần)
//    LaunchedEffect(Unit) {
//        petViewModel.getPetList()
//    }
//
//    Scaffold(
//        topBar = { /* ... */ },
//        bottomBar = { /* ... */ }
//    ) { paddingValues ->
//        Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color(0xFFFFE4B5), Color.White))).padding(paddingValues).padding(horizontal = 16.dp)) {
//            Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
//                Text("🐶 Danh sách thú cưng", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF444444))
//                Spacer(modifier = Modifier.height(16.dp))
//                OutlinedTextField(value = "", onValueChange = {}, leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Tìm kiếm", tint = Color.Gray) }, placeholder = { Text("Tìm kiếm theo tên hoặc loại", color = Color.Gray) }, modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), shape = RoundedCornerShape(8.dp), colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = Color(0xFFFFA500), unfocusedBorderColor = Color.LightGray, cursorColor = Color(0xFFFFA500)))
//                if (petList.isEmpty()) {
//                    Text("Chưa có hồ sơ thú cưng nào.", fontSize = 18.sp, color = Color.Gray)
//                } else {
//                    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
//                        items(petList) { pet ->
//                            PetListItemRow(pet = pet) { petId ->
//                                navController.navigate(Routes.PET_PROFILE_DETAIL.replace("{petId}", petId))
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun PetListItemRow(pet: PetInfo, onPetClick: (String) -> Unit) { // Sử dụng PetInfo
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clickable { onPetClick(pet.id) }
//            .padding(vertical = 4.dp)
//            .shadow(4.dp, shape = RoundedCornerShape(12.dp)),
//        shape = RoundedCornerShape(12.dp),
//        backgroundColor = Color.White,
//        elevation = 2.dp
//    ) {
//        Row(
//            modifier = Modifier
//                .padding(16.dp)
//                .fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            val petImageResource = if (!pet.petImage.isNullOrEmpty()) { // Sử dụng petImage
//                R.drawable.cat_dog
//            } else {
//                R.drawable.cat_dog
//            }
//
//            Image(
//                painter = painterResource(id = petImageResource),
//                contentDescription = pet.petName ?: "Hình thú cưng", // Sử dụng petName
//                modifier = Modifier
//                    .size(80.dp)
//                    .clip(CircleShape)
//                    .background(Color(0xFFFFA500)),
//                contentScale = ContentScale.Crop
//            )
//            Spacer(modifier = Modifier.width(16.dp))
//            Column(verticalArrangement = Arrangement.Center) {
//                Text(text = pet.petName ?: "Tên thú cưng", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF444444)) // Sử dụng petName
//                Text(text = "Loại: ${pet.petType ?: "Không rõ"}", color = Color.Gray, fontSize = 14.sp) // Sử dụng petType
//                // Thêm thông tin phụ nếu cần
//            }
//        }
//    }
//}