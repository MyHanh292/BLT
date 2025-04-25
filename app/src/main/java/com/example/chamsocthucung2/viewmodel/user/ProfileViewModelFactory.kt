package com.example.chamsocthucung2.viewmodel.user


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.chamsocthucung2.data.repository.user.ProfileRepository
import com.example.chamsocthucung2.viewmodel.ProfileViewModel


class ProfileViewModelFactory(
    private val profileRepository: ProfileRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(profileRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


