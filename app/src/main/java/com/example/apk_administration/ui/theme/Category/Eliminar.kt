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
    var error by remember { mutableStateOf<String?>(null) } // Para manejar errores

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
                    navController.navigate("categorias") // Navegar de vuelta si se cancela
                }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Eliminación de la categoría
    if (borrar) {
        LaunchedEffect(Unit) {
            try {
                val response = servicio.deleteCategory(categoryId.toString())
                if (response.isSuccessful) {
                    navController.navigate("categorias") {
                        popUpTo("categorias") { inclusive = true } // Regresar a la lista de categorías
                    }
                } else {
                    error = "Error al eliminar la categoría"
                }
            } catch (e: Exception) {
                error = "Error: ${e.localizedMessage}"
            } finally {
                borrar = false
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