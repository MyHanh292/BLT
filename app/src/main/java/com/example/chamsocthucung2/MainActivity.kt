package com.example.chamsocthucung2

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.example.chamsocthucung2.navigation.NavGraph
import com.example.chamsocthucung2.navigation.Routes
import com.example.chamsocthucung2.ui.theme.ChamSocThuCung2Theme
import com.example.chamsocthucung2.viewmodel.AppViewModel // Import AppViewModel
import com.example.chamsocthucung2.viewmodel.login.LoginViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val loginViewModel: LoginViewModel by viewModels()
    private val appViewModel: AppViewModel by viewModels() // Thêm AppViewModel
    private lateinit var googleSignInLauncher: ActivityResultLauncher<IntentSenderRequest>
    private lateinit var navController: androidx.navigation.NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        // Launcher dùng cho Identity API
        googleSignInLauncher =
            registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    loginViewModel.handleGoogleSignInResult(result.data)
                } else {
                    Log.e("GoogleSignIn", "Đăng nhập Google bị hủy hoặc thất bại")
                    Toast.makeText(this, "Đăng nhập Google thất bại", Toast.LENGTH_SHORT).show()
                }
            }

        setContent {
            navController = rememberNavController()
            var currentThemeColor by remember { mutableStateOf(Color(0xFFF8E7C0)) }

            ChamSocThuCung2Theme {
                NavGraph(
                    navController = navController,
                    appViewModel = appViewModel, // Truyền AppViewModel
                    mAuth = FirebaseAuth.getInstance(),
                    loginViewModel = loginViewModel,
                    googleSignIn = {
                        loginViewModel.startGoogleSignIn(googleSignInLauncher)
                    },
                    onSignSuccess = {
                        // Đăng nhập thành công, điều hướng từ ViewModel rồi
                    },
                    signOut = {
                        FirebaseAuth.getInstance().signOut()
                        navController.navigate(Routes.MAIN) {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        }
                        Toast.makeText(this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show()
                    },
                    changeTheme = { color ->
                        currentThemeColor = color
                    }
                )
            }
        }
    }
}