package com.example.apk_administration.ui.theme.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(authviewModel: AuthViewModel, onLoginSuccess: () -> Unit, onRegisterNavigate: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val isLoading by authviewModel.isLoading.observeAsState(false)

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )
        Button(
            onClick = { if (email.isNotEmpty() && password.isNotEmpty()) authviewModel.signIn(email, password) },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Login")
        }
        TextButton(onClick = onRegisterNavigate, modifier = Modifier.padding(top = 8.dp)) {
            Text("Don't have an account? Register")
        }
        if (isLoading) CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
    }

    LaunchedEffect(authviewModel.authState.value) {
        if (authviewModel.authState.value != null) onLoginSuccess()
    }
}

