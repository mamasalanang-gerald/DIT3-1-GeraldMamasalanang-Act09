package com.example.advanceduinavapp.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.advanceduinavapp.models.AuthState
import com.example.advanceduinavapp.models.User
import com.example.advanceduinavapp.repositories.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertIs

class AuthViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var authRepository: AuthRepository

    private lateinit var authViewModel: AuthViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        whenever(authRepository.getCurrentUser()).thenReturn(null)
        authViewModel = AuthViewModel(authRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testInitialStateIsUnauthenticated() {
        val state = authViewModel.authState.value
        assertIs<AuthState.Unauthenticated>(state)
    }

    @Test
    fun testInitialStateIsSuccessWhenUserExists() {
        val user = User(uid = "test-uid", email = "test@example.com")
        whenever(authRepository.getCurrentUser()).thenReturn(user)
        
        val viewModel = AuthViewModel(authRepository)
        val state = viewModel.authState.value
        
        assertIs<AuthState.Success>(state)
        assertEquals(user, (state as AuthState.Success).user)
    }
}
