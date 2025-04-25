package com.example.chamsocthucung2.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chamsocthucung2.data.local.firebase.AuthManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AppViewModel : ViewModel() {

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _hasPetProfile = MutableStateFlow(false)
    val hasPetProfile: StateFlow<Boolean> = _hasPetProfile

    private val _loggedInUserInfo = MutableStateFlow<Map<String, String>>(emptyMap())
    val loggedInUserInfo: StateFlow<Map<String, String>> = _loggedInUserInfo

    private val firestore = FirebaseFirestore.getInstance()

    init {
        _isLoggedIn.value = AuthManager().isLogged()
    }
    fun setLoggedIn(loggedIn: Boolean) {
        _isLoggedIn.value = loggedIn
    }

    fun setHasPetProfile(hasProfile: Boolean) {
        _hasPetProfile.value = hasProfile
    }

    fun updateUserInfo(userInfo: Map<String, String>) {
        _loggedInUserInfo.value = userInfo
    }

    fun checkIfUserHasPetProfile(userId: String) {
        viewModelScope.launch {
            try {
                val snapshot = firestore.collection("pet_profiles") // Thay "pet_profiles" bằng tên collection của bạn
                    .whereEqualTo("userId", userId)
                    .limit(1)
                    .get()
                    .await()
                _hasPetProfile.value = snapshot.documents.isNotEmpty()
            } catch (e: Exception) {
                Log.e("AppViewModel", "Lỗi kiểm tra hồ sơ thú cưng: ${e.message}")
                _hasPetProfile.value = false // Mặc định là chưa có nếu có lỗi
            }
        }
    }
}