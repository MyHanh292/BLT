package com.example.chamsocthucung2.viewmodel.login

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.core.app.ComponentActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.chamsocthucung2.data.repository.login.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.core.Context

// 📌 Trạng thái đăng nhập
sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val userEmail: String) : LoginState()
    data class Failure(val errorMessage: String) : LoginState()
    object PasswordResetEmailSent : LoginState()  // 📌 Đã gửi email đặt lại mật khẩu
    object OTPSent : LoginState()
    object OTPVerified : LoginState()
}

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AuthRepository(application)
    val authRepository = AuthRepository(application) // Nếu bạn có context
    private val auth = FirebaseAuth.getInstance()
    private val _loginStatus = MutableLiveData<LoginState>(LoginState.Idle)
    val loginStatus: LiveData<LoginState> = _loginStatus
    var storedVerificationId: String? = null
    var resendToken: PhoneAuthProvider.ForceResendingToken? = null


    // 🔐 ĐĂNG NHẬP BẰNG EMAIL/PASSWORD
    fun signInWithEmail(email: String, password: String) {
        viewModelScope.launch {
            _loginStatus.postValue(LoginState.Loading)
            val result = repository.signInWithEmail(email, password)
            if (result.isSuccess) {
                val user = result.getOrNull()?.user
                _loginStatus.postValue(LoginState.Success(user?.email ?: "Unknown"))
            } else {
                _loginStatus.postValue(LoginState.Failure(result.exceptionOrNull()?.message ?: "Đăng nhập thất bại"))
            }
        }
    }

    // 🆕 ĐĂNG KÝ TÀI KHOẢN MỚI
    fun registerWithEmail(email: String, password: String) {
        viewModelScope.launch {
            _loginStatus.postValue(LoginState.Loading)
            val result = repository.signUpWithEmail(email, password)
            if (result.isSuccess) {
                val user = result.getOrNull()?.user
                _loginStatus.postValue(LoginState.Success(user?.email ?: "Unknown"))
            } else {
                _loginStatus.postValue(LoginState.Failure(result.exceptionOrNull()?.message ?: "Đăng ký thất bại"))
            }
        }
    }

    // ☁️ ĐĂNG NHẬP GOOGLE (Khởi tạo SignIn Intent)
    fun startGoogleSignIn(launcher: ActivityResultLauncher<IntentSenderRequest>) {
        viewModelScope.launch {
            repository.beginGoogleSignIn(launcher)
        }
    }

    // ☁️ XỬ LÝ KẾT QUẢ GOOGLE SIGN-IN
    fun handleGoogleSignInResult(data: Intent?) {
        viewModelScope.launch {
            _loginStatus.postValue(LoginState.Loading)
            val result = repository.handleGoogleSignIn(data)
            if (result.isSuccess) {
                val user = result.getOrNull()
                _loginStatus.postValue(LoginState.Success(user?.email ?: "Unknown"))
            } else {
                _loginStatus.postValue(LoginState.Failure(result.exceptionOrNull()?.message ?: "Đăng nhập Google thất bại"))
            }
        }
    }

    // 📝 ĐĂNG KÝ + LƯU THÔNG TIN NGƯỜI DÙNG MỚI VÀO FIRESTORE
    // ───────────────────────────────────────────────
    fun registerUserWithInfo(
        email: String,
        password: String,
        fullName: String,
        phone: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid ?: ""
                    val userInfo = mapOf(
                        "fullName" to fullName,
                        "phone" to phone,
                        "email" to email
                    )

                    Firebase.firestore.collection("users").document(userId)
                        .set(userInfo)
                        .addOnSuccessListener {
                            onSuccess()
                        }
                        .addOnFailureListener { e ->
                            onError("Lỗi khi lưu thông tin: ${e.message}")
                        }
                } else {
                    val exception = task.exception
                    if (exception is FirebaseAuthUserCollisionException) {
                        onError("Email đã được đăng ký.")
                    } else {
                        onError(exception?.message ?: "Đăng ký thất bại. Vui lòng thử lại.")
                    }
                }
            }
    }

    // Kiểm tra thông tin đăng nhập khi ứng dụng khởi động
    fun checkLoginStatus(): Pair<String?, String?> {
        return repository.getUserSession()
    }

    // Đăng xuất và xóa thông tin đăng nhập
    fun logout() {
        repository.clearUserSession()
        _loginStatus.value = LoginState.Idle
    }


    // ─────────────────────────────────────────────
    // 📧 Gửi link đặt lại mật khẩu qua Email
    // ─────────────────────────────────────────────
    fun forgotPassword(email: String) {
        _loginStatus.value = LoginState.Loading
        viewModelScope.launch {
            val result = authRepository.sendPasswordReset(email)
            _loginStatus.value = if (result.isSuccess) {
                LoginState.PasswordResetEmailSent
            } else {
                LoginState.Failure(result.exceptionOrNull()?.message ?: "Đã xảy ra lỗi khi gửi email")
            }
        }
    }

    // ─────────────────────────────────────────────
    // 📲 Gửi OTP tới số điện thoại
    // ─────────────────────────────────────────────
    fun sendOtp(phoneNumber: String, @SuppressLint("RestrictedApi") activity: ComponentActivity) {
        _loginStatus.value = LoginState.Loading
        val formattedPhone = authRepository.formatPhoneToE164(phoneNumber)

        authRepository.verifyPhoneNumber(
            phoneNumber = formattedPhone, // ✅ Sử dụng số đã format
            activity = activity,
            onVerificationCompleted = { credential ->
                signInWithPhoneCredential(credential)
            },
            onVerificationFailed = { exception ->
                _loginStatus.value = LoginState.Failure(exception.message ?: "Xác minh thất bại")
            },
            onCodeSent = { verificationId, token ->
                verificationId.also { this.verificationId = it }
                token.also { this.resendToken = it }
                _loginStatus.value = LoginState.OTPSent
            }
        )
    }
    var verificationId: String? = null
    fun updateVerification(id: String, token: PhoneAuthProvider.ForceResendingToken) {
        verificationId = id
        resendToken = token
    }

    fun resendOtp(phoneNumber: String, @SuppressLint("RestrictedApi") activity: ComponentActivity, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val token = resendToken
        if (token != null) {
            val formattedPhone = authRepository.formatPhoneToE164(phoneNumber)
            authRepository.verifyPhoneNumber(
                phoneNumber = formattedPhone,
                activity = activity,
                onVerificationCompleted = { credential ->
                    signInWithPhoneCredential(credential)
                },
                onVerificationFailed = { exception ->
                    onError(exception.message ?: "Gửi lại OTP thất bại")
                },
                onCodeSent = { id, newToken ->
                    updateVerification(id, newToken)
                    onSuccess()
                },
                forceResendingToken = token
            )
        } else {
            onError("Không có token để gửi lại OTP")
        }
    }

    // ─────────────────────────────────────────────
    // ✅ Xác minh mã OTP nhập từ người dùng
    // ─────────────────────────────────────────────
    fun verifyOtpCode(otp: String) {
        val verificationId = storedVerificationId ?: return
        val credential = PhoneAuthProvider.getCredential(verificationId, otp)

        _loginStatus.value = LoginState.Loading
        viewModelScope.launch {
            val result = authRepository.signInWithPhoneAuthCredential(credential)
            _loginStatus.value = if (result.isSuccess) {
                val user = result.getOrNull()
                val email = user?.email ?: user?.phoneNumber ?: "Không có email"
                LoginState.Success(userEmail = email)
            } else {
                LoginState.Failure(result.exceptionOrNull()?.message ?: "Xác thực thất bại")
            }
        }
    }

    private fun signInWithPhoneCredential(credential: PhoneAuthCredential) {
        _loginStatus.value = LoginState.Loading
        viewModelScope.launch {
            val result = authRepository.signInWithPhoneAuthCredential(credential)
            _loginStatus.value = if (result.isSuccess) {
                LoginState.OTPVerified
            } else {
                LoginState.Failure(result.exceptionOrNull()?.message ?: "Đăng nhập bằng OTP thất bại")
            }
        }
    }

    fun resetLoginState() {
        _loginStatus.value = LoginState.Idle
    }

}
