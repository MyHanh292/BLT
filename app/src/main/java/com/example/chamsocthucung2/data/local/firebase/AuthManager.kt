package com.example.chamsocthucung2.data.local.firebase

import com.google.firebase.auth.FirebaseAuth

class AuthManager {
    private val auth = FirebaseAuth.getInstance()

    fun isLogged(): Boolean {
        return auth.currentUser != null
    }
}