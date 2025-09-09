package com.example.meustock.ui.states

data class ForgotPasswordUiState(
    val email: String = "",
    val onEmailChange: (String) -> Unit = {},

)