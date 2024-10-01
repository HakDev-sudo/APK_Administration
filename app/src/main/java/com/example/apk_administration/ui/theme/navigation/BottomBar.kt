package com.example.apk_administration.ui.theme.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.navigation.NavHostController

@Composable
fun BottomBarItem(
    icon: @Composable () -> Unit,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        IconButton(onClick = onClick, modifier = Modifier.size(48.dp)) {
            icon()
        }
        Text(
            text = label,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun CustomBottomBar(navController: NavHostController) {
    Surface(
        tonalElevation = 8.dp,
        shadowElevation = 8.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomBarItem(
                icon = { Icon(Icons.Filled.Menu, contentDescription = "Menu") },
                label = "Menu",
                onClick = { /* TODO: Implementar acción */ }
            )
            BottomBarItem(
                icon = { Icon(Icons.Filled.Nfc, contentDescription = "NFC") },
                label = "NFC",
                onClick = { navController.navigate("nfc") }
            )
            BottomBarItem(
                icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
                label = "Home",
                onClick = { navController.navigate("home") }
            )
            BottomBarItem(
                icon = { Icon(Icons.Filled.Settings, contentDescription = "Configuración") },
                label = "Config",
                onClick = { navController.navigate("setting") }
            )
            BottomBarItem(
                icon = { Icon(Icons.Filled.Person, contentDescription = "Perfil") },
                label = "Perfil",
                onClick = { /* TODO: Implementar acción */ }
            )
        }
    }
}


