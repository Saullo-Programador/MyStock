package com.example.meustock.ui.viewModel

import app.cash.turbine.test
import com.example.meustock.authentication.FirebaseAuthManager
import com.example.meustock.ui.states.AuthUiState
import com.google.firebase.auth.FirebaseUser
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    private lateinit var authManager: FirebaseAuthManager
    private lateinit var viewModel: AuthViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        authManager = mockk()
        viewModel = AuthViewModel(authManager)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createFakeUser() = mockk<FirebaseUser>()

    @Test
    fun `signIn should set loading and success state when credentials are valid`() = runTest {
        val email = "test@example.com"
        val password = "password123"
        val user = createFakeUser()

        viewModel.signInStateUpdate(email, password)
        coEvery { authManager.signIn(email, password) } returns Result.success(user)

        viewModel.uiState.test {
            // The initial item is the default AuthUiState()
            awaitItem()

            viewModel.signIn()

            // Depending on the dispatcher, we might see:
            // 1. Loading state
            // 2. Success state
            // Since we use UnconfinedTestDispatcher, it might jump to Success.
            // Let's check for the success state.
            val state = awaitItem()
            assertEquals(AuthUiState(user = user), state)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `signIn should set error state when fields are blank`() = runTest {
        viewModel.signInStateUpdate("", "")

        viewModel.uiState.test {
            awaitItem() // Consume initial state

            viewModel.signIn()
            val state = awaitItem()
            assertEquals("Preencha todos os campos", state.error)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `signIn should set error state when authManager fails`() = runTest {
        val email = "test@example.com"
        val password = "password123"
        viewModel.signInStateUpdate(email, password)

        coEvery { authManager.signIn(email, password) } returns Result.failure(Exception("Invalid credentials"))

        viewModel.uiState.test {
            awaitItem() // Consume initial state

            viewModel.signIn()
            val state = awaitItem()
            assertEquals("Invalid credentials", state.error)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `signUp should set error when passwords do not match`() = runTest {
        viewModel.signUpStateUpdate(
            empresaName = "Company",
            username = "user",
            email = "test@example.com",
            password = "password123",
            passwordConfirmation = "different_password",
            check = true
        )

        viewModel.uiState.test {
            awaitItem() // Consume initial state

            viewModel.signUp()
            val state = awaitItem()
            assertEquals("As senhas não coincidem", state.error)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `signUp should set error when email is invalid`() = runTest {
        viewModel.signUpStateUpdate(
            empresaName = "Company",
            username = "user",
            email = "invalidemail",
            password = "password123",
            passwordConfirmation = "password123",
            check = true
        )

        viewModel.uiState.test {
            awaitItem() // Consume initial state

            viewModel.signUp()
            val state = awaitItem()
            assertEquals("O email deve conter um @", state.error)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `signUp should succeed when all fields are valid`() = runTest {
        val email = "test@example.com"
        val user = createFakeUser()
        viewModel.signUpStateUpdate(
            empresaName = "Company",
            username = "user",
            email = email,
            password = "password123",
            passwordConfirmation = "password123",
            check = true
        )

        coEvery {
            authManager.signUp(any(), any(), any(), any())
        } returns Result.success(user)

        viewModel.uiState.test {
            awaitItem() // Consume initial state

            viewModel.signUp()
            val state = awaitItem()
            assertEquals(AuthUiState(user = user), state)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
// Removed duplicate extension functions that were shadowing ViewModel members
