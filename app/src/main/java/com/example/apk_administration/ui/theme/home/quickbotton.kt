package com.example.apk_administration.ui.theme.home

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
import androidx.navigation.NavController

@Composable
fun ActionButton(
    icon: @Composable () -> Unit,
    label: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .size(width = 110.dp, height = 110.dp)
            .padding(4.dp),
        onClick = onClick,
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 4.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            icon()
            Text(
                text = label,
                fontSize = 12.sp,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(4.dp)
            )
        }
    }
}

@Composable
fun QuickActionButtons(navController: NavController) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        ActionButton(
            icon = {
                Icon(
                    imageVector = Icons.Filled.Assessment,
                    contentDescription = "Visualizar registro",
                    modifier = Modifier
                        .padding(8.dp)
                        .size(24.dp)
                )
            },
            label = "Visualizar Registro",
            onClick = { navController.navigate("registros") }
        )

        ActionButton(
            icon = {
                Icon(
                    imageVector = Icons.Filled.Inventory,
                    contentDescription = "Administrar productos",
                    modifier = Modifier
                        .padding(8.dp)
                        .size(24.dp)
                )
            },
            label = "Administrar Productos",
            onClick = { navController.navigate("admProducts")  }
        )
    }
}

