package com.example.chamsocthucung2.view.user


import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.chamsocthucung2.R
import com.example.chamsocthucung2.data.local.AppDatabase
import com.example.chamsocthucung2.data.model.user.ProfileUserEntity
import com.example.chamsocthucung2.data.repository.user.ProfileRepository
import com.example.chamsocthucung2.ui.components.BottomNavigationBar
import com.example.chamsocthucung2.viewmodel.ProfileViewModel
import com.example.chamsocthucung2.viewmodel.user.ProfileViewModelFactory
//import com.example.chamsocthucung2.data.model.user.ProfileUserEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInfoScreen(
    navController: NavController,
    mAuth: FirebaseAuth,
    googleSignIn: () -> Unit,
    changeTheme: (Color) -> Unit,
) {
    val context = LocalContext.current
    val viewModelStoreOwner = context as ViewModelStoreOwner


    // Khởi tạo ViewModel không dùng Hilt
    val userDao = remember { AppDatabase.getDatabase(context).profileUserDao() }
    val profileRepository = remember { ProfileRepository(userDao) }
    val profileViewModel = remember {
        ViewModelProvider(viewModelStoreOwner, ProfileViewModelFactory(profileRepository))
            .get(ProfileViewModel::class.java)
    }


    val user = mAuth.currentUser
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedColor by remember { mutableStateOf(Color(0xFFF8E7C0)) }


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                navController = navController,
                onItemClick = { scope.launch { drawerState.close() } }
            )
        },
        scrimColor = Color.Transparent
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Thông tin tài khoản", fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Quay lại")
                        }
                    },
                    actions = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    colors = TopAppBarDefaults.mediumTopAppBarColors(
                        containerColor = Color(0xFFFF9800),
                        navigationIconContentColor = Color.White,
                        actionIconContentColor = Color.White
                    )
                )
            },
            bottomBar = { BottomNavigationBar(navController = navController) },
            containerColor = selectedColor
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Avatar Section
                UserAvatarSection(
                    user = FirebaseAuth.getInstance().currentUser,
                    launcher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.GetContent(),
                        onResult = { uri: Uri? ->
                            uri?.let { updateAvatarInFirebase(it) }
                        }
                    )
                )
                Spacer(modifier = Modifier.height(24.dp))


                // Scrollable Form
                UserInfoForm(
                    profileViewModel = profileViewModel,
                    navController = navController
                )


                // Logout Button
                Button(
                    onClick = {
                        googleSignIn()
                        mAuth.signOut()
                        navController.navigate("main_screen") {
                            popUpTo("main_screen") { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Đăng xuất", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}


@Composable
private fun UserAvatarSection(
    user: FirebaseUser?,
    launcher: ManagedActivityResultLauncher<String, Uri?>
) {
    Box(
        modifier = Modifier
            .size(130.dp)
            .background(Color.LightGray, shape = CircleShape)
            .border(4.dp, Color.White, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = rememberAsyncImagePainter(user?.photoUrl ?: "https://via.placeholder.com/150"),
            contentDescription = "Avatar",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .clickable { launcher.launch("image/*") },
            contentScale = ContentScale.Crop
        )
    }


    Spacer(modifier = Modifier.height(16.dp))


    Text(
        text = user?.displayName ?: "Chưa có tên",
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp
    )
    Text(
        text = user?.email ?: "Không có email",
        fontSize = 16.sp,
        color = Color.Gray
    )
}
fun updateAvatarInFirebase(uri: Uri) {
    val user = FirebaseAuth.getInstance().currentUser
    val profileUpdates = UserProfileChangeRequest.Builder()
        .setPhotoUri(uri)
        .build()


    user?.updateProfile(profileUpdates)
        ?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("Avatar", "Avatar updated successfully!")
            } else {
                Log.d("Avatar", "Error updating avatar")
            }
        }
}


@Composable
fun DrawerContent(navController: NavController, onItemClick: () -> Unit = {}) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val firebaseUser = FirebaseAuth.getInstance().currentUser
    val defaultAvatarUrl = firebaseUser?.photoUrl?.toString()
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }


    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(Color.White)
            .padding(16.dp)
    ) {
        AvatarEditSection(
            selectedImageUri = selectedImageUri,
            launcher = launcher,
            defaultAvatarUrl = defaultAvatarUrl
        )


        Spacer(modifier = Modifier.height(16.dp))
        Divider()


        DrawerMenuItems(navController, onItemClick)
    }
}


