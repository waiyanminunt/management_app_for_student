package com.example.supporterunt.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.supporterunt.app.network.LoginRequest
import com.example.supporterunt.app.ui.viewmodels.AuthState
import com.example.supporterunt.app.ui.viewmodels.AuthViewModel
import com.example.supporterunt.app.utils.BiometricHelper

@Composable
fun LoginScreen(
    onLoginSuccess: (String) -> Unit,
    onNavigateToRegister: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    
    val context = LocalContext.current
    var biometricError by remember { mutableStateOf<String?>(null) }
    
    val loginState by viewModel.loginState.collectAsState()

    LaunchedEffect(loginState) {
        if (loginState is AuthState.Success) {
            viewModel.resetStates()
            val role = (loginState as AuthState.Success).role ?: "STUDENT"
            onLoginSuccess(role)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Welcome to Supporter Unt", style = MaterialTheme.typography.headlineMedium)
        
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (loginState is AuthState.Error) {
            Text(
                text = (loginState as AuthState.Error).error,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        Button(
            onClick = {
                viewModel.login(LoginRequest(email, password))
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = email.isNotBlank() && password.isNotBlank() && loginState !is AuthState.Loading
        ) {
            if (loginState is AuthState.Loading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
            } else {
                Text("Login")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (BiometricHelper.isBiometricAvailable(context)) {
            OutlinedButton(
                onClick = {
                    biometricError = null
                    val activity = context as? FragmentActivity
                    if (activity != null) {
                        BiometricHelper.showBiometricPrompt(
                            activity = activity,
                            onSuccess = { onLoginSuccess("TEACHER") }, // Assuming Admin = TEACHER routing for now
                            onError = { biometricError = it }
                        )
                    } else {
                        biometricError = "System error: Activity not found"
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login with Biometrics")
            }
        }
        
        if (biometricError != null) {
            Text(
                text = biometricError ?: "",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onNavigateToRegister) {
            Text("Don't have an account? Register")
        }
    }
}
