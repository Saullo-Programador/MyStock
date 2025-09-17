package com.example.meustock.ui.states

data class UserUiState(
    val empresa: String = "",
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val passwordConfirm: String = "",
    val passwordCurrent: String = "",
    val onEmpresaChange: (String) -> Unit = {},
    val onUserNameChange: (String) -> Unit = {},
    val onEmailChange: (String) -> Unit = {},
    val onPasswordChange: (String) -> Unit = {},
    val onPasswordConfirmChange: (String) -> Unit = {},
    val onPasswordCurrentChange: (String) -> Unit = {},


)