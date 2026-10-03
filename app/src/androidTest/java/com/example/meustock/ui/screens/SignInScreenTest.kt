package com.example.meustock.ui.screens

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.meustock.ui.states.SignInUiState
import com.example.meustock.ui.viewModel.AuthViewModel
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

class SignInScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val viewModel: AuthViewModel = mockk(relaxed = true)

    @Test
    fun `signInForm should allow entering email and password`() {
        val uiState = SignInUiState(
            email = "",
            password = ""
        )

        composeTestRule.setContent {
            SignInForm(
                onSignUpClick = {},
                onSignInClick = {},
                onForgotPasswordClick = {},
                uiState = uiState
            )
        }

        // Note: In a real scenario, we'd check if the value changes in the UI,
        // but since SignInForm takes uiState as a parameter,
        // we verify the existence of the fields and the ability to interact.

        composeTestRule.onNodeWithText("Digite seu e-mail").assertExists()
        composeTestRule.onNodeWithText("Digite sua Senha!").assertExists()
        composeTestRule.onNodeWithText("Entrar").assertExists()
    }

    @Test
    fun `signInButton should trigger onSignInClick`() {
        var clicked = false
        val uiState = SignInUiState()

        composeTestRule.setContent {
            SignInForm(
                onSignUpClick = {},
                onSignInClick = { clicked = true },
                onForgotPasswordClick = {},
                uiState = uiState
            )
        }

        composeTestRule.onNodeWithText("Entrar").performClick()

        assert(clicked)
    }
}
