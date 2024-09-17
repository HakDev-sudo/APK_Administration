package com.example.apk_administration.ui.theme.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Input
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.FloatingActionButton
import androidx.compose.ui.tooling.preview.Preview


// Acá escribiras tu
@Composable
fun HomeScreen(padding: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        // Panel de estado
        StatusCard(title = "Inventario actual", value = "1200 items", icon = Icons.Filled.Inventory)
        StatusCard(title = "Pendiente de entrada", value = "300 items", icon = Icons.Filled.Input)

        Spacer(modifier = Modifier.height(16.dp))

        // Tareas pendientes
        TaskList(tasks = listOf("Escanear 10 productos", "Actualizar inventario"))

        Spacer(modifier = Modifier.height(16.dp))

        // Botones de acción rápida
        QuickActionButtons()
    }
}

@Composable
fun StatusCard(title: String, value: String, icon: ImageVector) {
    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.padding(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = null)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = title, style = MaterialTheme.typography.bodyLarge)
                Text(text = value, style = MaterialTheme.typography.titleLarge)
            }
        }
    }
}

@Composable
fun QuickActionButtons() {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) {
        FloatingActionButton(onClick = { /* Acceso al escaneo */ }) {
            Icon(imageVector = Icons.Filled.QrCodeScanner, contentDescription = "Escanear")
        }
        FloatingActionButton(onClick = { /* Añadir nuevo producto */ }) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = "Añadir producto")
        }
        FloatingActionButton(onClick = { /* Revisión de inventario */ }) {
            Icon(imageVector = Icons.Filled.Inventory, contentDescription = "Inventario")
        }
    }
}

@Composable
fun TaskList(tasks: List<String>) {
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(tasks) { task ->
            Text(text = task, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(padding = PaddingValues(0.dp))
}