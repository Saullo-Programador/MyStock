package com.example.meustock.ui.states

data class SignUpUiState(
    val empresaName: String = "",
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val passwordConfirmation: String = "",
    val onEmpresaNameChange: (String) -> Unit = {},
    val onUsernameChange: (String) -> Unit = {},
    val onEmailChange: (String) -> Unit = {},
    val onPasswordChange: (String) -> Unit = {},
    val onPasswordConfirmationChange: (String) -> Unit = {},
    val check: Boolean = false,
    val onCheckChange: (Boolean) -> Unit = {}
)