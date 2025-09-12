package com.example.meustock.ui.viewModel

import androidx.lifecycle.ViewModel
import com.example.meustock.ui.states.AuthUiState
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        val currentUser = FirebaseAuth.getInstance().currentUser
        _uiState.value = AuthUiState(user = currentUser, isLoading = false)
    }
}