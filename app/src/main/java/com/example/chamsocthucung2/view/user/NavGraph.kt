package com.example.chamsocthucung2.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.chamsocthucung2.view.login.*
//import com.example.chamsocthucung2.view.user.AccountScreen
import com.example.chamsocthucung2.view.user.AppointmentDetailScreen
import com.example.chamsocthucung2.view.user.BookingScreen
import com.example.chamsocthucung2.view.user.ChatScreen
import com.example.chamsocthucung2.view.user.HomeScreen
import com.example.chamsocthucung2.view.user.PetInfoScreen
import com.example.chamsocthucung2.view.user.PetProfileDetailScreen
import com.example.chamsocthucung2.view.user.TaoHoSoScreen
import com.example.chamsocthucung2.view.user.UserInfoScreen
import com.example.chamsocthucung2.viewmodel.AppViewModel // Import AppViewModel
import com.example.chamsocthucung2.viewmodel.login.LoginViewModel
import com.example.chamsocthucung2.viewmodel.user.PetViewModel
import com.google.firebase.auth.FirebaseAuth

object Routes {
    const val MAIN = "main_screen"
    const val REGISTER = "Register_Screen"
    const val FORGOT_PASSWORD = "Forgot_Password_Screen"
    const val CONFIRM_NEW_PASSWORD = "confirm_new_password_screen"
    const val VERIFY_PASSWORD = "verify_password_screen"
    const val VERIFY_PASSWORD_WITH_ARG = "verify_password/{phoneNumber}"
    const val HOME = "home_screen"
//    const val ACCOUNT = "account_screen"
    const val PET_INFO = "pet_info"
    const val PET_PROFILE_DETAIL = "pet_profiledetail"
    const val TAO_HO_SO = "tao_ho_so"
    const val BOOKING = "booking_screen"
    const val APPOINTMENT_DETAIL = "appointment_detail_screen"
    const val PROFILE = "profile_screen"
    const val CHAT = "chat_screen" // Sử dụng hằng số Routes.CHAT
}

@Composable
fun NavGraph(
    navController: NavHostController,
    appViewModel: AppViewModel, // Thêm AppViewModel
    mAuth: FirebaseAuth,
    loginViewModel: LoginViewModel,
    googleSignIn: () -> Unit,
    onSignSuccess: () -> Unit,
    signOut: () -> Unit,
    changeTheme: (Color) -> Unit
) {
    val petViewModel: PetViewModel = viewModel()
    val isLoggedIn by appViewModel.isLoggedIn.collectAsState()

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) Routes.HOME else Routes.MAIN
    ) {
        // 🔹 Màn hình xác thực
        composable(Routes.MAIN) { MainScreen(navController, viewModel()) }
        composable(Routes.REGISTER) {
            RegisterScreen(navController, loginViewModel) { userInfo ->
                appViewModel.setLoggedIn(true)
                appViewModel.updateUserInfo(userInfo)
                navController.navigate(Routes.TAO_HO_SO)
            }
        }
        composable(Routes.FORGOT_PASSWORD) { ForgotPasswordScreen(navController) }
        composable(Routes.CONFIRM_NEW_PASSWORD) { ConfirmNewPasswordScreen(navController) }
        composable(
            route = Routes.VERIFY_PASSWORD_WITH_ARG,
            arguments = listOf(navArgument("phoneNumber") { type = NavType.StringType })
        ) {
            val phoneNumber = it.arguments?.getString("phoneNumber") ?: ""
            VerifyPasswordScreen(phoneNumber, navController)
        }

        // 🔹 Màn hình chính
        composable(Routes.HOME) { HomeScreen(navController) }
//        composable(Routes.ACCOUNT) { AccountScreen(navController, mAuth, signOut, changeTheme) }

        // 🔹 Màn hình thông tin thú cưng
        composable(Routes.TAO_HO_SO) {
            TaoHoSoScreen(
                navController,
                appViewModel
            )
        } // Truyền appViewModel
        composable(Routes.PET_INFO) { PetInfoScreen(navController) }
        composable(Routes.PET_PROFILE_DETAIL) { PetProfileDetailScreen(navController) }

        composable(Routes.APPOINTMENT_DETAIL) {
            AppointmentDetailScreen(navController, petViewModel)
        }

        // 🔹 Màn hình đặt lịch và chi tiết cuộc hẹn
        composable(Routes.BOOKING) { BookingScreen(navController, petViewModel) }

        //Màn hình profile
        composable(Routes.PROFILE) {
            UserInfoScreen(navController, mAuth, signOut, changeTheme)
            }
            composable(Routes.CHAT)
            { ChatScreen(rememberNavController()) }
        }
    }
