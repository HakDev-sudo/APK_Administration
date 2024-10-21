package com.example.apk_administration.ui.theme.Category

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun ContenidoCategoryEditar(
    navController: NavHostController,
    servicio: CategoryApiService,
    categoryId: Int = 0
) {
    var id by remember { mutableStateOf(categoryId) }
    var name by remember { mutableStateOf("") }
    var img by remember { mutableStateOf<String?>(null) } // img puede ser null por defecto
    var grabar by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(id != 0) }
    var isProcessing by remember { mutableStateOf(false) } // Estado para bloquear el botón mientras se procesa la solicitud

    // Cargar los datos de la categoría si es edición
    LaunchedEffect(id) {
        if (id != 0) {
            val response = servicio.selectCategory(id.toString())
            if (response.isSuccessful) {
                val objCategory = response.body()
                objCategory?.let {
                    name = it.name
                    img = it.img // img puede ser null
                }
            }
            isLoading = false // Ya se cargaron los datos
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Mostrar los TextField solo cuando no está cargando
        if (!isLoading) {
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre de la Categoría") },
                singleLine = true
            )
            TextField(
                value = img ?: "", // Muestra un campo vacío si img es null
                onValueChange = { img = if (it.isEmpty()) null else it }, // Asigna null si el campo está vacío
                label = { Text("URL de Imagen (opcional)") },
                singleLine = true
            )
            Button(
                onClick = { grabar = true }, // Al hacer clic, cambiar grabar a true
                modifier = Modifier.padding(top = 16.dp),
                enabled = !isProcessing // Deshabilitar mientras está procesando
            ) {
                Text("Guardar", fontSize = 16.sp)
            }
        } else {
            // Mostrar un indicador de carga mientras se obtienen los datos
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }
    }

    // Guardar la categoría (inserción o actualización)
    if (grabar) {
        // Asegúrate de que el modelo se crea correctamente
        val objCategory = CategoryModel(id, name, img)

        // Bloquear el botón durante el procesamiento
        isProcessing = true

        LaunchedEffect(grabar) {
            val response = if (id == 0) {
                servicio.insertCategory(objCategory.copy(id = 0)) // id se envía como 0 para nuevas inserciones
            } else {
                servicio.updateCategory(id.toString(), objCategory)
            }

            if (response.isSuccessful) {
                // Redirigir a la pantalla de categorías después de guardar
                navController.navigate("categorias") {
                    popUpTo("categorias") { inclusive = true }
                }
            } else {
                // Aquí puedes manejar el error
            }

            // Reiniciar el estado de grabar e isProcessing
            grabar = false
            isProcessing = false
        }
    }
}