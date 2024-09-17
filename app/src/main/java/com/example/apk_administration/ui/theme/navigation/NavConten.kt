package com.example.apk_administration.ui.theme.navigation

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.apk_administration.ui.theme.home.HomeScreen
import com.example.apk_administration.ui.theme.login.LoginStructre

@Composable
fun NavigationHost(navController: NavHostController, padding: PaddingValues) {
    NavHost(navController = navController, startDestination = "home") {
        // Pantalla de login

        // Pantalla de Home
        composable("home") {
            HomeScreen(padding)

        }
    }
}
