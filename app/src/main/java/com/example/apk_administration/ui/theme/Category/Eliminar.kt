package com.example.apk_administration.ui.theme.Category

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController

@Composable
fun ContenidoCategoryEliminar(navController: NavHostController, servicio: CategoryApiService, categoryId: Int) {
    var showDialog by remember { mutableStateOf(true) }
    var borrar by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var isDeleted by remember { mutableStateOf(false) } // Nuevo estado para evitar reintentos

    // Diálogo de confirmación
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Confirmar Eliminación") },
            text = { Text("¿Está seguro de eliminar la Categoría?") },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        borrar = true
                    }
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                Button(onClick = {
                    showDialog = false
                    navController.navigate("categoryList") // Navegar de vuelta si se cancela
                }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Eliminación de la categoría
    if (borrar && !isDeleted) {
        LaunchedEffect(Unit) {
            try {
                val response = servicio.deleteCategory(categoryId.toString())
                if (response.isSuccessful) {
                    isDeleted = true // Marcamos como eliminado para evitar reintentos
                    navController.navigate("categoryList") {
                        popUpTo("categorias") { inclusive = true }
                    }
                } else {
                    error = "Error al eliminar la categoría"
                    navController.navigate("categoryList") {
                        popUpTo("categorias") { inclusive = true }
                    }
                }
            } catch (e: Exception) {
                error = "Error: ${e.localizedMessage}"
            } finally {
                borrar = false // Restablecer estado para evitar reintentos
            }
        }
    }

    // Mostrar mensaje de error si lo hay
    error?.let {
        AlertDialog(
            onDismissRequest = { error = null },
            title = { Text(text = "Error") },
            text = { Text(it) },
            confirmButton = {
                Button(onClick = { error = null }) {
                    Text("Aceptar")
                }
            }
        )
    }
}
