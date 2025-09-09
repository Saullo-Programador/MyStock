package com.example.meustock.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meustock.authentication.FirebaseAuthManager
import com.example.meustock.ui.states.AuthUiState
import com.example.meustock.ui.states.ForgotPasswordUiState
import com.example.meustock.ui.states.SignInUiState
import com.example.meustock.ui.states.SignUpUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authManager: FirebaseAuthManager
): ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val _forgotPassword = MutableStateFlow(ForgotPasswordUiState())
    val forgotPassword: StateFlow<ForgotPasswordUiState> = _forgotPassword.asStateFlow()

    private val _signIn = MutableStateFlow(SignInUiState())
    val signIn: StateFlow<SignInUiState> = _signIn.asStateFlow()

    private val _signUp = MutableStateFlow(SignUpUiState())
    val signUp: StateFlow<SignUpUiState> = _signUp.asStateFlow()

    // Inicialização dos estados
    init {
        _signIn.update { currentState ->
            currentState.copy(
                onEmailChange = { email ->
                    _signIn.value = _signIn.value.copy(email = email)
                },
                onPasswordChange = { password ->
                    _signIn.value = _signIn.value.copy(password = password)
                }
            )
        }
        _signUp.update { currentState ->
            currentState.copy(
                onEmpresaNameChange = { empresaName ->
                    _signUp.value = _signUp.value.copy(empresaName = empresaName)
                },
                onUsernameChange = { username ->
                    _signUp.value = _signUp.value.copy(username = username)
                },
                onEmailChange = { email ->
                    _signUp.value = _signUp.value.copy(email = email)
                },
                onPasswordChange = { password ->
                    _signUp.value = _signUp.value.copy(password = password)
                },
                onPasswordConfirmationChange = { passwordConfirmation ->
                    _signUp.value = _signUp.value.copy(passwordConfirmation = passwordConfirmation)
                },
                onCheckChange = { check ->
                    _signUp.value = _signUp.value.copy(check = check)
                }
            )
        }
        _forgotPassword.update { currentState ->
            currentState.copy(
                onEmailChange = { email ->
                    _forgotPassword.value = _forgotPassword.value.copy(email = email)
                }
            )
        }
    }

    // SignIn
    fun signIn() {
        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)
            if (_signIn.value.email.isBlank() || _signIn.value.password.isBlank()){
                _uiState.value = AuthUiState(error = "Preencha todos os campos")
                return@launch
            }
            val result = authManager.signIn(
                email = _signIn.value.email,
                password = _signIn.value.password
            )
            _uiState.value = result.fold(
                onSuccess = { AuthUiState(user = it) },
                onFailure = { AuthUiState(error = it.message) }
            )
        }
    }

    // SignUp
    fun signUp() {
        val empresaName = _signUp.value.empresaName
        val email = _signUp.value.email
        val username = _signUp.value.username
        val password = _signUp.value.password
        val passwordConfirmation = _signUp.value.passwordConfirmation
        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)

            if (username.isBlank() || email.isBlank() || password.isBlank() || passwordConfirmation.isBlank() || empresaName.isBlank()){
                _uiState.value = AuthUiState(error = "Preencha todos os campos")
                return@launch
            } else if (password != passwordConfirmation){
                _uiState.value = AuthUiState(error = "As senhas não coincidem")
                return@launch
            } else if (!_signUp.value.check){
                _uiState.value = AuthUiState(error = "Aceite os termos de uso")
                return@launch
            } else if (!email.contains("@")){
                _uiState.value = AuthUiState(error = "O email deve conter um @")
                return@launch
            } else if (username.length < 3){
                _uiState.value = AuthUiState(error = "O nome de usuário deve ter no mínimo 3 caracteres")
                return@launch
            } else if (username.length > 20){
                _uiState.value = AuthUiState(error = "O nome de usuário deve ter no máximo 20 caracteres")
                return@launch
            } else if (password.length > 20){
                _uiState.value = AuthUiState(error = "A senha deve ter no máximo 20 caracteres")
                return@launch
            } else if (password.length < 6){
                _uiState.value = AuthUiState(error = "A senha deve ter no mínimo 6 caracteres")
                return@launch
            }
            val result = authManager.signUp(
                empresaName = empresaName,
                username = username,
                email = email,
                password = password,
            )
            _uiState.value = result.fold(
                onSuccess = { AuthUiState(user = it) },
                onFailure = { AuthUiState(error = it.message) }
            )
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authManager.logout()
            _uiState.value = AuthUiState()
        }
    }


    // Forgot Password
    fun forgotPassword() {
        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)

            val email = _forgotPassword.value.email.trim()
            if (email.isBlank()) {
                _uiState.value = AuthUiState(error = "Preencha todos os campos")
                return@launch
            }

            val result = authManager.sendPasswordResetEmail(email)

            _uiState.value = result.fold(
                onSuccess = {
                    _forgotPassword.update { it.copy(success = true) }
                    AuthUiState()
                },
                onFailure = {
                    AuthUiState(error = it.message)
                }
            )
        }
    }

    // Clear Error
    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

}