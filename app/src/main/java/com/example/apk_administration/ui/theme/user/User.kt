package com.example.apk_administration.ui.theme.user

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.apk_administration.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun PerfilScreen(padding: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .verticalScroll(rememberScrollState())
    ) {
        // Top Section: Profile Picture, Name, Role
        ProfileHeaderSection()

        // Editable Personal Information Section
        EditableUserInfoSection()

        // Work Information Section
        EditableWorkInfoSection()

        // Sensitive Information (Non-Editable)
        SensitiveInfoSection()

        // Change Password & Security
        SecuritySettingsSection()
    }
}

@Composable
fun ProfileHeaderSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            Image(
                painter = painterResource(id = R.drawable.icon_perfil),
                contentDescription = "Imagen de perfil",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
            )
            IconButton(
                onClick = { /* Cambiar imagen de perfil */ },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(40.dp)
                    .background(MaterialTheme.colorScheme.surface, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Editar imagen",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Nombre del Usuario",
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = "Administrador",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun EditableUserInfoSection() {
    var nombre by remember { mutableStateOf("John Doe") }
    var email by remember { mutableStateOf("john.doe@example.com") }
    var telefono by remember { mutableStateOf("123-456-789") }
    var departamento by remember { mutableStateOf("Almacén Principal") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Información Personal",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre Completo") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo Electrónico") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = telefono,
            onValueChange = { telefono = it },
            label = { Text("Número de Teléfono") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = departamento,
            onValueChange = { departamento = it },
            label = { Text("Departamento/Almacén Asignado") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
//
@Composable
fun EditableWorkInfoSection() {
    var horario by remember { mutableStateOf("Turno: 8 AM - 5 PM") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Información de Trabajo",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = "Supervisor de Almacén",
            onValueChange = {},
            label = { Text("Título de Trabajo") },
            modifier = Modifier.fillMaxWidth(),
            enabled = false,
            readOnly = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = horario,
            onValueChange = { horario = it },
            label = { Text("Horario de Trabajo") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = "Permisos: Gestión Completa",
            onValueChange = {},
            label = { Text("Permisos") },
            modifier = Modifier.fillMaxWidth(),
            enabled = false,
            readOnly = true
        )
    }
}

@Composable
fun SensitiveInfoSection() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Información Sensible",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        ListItem(
            headlineContent = { Text("ID de Empleado: 123456") },
            leadingContent = {
                Icon(
                    Icons.Filled.Lock,
                    contentDescription = "ID de empleado",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        )
        ListItem(
            headlineContent = { Text("Último Inicio de Sesión: 01/10/2024 08:30 AM") },
            leadingContent = {
                Icon(
                    Icons.Filled.AccessTime,
                    contentDescription = "Último inicio de sesión",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        )
    }
}

@Composable
fun SecuritySettingsSection() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Seguridad",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        ListItem(
            headlineContent = { Text("Cambiar Contraseña") },
            leadingContent = {
                Icon(
                    Icons.Filled.Security,
                    contentDescription = "Cambiar contraseña",
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            modifier = Modifier.clickable { /* Cambiar contraseña */ }
        )
        ListItem(
            headlineContent = { Text("Método de Autenticación: 2FA Habilitado") },
            leadingContent = {
                Icon(
                    Icons.Filled.VerifiedUser,
                    contentDescription = "Autenticación",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UserProfileScreenPreview() {
    PerfilScreen(padding = PaddingValues(0.dp))
}

