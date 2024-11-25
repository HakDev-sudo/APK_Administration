package com.example.apk_administration.ui.theme.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Inventory
import androidx.compose.material.icons.outlined.ListAlt
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

data class DrawerItem(val title: String, val route: String, val icon: @Composable () -> Unit)

@Composable
fun DrawerContent(navController: NavHostController,drawerState: DrawerState,
                  scope: CoroutineScope
) {
    val items = listOf(
        DrawerItem("Inicio", "home") { Icon(Icons.Filled.Home, contentDescription = "Inicio") },
        DrawerItem("Perfil", "perfil") { Icon(Icons.Filled.Person, contentDescription = "Perfil") },
        DrawerItem("Productos", "admProducts") { Icon(Icons.Filled.Inventory, contentDescription = "Productos") },
        DrawerItem("Usuarios", "admUsers") { Icon(Icons.Filled.People, contentDescription = "Usuarios") },
        DrawerItem("Registros", "registros") { Icon(Icons.Filled.ListAlt, contentDescription = "Registros") },
        DrawerItem("Configuración", "setting") { Icon(Icons.Filled.Settings, contentDescription = "Configuración") },

    )

    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    ModalDrawerSheet(
        modifier = Modifier.width(300.dp)  // Fixed width, not percentage-based
    ) {
        Text(
            text = "Menú",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(16.dp)
        )

        items.forEach { item ->
            NavigationDrawerItem(
                label = { Text(item.title) },
                selected = currentRoute == item.route,
                icon = { item.icon() },
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                    // Close the drawer after navigation
                    scope.launch { drawerState.close() }
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

