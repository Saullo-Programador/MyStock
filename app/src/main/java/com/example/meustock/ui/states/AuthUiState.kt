package com.example.meustock.ui.states

import com.google.firebase.auth.FirebaseUser

data class AuthUiState(
    val isLoading: Boolean = false,
    val user: FirebaseUser? = null,
    val username: String? = null,
    val error: String? = null
)

