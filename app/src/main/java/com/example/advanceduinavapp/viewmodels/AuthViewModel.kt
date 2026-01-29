package com.example.advanceduinavapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.advanceduinavapp.models.AuthState
import com.example.advanceduinavapp.models.User
import com.example.advanceduinavapp.repositories.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    init {
        checkAuthenticationState()
    }

    private fun checkAuthenticationState() {
        val currentUser = authRepository.getCurrentUser()
        _authState.value = if (currentUser != null) {
            AuthState.Success(currentUser)
        } else {
            AuthState.Unauthenticated
        }
    }

    fun login(email: String, password: String) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            val result = authRepository.login(email, password)
            _authState.value = result.fold(
                onSuccess = { user -> AuthState.Success(user) },
                onFailure = { error -> AuthState.Error(error.message ?: "Login failed") }
            )
        }
    }

    fun logout() {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            val result = authRepository.logout()
            _authState.value = result.fold(
                onSuccess = { AuthState.Unauthenticated },
                onFailure = { error -> AuthState.Error(error.message ?: "Logout failed") }
            )
        }
    }
}
