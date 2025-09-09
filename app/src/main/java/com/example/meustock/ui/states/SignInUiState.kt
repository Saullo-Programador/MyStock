package com.example.meustock.ui.states

data class SignInUiState(
    val email: String = "",
    val password: String = "",
    val onEmailChange: (String) -> Unit = {},
    val onPasswordChange: (String) -> Unit = {},
    val isResetEmailSent: Boolean = false
)