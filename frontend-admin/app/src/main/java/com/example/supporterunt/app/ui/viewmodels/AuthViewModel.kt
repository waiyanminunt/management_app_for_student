package com.example.supporterunt.app.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.supporterunt.app.network.LoginRequest
import com.example.supporterunt.app.network.NetworkModule
import com.example.supporterunt.app.network.RegisterRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val message: String, val role: String? = null) : AuthState()
    data class Error(val error: String) : AuthState()
}

class AuthViewModel : ViewModel() {
    private val api = NetworkModule.api

    private val _loginState = MutableStateFlow<AuthState>(AuthState.Idle)
    val loginState: StateFlow<AuthState> = _loginState.asStateFlow()

    private val _registerState = MutableStateFlow<AuthState>(AuthState.Idle)
    val registerState: StateFlow<AuthState> = _registerState.asStateFlow()

    fun login(request: LoginRequest) {
        viewModelScope.launch {
            _loginState.value = AuthState.Loading
            try {
                // Return type is JwtAuthResponse directly, failures will throw HTTP Exception
                val response = api.login(request)
                // FastAPI payload fields are snake_case `access_token`, `token_type`, but mapped differently in App
                _loginState.value = AuthState.Success("Login Successful", response.role)
            } catch (e: Exception) {
                e.printStackTrace()
                _loginState.value = AuthState.Error("Network error: ${e.message}")
            }
        }
    }

    fun register(request: RegisterRequest) {
        viewModelScope.launch {
            _registerState.value = AuthState.Loading
            try {
                // Return type is Map directly
                val response = api.register(request)
                _registerState.value = AuthState.Success("Registration Successful")
            } catch (e: Exception) {
                e.printStackTrace()
                _registerState.value = AuthState.Error("Network error: ${e.message}")
            }
        }
    }

    fun resetStates() {
        _loginState.value = AuthState.Idle
        _registerState.value = AuthState.Idle
    }
}
