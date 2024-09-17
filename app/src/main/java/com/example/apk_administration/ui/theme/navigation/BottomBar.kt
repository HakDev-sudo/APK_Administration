package com.example.apk_administration.ui.theme.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Dashboard
import com.google.androidgamesdk.gametextinput.Settings

@Composable
fun CustomBottomBar() {
    BottomAppBar {
        IconButton(
            onClick = { print("Build") },
            modifier = Modifier.weight(1f)
        ) {
            Icon(Icons.Filled.Inventory, contentDescription = "Build description")
        }
        IconButton(
            onClick = { print("Menu") },
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                Icons.Filled.Dashboard,
                contentDescription = "Menu description",
            )
        }
        IconButton(
            onClick = { print("Build") },
            modifier = Modifier.weight(1f)
        ) {
            Icon(Icons.Filled.Home, contentDescription = "Build description")
        }
        IconButton(
            onClick = { print("Favorite") },
            modifier = Modifier.weight(1f)) {
            Icon(
                Icons.Filled.Notifications,
                contentDescription = "Favorite description",
            )
        }
        IconButton(
            onClick = { print("Delete") },
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                Icons.Filled.Settings,
                contentDescription = "Delete description",
            )
        }
    }
}
