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
            Column(
                modifier = Modifier
                    .fillMaxWidth() // Alinea el contenido a la izquierda llenando el ancho disponible
                    .padding(vertical = 4.dp) // Espacio entre cada ítem
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth() // Que el ítem ocupe todo el ancho
                        .background(
                            if (currentRoute == item.route) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                            else Color.Transparent
                        )
                        .clickable {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                        .padding(8.dp) // Espaciado interno en cada ítem
                ) {
                    item.icon() // Ícono del elemento
                    Spacer(modifier = Modifier.width(16.dp)) // Separador entre el ícono y el texto
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.bodyLarge, // Tamaño de texto adecuado para ítems de lista
                        modifier = Modifier.weight(1f) // Hace que el texto ocupe todo el espacio restante
                    )
                }

                // Separador entre los elementos, sin bordes circulares
                Divider(
                    color = Color.LightGray,
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}

