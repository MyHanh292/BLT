package com.example.chamsocthucung2.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chamsocthucung2.data.model.user.ProfileUserEntity
import com.example.chamsocthucung2.data.repository.user.ProfileRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class ProfileViewModel(
    private val profileRepository: ProfileRepository
) : ViewModel() {


    private val _userProfile = MutableStateFlow<ProfileUserEntity?>(null)
    val userProfile: StateFlow<ProfileUserEntity?> = _userProfile.asStateFlow()


    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()


    fun saveUser(user: ProfileUserEntity) {
        viewModelScope.launch {
            try {
                profileRepository.saveUser(user)
                _userProfile.value = user
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }


    fun getUserByEmail(email: String) {
        viewModelScope.launch {
            try {
                _userProfile.value = profileRepository.getUserByEmail(email)
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }


    fun deleteUser(user: ProfileUserEntity) {
        viewModelScope.launch {
            try {
                profileRepository.deleteUser(user)
                _userProfile.value = null
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }


    fun insertUser(user: ProfileUserEntity) {
        viewModelScope.launch {
            try {
                profileRepository.insertUser(user)
                _userProfile.value = user
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }


    fun insertOrUpdate(user: ProfileUserEntity) {
        viewModelScope.launch {
            try {
                profileRepository.insertOrUpdate(user)
                _userProfile.value = user
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }


    fun getUserByIdFlow(userId: String): Flow<ProfileUserEntity?> {
        return profileRepository.getUserById(userId)
    }


    fun getProfileFlow(email: String): Flow<ProfileUserEntity> {
        return profileRepository.getProfileFlow(email)
    }
}
