package com.example.apk_administration.ui.theme.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenContent(padding: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        Text(
            text = "Configuración",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )

        // Opción: Cambiar idioma
        SettingOption(
            title = "Cambiar idioma",
            description = "Selecciona el idioma de la aplicación",
            icon = Icons.Filled.Language,
            onClick = { /* Acción para cambiar idioma */ }
        )

        Divider()

        // Opción: Notificaciones
        var notificationsEnabled by remember { mutableStateOf(true) }
        SettingSwitch(
            title = "Notificaciones",
            description = "Activar o desactivar notificaciones",
            icon = Icons.Filled.Notifications,
            checked = notificationsEnabled,
            onCheckedChange = { notificationsEnabled = it }
        )

        Divider()

        // Opción: Sincronización automática
        var syncEnabled by remember { mutableStateOf(false) }
        SettingSwitch(
            title = "Sincronización automática",
            description = "Habilitar sincronización de datos automática",
            icon = Icons.Filled.Sync,
            checked = syncEnabled,
            onCheckedChange = { syncEnabled = it }
        )

        Divider()

        // Opción: Modo oscuro
        var darkModeEnabled by remember { mutableStateOf(false) }
        SettingSwitch(
            title = "Modo oscuro",
            description = "Habilitar o deshabilitar modo oscuro",
            icon = Icons.Filled.DarkMode,
            checked = darkModeEnabled,
            onCheckedChange = { darkModeEnabled = it }
        )

        Divider()

        // Opción: Seguridad (Cambiar contraseña)
        SettingOption(
            title = "Cambiar contraseña",
            description = "Actualizar la contraseña de la cuenta",
            icon = Icons.Filled.Security,
            onClick = { /* Acción para cambiar contraseña */ }
        )

        Divider()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingOption(title: String, description: String, icon: ImageVector, onClick: () -> Unit) {
    ListItem(
        headlineContent = { Text(title) },
        supportingContent = { Text(description) },
        leadingContent = { Icon(icon, contentDescription = null) },
        modifier = Modifier.clickable(onClick = onClick)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingSwitch(
    title: String,
    description: String,
    icon: ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    ListItem(
        headlineContent = { Text(title) },
        supportingContent = { Text(description) },
        leadingContent = { Icon(icon, contentDescription = null) },
        trailingContent = {
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    MaterialTheme {
        SettingsScreenContent(padding = PaddingValues(0.dp))
    }
}
