package com.example.meustock.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meustock.authentication.FirebaseAuthManager
import com.example.meustock.ui.states.UserUiState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class UserEvent{
    object Loading : UserEvent()
    data class Success(val uiState: UserUiState) : UserEvent()
    data class Error(val message: String) : UserEvent()
    object Idle : UserEvent()
}

@HiltViewModel
class UserUpdateViewModel @Inject constructor(
    private val authManager: FirebaseAuthManager
): ViewModel() {

    private val _uiState = MutableStateFlow(UserUiState())
    val uiState: StateFlow<UserUiState> = _uiState.asStateFlow()

    private val _userEvent = MutableStateFlow<UserEvent>(UserEvent.Idle)
    val userEvent: StateFlow<UserEvent> = _userEvent.asStateFlow()


    init {
        _uiState.update { currentState ->
            currentState.copy(
                onEmpresaChange = { empresa ->
                    _uiState.value = _uiState.value.copy(empresa = empresa)
                },
                onUserNameChange = { username ->
                    _uiState.value = _uiState.value.copy(username = username)
                },
                onEmailChange = { email ->
                    _uiState.value = _uiState.value.copy(email = email)
                },
                onPasswordChange = { password ->
                    _uiState.value = _uiState.value.copy(password = password)
                },
                onPasswordConfirmChange = { passwordConfirm ->
                    _uiState.value = _uiState.value.copy(passwordConfirm = passwordConfirm)
                },
                onPasswordCurrentChange = { passwordCurrent ->
                    _uiState.value = _uiState.value.copy(passwordCurrent = passwordCurrent)
                }
            )
        }
    }

    fun updatePerfil() {
        viewModelScope.launch {
            try {
                val currentUser =
                    authManager.getCurrentUser() ?: throw Exception("Usuário não autenticado")
                authManager.updateUser(
                    currentUser.uid,
                    _uiState.value.empresa,
                    _uiState.value.username,
                    _uiState.value.email,
                ).getOrThrow()
                _userEvent.value = UserEvent.Success(_uiState.value)
                _uiState.update { it.copy(username = "", email = "", empresa = "") }
            } catch (e: Exception) {
                _userEvent.value = UserEvent.Error(e.message ?: "Erro ao atualizar perfil")
            }
        }
    }


    fun updateSenha() {
        viewModelScope.launch{
            val currentPassword = _uiState.value.passwordCurrent
            val newPassword = _uiState.value.password
            val confirmPassword = _uiState.value.passwordConfirm

            if (newPassword != confirmPassword) {
                _userEvent.value = UserEvent.Error("As senhas não coincidem")
                return@launch
            }

            try {
                authManager.updatePassword(currentPassword, newPassword).getOrThrow()
                _userEvent.value = UserEvent.Success(_uiState.value)
                _uiState.update { it.copy(password = "", passwordConfirm = "") }
            } catch (e: Exception) {
                _userEvent.value = UserEvent.Error(e.message ?: "Erro ao atualizar senha")
            }
        }
    }

    fun verifyPassword(onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            val result = authManager.verifyPassword(_uiState.value.passwordCurrent)
            _userEvent.value = UserEvent.Loading
            if (result.isSuccess) {
                _userEvent.value = UserEvent.Success(_uiState.value)
                onSuccess() // abre o BottomSheet
                _uiState.update { it.copy(passwordCurrent = "") }
            } else {
                _userEvent.value = UserEvent.Error("Senha incorreta")
            }
        }
    }

}