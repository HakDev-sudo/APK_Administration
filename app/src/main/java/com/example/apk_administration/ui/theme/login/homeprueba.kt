package com.example.apk_administration.ui.theme.login

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.wear.compose.material3.Button
import com.example.apk_administration.ui.theme.navigation.AlmacenApp

@Composable
fun HomeScreen(authviewModel: AuthViewModel, onLogout: () -> Unit) {
    val user = authviewModel.authState.observeAsState().value

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Welcome, ${user?.email ?: "User"}!")
        Button(
            onClick = { authviewModel.signOut(); onLogout() },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Logout")
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(authviewModel: AuthViewModel) {
    val authnavController = rememberNavController()

    NavHost(navController = authnavController, startDestination = "login") {
        composable("login") {
            LoginStructure(
                navController = authnavController,
                authViewModel = authviewModel,
                modifier = Modifier
            )
        }
        composable("register") {
            RegisterScreen(
                viewModel = authviewModel,
                onRegisterSuccess = { authnavController.navigate("home") { popUpTo("login") { inclusive = true } } }
            )
        }
        composable("home") {
            AlmacenApp()
        }
    }
}
