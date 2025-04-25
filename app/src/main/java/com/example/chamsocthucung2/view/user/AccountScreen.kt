//package com.example.chamsocthucung2.view.user
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.lazy.LazyRow
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.AlertDialog
//import androidx.compose.material.Button
//import androidx.compose.material.ButtonDefaults
//import androidx.compose.material.Card
//import androidx.compose.material.Divider
//import androidx.compose.material.Icon
//import androidx.compose.material.Scaffold
//import androidx.compose.material.Text
//import androidx.compose.material.TextButton
//import androidx.compose.material.TopAppBar
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Backup
//import androidx.compose.material.icons.filled.ChevronRight
//import androidx.compose.material.icons.filled.ColorLens
//import androidx.compose.material.icons.filled.Feedback
//import androidx.compose.material.icons.filled.Help
//import androidx.compose.material.icons.filled.Info
//import androidx.compose.material.icons.filled.Notifications
//import androidx.compose.material.icons.filled.Person
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.TopAppBar
//import androidx.compose.material3.TopAppBarDefaults
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.draw.shadow
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavController
//import coil.compose.rememberAsyncImagePainter
//import com.google.firebase.auth.FirebaseAuth
//
//@Composable
//fun AccountScreen(
//    navController: NavController,
//    mAuth: FirebaseAuth,
//    googleSignIn: () -> Unit,
//    onThemeChange: (Color) -> Unit
//) {
//    val user = mAuth.currentUser
//    val context = LocalContext.current
//    var selectedColor by remember { mutableStateOf(Color(0xFFF8E7C0)) }
//    val themeColors = listOf(
//        Color(0xFFF8E7C0), // Màu be nhạt
//        Color(0xFFFFF3E0), // Màu cam nhạt
//        Color(0xFFE8F5E9), // Màu xanh lá nhạt
//        Color(0xFFE3F2FD)  // Màu xanh dương nhạt
//    )
//    var showThemeDialog by remember { mutableStateOf(false) }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Cài đặt tài khoản", fontWeight = FontWeight.Bold) },
//                backgroundColor = MaterialTheme.colorScheme.primary,
//                contentColor = MaterialTheme.colorScheme.onPrimary
//            )
//        },
//        backgroundColor = MaterialTheme.colorScheme.background
//    ) { paddingValues ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//                .verticalScroll(rememberScrollState())
//        ) {
//            // Phần thông tin user
//            Card(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp),
//                elevation = 4.dp,
//                shape = RoundedCornerShape(12.dp)
//            ) {
//                Column(
//                    modifier = Modifier.padding(16.dp),
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    // Avatar
//                    Box(
//                        modifier = Modifier
//                            .size(100.dp)
//                            .background(Color.LightGray, shape = CircleShape)
//                            .border(3.dp, MaterialTheme.colorScheme.primary, shape = CircleShape),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        if (user?.photoUrl != null) {
//                            Image(
//                                painter = rememberAsyncImagePainter(user.photoUrl),
//                                contentDescription = "Avatar",
//                                modifier = Modifier
//                                    .size(95.dp)
//                                    .clip(CircleShape),
//                                contentScale = ContentScale.Crop
//                            )
//                        } else {
//                            Icon(
//                                imageVector = Icons.Default.Person,
//                                contentDescription = "Avatar",
//                                modifier = Modifier.size(50.dp),
//                                tint = Color.White
//                            )
//                        }
//                    }
//
//                    Spacer(modifier = Modifier.height(16.dp))
//
//                    // Thông tin
//                    Text(
//                        text = user?.displayName ?: "Khách",
//                        fontWeight = FontWeight.Bold,
//                        fontSize = 20.sp
//                    )
//                    Text(
//                        text = user?.email ?: "Chưa đăng nhập",
//                        fontSize = 14.sp,
//                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
//                    )
//                }
//            }
//
//            // Danh sách cài đặt
//            Card(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 16.dp),
//                elevation = 4.dp,
//                shape = RoundedCornerShape(12.dp)
//            ) {
//                Column {
//                    // Đổi màu giao diện
//                    SettingItem(
//                        icon = Icons.Default.ColorLens,
//                        title = "Đổi màu giao diện",
//                        onClick = { showThemeDialog = true }
//                    )
//                    Divider(thickness = 0.5.dp)
//
//                    // Sao lưu dữ liệu
//                    SettingItem(
//                        icon = Icons.Default.Backup,
//                        title = "Sao lưu & Khôi phục",
//                        onClick = { /* Xử lý sao lưu */ }
//                    )
//                    Divider(thickness = 0.5.dp)
//
//                    // Phản hồi
//                    SettingItem(
//                        icon = Icons.Default.Feedback,
//                        title = "Gửi phản hồi",
//                        onClick = { /* Xử lý phản hồi */ }
//                    )
//                    Divider(thickness = 0.5.dp)
//
//                    // Câu hỏi thường gặp
//                    SettingItem(
//                        icon = Icons.Default.Help,
//                        title = "Câu hỏi thường gặp",
//                        onClick = { navController.navigate("faq_screen") }
//                    )
//                    Divider(thickness = 0.5.dp)
//
//                    // Thông tin phiên bản
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(16.dp),
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Icon(
//                            imageVector = Icons.Default.Info,
//                            contentDescription = "Phiên bản",
//                            modifier = Modifier.padding(end = 16.dp),
//                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
//                        )
//                        Column(modifier = Modifier.weight(1f)) {
//                            Text("Phiên bản", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
//                            Text("1.2.1 (10)", fontWeight = FontWeight.Medium)
//                        }
//                    }
//                }
//            }
//
//            Spacer(modifier = Modifier.height(32.dp))
//            Button(
//                onClick = {
//                    googleSignIn()
//                    mAuth.signOut()
//                    navController.navigate("main_screen") {
//                        popUpTo("main_screen") { inclusive = true }
//                    }
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(60.dp)
//                    .shadow(elevation = 4.dp, shape = RoundedCornerShape(12.dp), clip = true), // Thêm đổ bóng và bo tròn góc
//                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
//                shape = RoundedCornerShape(12.dp) // Bo tròn góc cho nút
//            ) {
//                Text(
//                    "Đăng xuất",
//                    color = Color.White,
//                    fontWeight = FontWeight.Bold,
//                    fontSize = 18.sp
//                )
//            }
//        }
//    }
//    // Dialog chọn màu
//    if (showThemeDialog) {
//        AlertDialog(
//            onDismissRequest = { showThemeDialog = false },
//            title = { Text("Chọn màu giao diện") },
//            text = {
//                LazyRow(
//                    horizontalArrangement = Arrangement.spacedBy(16.dp),
//                    contentPadding = PaddingValues(vertical = 8.dp)
//                ) {
//                    items(themeColors) { color ->
//                        Box(
//                            modifier = Modifier
//                                .size(50.dp)
//                                .clip(CircleShape)
//                                .background(color)
//                                .border(
//                                    2.dp,
//                                    if (color == selectedColor) MaterialTheme.colorScheme.primary
//                                    else Color.Transparent,
//                                    CircleShape
//                                )
//                                .clickable {
//                                    selectedColor = color
//                                    onThemeChange(color)
//                                }
//                        )
//                    }
//                }
//            },
//            confirmButton = {
//                TextButton(onClick = { showThemeDialog = false }) {
//                    Text("XONG")
//                }
//            }
//        )
//    }
//}
//@Composable
//fun SettingItem(
//    icon: ImageVector,
//    title: String,
//    onClick: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    Row(
//        modifier = modifier
//            .fillMaxWidth()
//            .clickable(onClick = onClick)
//            .padding(16.dp),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Icon(
//            imageVector = icon,
//            contentDescription = title,
//            modifier = Modifier.padding(end = 16.dp),
//            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
//        )
//        Text(
//            text = title,
//            modifier = Modifier.weight(1f),
//            color = MaterialTheme.colorScheme.onSurface
//        )
//        Icon(
//            imageVector = Icons.Default.ChevronRight,
//            contentDescription = null,
//            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
//        )
//    }
//}