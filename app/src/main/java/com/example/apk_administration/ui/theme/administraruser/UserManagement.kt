package com.example.apk_administration.ui.theme.administraruser

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Barra superior con botones de agregar y filtro para usuarios
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserManagementTopBar() {
    TopAppBar(
        title = { Text("Gestión de Usuarios", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
        actions = {
            IconButton(onClick = { /* Acción para agregar usuario */ }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Usuario")
            }
            IconButton(onClick = { /* Acción para filtrar usuarios */ }) {
                Icon(Icons.Default.FilterList, contentDescription = "Filtrar Usuarios")
            }
        }
    )
}

// Componente para mostrar cada usuario en una tarjeta
@Composable
fun UserCard(user: User, onDeleteClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = user.name, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Cargo: ${user.role}", fontSize = 14.sp, color = Color.Gray)
                Text(text = "Email: ${user.email}", fontSize = 14.sp, color = Color.Gray)
            }

            // Botón de eliminar usuario (Icono de basura)
            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar Usuario")
            }
        }
    }
}

// Lista de usuarios usando LazyColumn
@Composable
fun UserList(users: List<User>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(users) { user ->
            UserCard(user = user, onDeleteClick = { /* Acción de eliminar usuario */ })
        }
    }
}

// Pantalla completa de gestión de usuarios
@Composable
fun UserManagementScreen(users: List<User>, padding: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        // Barra superior
        UserManagementTopBar()

        // Lista de usuarios
        UserList(users = users)
    }
}

// Datos ficticios de ejemplo para usuarios
data class User(val name: String, val role: String, val email: String)

@Preview(showBackground = true)
@Composable
fun PreviewUserManagementScreen() {
    val sampleUsers = listOf(
        User(name = "Juan Pérez", role = "Administrador", email = "juan.perez@example.com"),
        User(name = "Ana Gómez", role = "Supervisor", email = "ana.gomez@example.com"),
        User(name = "Luis Martínez", role = "Operador", email = "luis.martinez@example.com")
    )
    UserManagementScreen(users = sampleUsers, padding = PaddingValues(0.dp))
}
