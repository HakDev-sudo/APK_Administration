package com.example.apk_administration.ui.theme.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

data class DrawerItem(val title: String, val route: String, val icon: @Composable () -> Unit)

@Composable
fun DrawerContent(navController: NavHostController) {
    val items = listOf(
        DrawerItem("Inicio", "home") { Icon(Icons.Filled.Home, contentDescription = "Inicio") },
        DrawerItem("Productos", "admProducts") { Icon(Icons.Filled.Inventory, contentDescription = "Productos") },
        DrawerItem("Usuarios", "admUsers") { Icon(Icons.Filled.People, contentDescription = "Usuarios") },
        DrawerItem("Registros", "registros") { Icon(Icons.Filled.ListAlt, contentDescription = "Registros") },
        DrawerItem("Configuración", "setting") { Icon(Icons.Filled.Settings, contentDescription = "Configuración") }
    )

    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp) // Ajuste para mejorar la apariencia
    ) {
        Text(
            text = "Menú",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp) // Espacio debajo del título
        )

        items.forEach { item ->
            NavigationDrawerItem(
                icon = { item.icon() },
                label = { Text(item.title) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }

                },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
        }
    }
}