@Composable
fun AvatarEditSection(
    selectedImageUri: Uri?,
    launcher: ManagedActivityResultLauncher<String, Uri?>,
    defaultAvatarUrl: String? // ← avatar từ Firebase
) {
    val avatarPainter = when {
        selectedImageUri != null -> rememberAsyncImagePainter(selectedImageUri)
        !defaultAvatarUrl.isNullOrEmpty() -> rememberAsyncImagePainter(defaultAvatarUrl)
        else -> painterResource(id = R.drawable.ic_profile)
    }


    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color.Gray)
        ) {
            Image(
                painter = avatarPainter,
                contentDescription = "Avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { launcher.launch("image/*") }
            )


            Icon(
                painter = painterResource(id = R.drawable.ic_email),
                contentDescription = "Change Avatar",
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.BottomEnd)
                    .offset(x = (-4).dp, y = (-4).dp)
                    .background(Color.White, CircleShape)
                    .clickable { launcher.launch("image/*") },
                tint = Color.Black
            )
        }
    }
}


@Composable
private fun DrawerMenuItems(
    navController: NavController,
    onItemClick: () -> Unit = {}
) {
    val menuItems = listOf(
        MenuItem("Hồ sơ", "profile_screen", Color(0xFF4CAF50)),
        MenuItem("Mật Khẩu", "password_screen", Color(0xFF2196F3)),
        MenuItem("Tùy chỉnh", "settings_screen", Color(0xFF9C27B0)),
        MenuItem("Thông báo", "notifications_screen", Color(0xFFFF9800)),
        MenuItem("Giới thiệu", "about_screen", Color(0xFFE91E63))
    )


    LazyColumn {
        items(menuItems) { item ->
            ListItem(
                headlineContent = { Text(item.title) },
                leadingContent = {
                    Icon(
                        imageVector = when (item.title) {
                            "Hồ sơ" -> Icons.Default.Person
                            "Mật Khẩu" -> Icons.Default.Lock
                            "Tùy chỉnh" -> Icons.Default.Settings
                            "Thông báo" -> Icons.Default.Notifications
                            "Giới thiệu" -> Icons.Default.Info
                            else -> Icons.Default.AccountCircle
                        },
                        contentDescription = item.title
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate(item.route)
                        onItemClick()
                    },
                colors = ListItemDefaults.colors(containerColor = item.color.copy(alpha = 0.1f))
            )
            Divider()
        }
    }
}


@Composable
fun PasswordScreen(
    onChangePassword: (String, String) -> Unit // oldPass, newPass
) {
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmNewPassword by remember { mutableStateOf("") }


    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {


        Text("Đổi mật khẩu", style = MaterialTheme.typography.headlineSmall)


        OutlinedTextField(
            value = oldPassword,
            onValueChange = { oldPassword = it },
            label = { Text("Mật khẩu hiện tại") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )


        Spacer(Modifier.height(8.dp))


        OutlinedTextField(
            value = newPassword,
            onValueChange = { newPassword = it },
            label = { Text("Mật khẩu mới") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )


        Spacer(Modifier.height(8.dp))


        OutlinedTextField(
            value = confirmNewPassword,
            onValueChange = { confirmNewPassword = it },
            label = { Text("Xác nhận mật khẩu mới") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )


        Spacer(Modifier.height(16.dp))


        Button(
            onClick = {
                if (newPassword == confirmNewPassword) {
                    onChangePassword(oldPassword, newPassword)
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Xác nhận")
        }
    }
}
@Composable
fun SettingsScreen(
    isDarkMode: Boolean,
    onToggleDarkMode: () -> Unit,
    onColorChange: (Color) -> Unit
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {


        Text("Tùy chỉnh giao diện", style = MaterialTheme.typography.headlineSmall)


        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Chế độ tối")
            Spacer(Modifier.weight(1f))
            Switch(checked = isDarkMode, onCheckedChange = { onToggleDarkMode() })
        }


        Spacer(Modifier.height(16.dp))


        Text("Chọn màu nền", style = MaterialTheme.typography.titleMedium)
        Row(Modifier.padding(top = 8.dp)) {
            listOf(Color.Red, Color.Blue, Color.Green, Color.Magenta).forEach { color ->
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .padding(4.dp)
                        .clip(CircleShape)
                        .background(color)
                        .clickable { onColorChange(color) }
                )
            }
        }
    }
}
@Composable
fun NotificationsScreen(
    emailNoti: Boolean,
    pushNoti: Boolean,
    onEmailToggle: () -> Unit,
    onPushToggle: () -> Unit
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {


        Text("Cài đặt Thông báo", style = MaterialTheme.typography.headlineSmall)


        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Thông báo Email")
            Spacer(Modifier.weight(1f))
            Switch(checked = emailNoti, onCheckedChange = { onEmailToggle() })
        }


        Spacer(Modifier.height(8.dp))


        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Thông báo Đẩy (Push)")
            Spacer(Modifier.weight(1f))
            Switch(checked = pushNoti, onCheckedChange = { onPushToggle() })
        }
    }
}
@Composable
fun AboutScreen() {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {


        Text("Giới thiệu ứng dụng", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(8.dp))


        Text("Phiên bản: 1.0.0")
        Text("Tác giả: Nhóm UTH App Team")
        Text("Email hỗ trợ: support@uthapp.com")
    }
}




data class MenuItem(
    val title: String,
    val route: String,
    val color: Color
)


@Composable
fun UserInfoForm(
    profileViewModel: ProfileViewModel,
    navController: NavController
) {
    val userProfile by profileViewModel.userProfile.collectAsState()
    val errorMessage by profileViewModel.errorMessage.collectAsState()


    // Biến trạng thái các trường nhập liệu
    var firstName by remember { mutableStateOf(userProfile?.firstName ?: "") }
    var lastName by remember { mutableStateOf(userProfile?.lastName ?: "") }
    var email by remember { mutableStateOf(userProfile?.email ?: "") }
    var gender by remember { mutableStateOf(userProfile?.gender ?: "") }
    var role by remember { mutableStateOf(userProfile?.role ?: "") }


    // Cập nhật lại các giá trị khi thông tin người dùng thay đổi
    LaunchedEffect(userProfile) {
        firstName = userProfile?.firstName ?: ""
        lastName = userProfile?.lastName ?: ""
        email = userProfile?.email ?: ""
        gender = userProfile?.gender ?: ""
        role = userProfile?.role ?: ""
    }


    // Biến để kiểm tra khi người dùng muốn thiết lập lại thông tin
    var showResetDialog by remember { mutableStateOf(false) }


    Column(modifier = Modifier.padding(16.dp)) {
        // Các trường nhập liệu vẫn giữ nguyên
        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("Tên") },
            modifier = Modifier.fillMaxWidth()
        )


        Spacer(modifier = Modifier.height(8.dp))


        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Họ") },
            modifier = Modifier.fillMaxWidth()
        )


        Spacer(modifier = Modifier.height(8.dp))


        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )


        Spacer(modifier = Modifier.height(8.dp))


        OutlinedTextField(
            value = gender,
            onValueChange = { gender = it },
            label = { Text("Giới tính") },
            modifier = Modifier.fillMaxWidth()
        )


        Spacer(modifier = Modifier.height(8.dp))


        OutlinedTextField(
            value = role,
            onValueChange = { role = it },
            label = { Text("Vai trò") },
            modifier = Modifier.fillMaxWidth()
        )


        Spacer(modifier = Modifier.height(16.dp))


        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            // Nút Thiết lập lại
            OutlinedButton(onClick = {
                showResetDialog = true
            }) {
                Text("Thiết lập lại")
            }


            // Nút Lưu
            Button(onClick = {
                // Lưu thông tin người dùng
                val user = ProfileUserEntity(
                    firstName = firstName,
                    lastName = lastName,
                    email = email,
                    gender = gender,
                    role = role,
                    uid = userProfile?.uid ?: "", // Lấy UID từ userProfile nếu có
                    avatarUrl = userProfile?.avatarUrl
                )
                profileViewModel.saveUser(user)
            }) {
                Text("Lưu")
            }
        }


        // Hiển thị lỗi nếu có
        errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }


    // Dialog xác nhận thiết lập lại
    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Xác nhận") },
            text = { Text("Bạn chắc chắn muốn thiết lập lại?") },
            confirmButton = {
                TextButton(onClick = {
                    // Đặt lại các giá trị ban đầu
                    firstName = userProfile?.firstName ?: ""
                    lastName = userProfile?.lastName ?: ""
                    gender = userProfile?.gender ?: ""
                    role = userProfile?.role ?: ""
                    showResetDialog = false
                }) {
                    Text("Đồng ý")
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("Hủy")
                }
            }
        )
    }
}


