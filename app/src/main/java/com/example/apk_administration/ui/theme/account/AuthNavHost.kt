package com.example.apk_administration.ui.theme.account

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.apk_administration.ui.theme.login.LoginStructre

@Composable
fun AuthNavHost(navController: NavHostController = rememberNavController(),
                onLoginSuccess: () -> Unit) {
    NavHost(navController = navController, startDestination = "login") {
        // Pantalla de login
        composable("login") {
            LoginStructre(navController = navController)
        }
        // Pantalla de registro
        composable("basic_info") {
            BasicInfoScreen(
                onNextClick = { name, lastname, year, email, phone, profileImageUri, password ->
                // Aquí se navega a la siguiente pantalla o se maneja la lógica
                navController.navigate("") // Reemplaza "next_screen" con el destino correcto
            },
                navController = navController, padding = PaddingValues())
        }


    }
}
