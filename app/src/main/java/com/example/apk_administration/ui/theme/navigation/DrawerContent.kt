package com.example.apk_administration.ui.theme.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState



@Composable
fun DrawerContent(navController: NavHostController) {
    val options = listOf("Inicio", "Productos", "Usuarios", "Registros", "Configuración")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = "Menú", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(8.dp))

        options.forEach { option ->
            TextButton(onClick = {
                when (option) {
                    "Inicio" -> navController.navigate("home")
                    "Productos" -> navController.navigate("admProducts")
                    "Usuarios" -> navController.navigate("admUsers")
                    "Registros" -> navController.navigate("registros")
                    "Configuración" -> navController.navigate("setting")
                }
            },
                colors= ButtonDefaults.textButtonColors(
                    contentColor = Color(0xFF000000), // Color del texto: negro
                    containerColor = Color.Transparent // Color de fondo: transparente
                )
            ) {
                Text(text = option, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}
