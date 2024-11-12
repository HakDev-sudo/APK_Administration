package com.example.apk_administration.ui.theme.products

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.apk_administration.ui.theme.Category.CategoryModel
import com.example.apk_administration.ui.theme.NFC.NfcModel
import android.content.Context
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.KeyboardType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.io.InputStream
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private fun convertUriToBase64(uri: Uri, context: Context): String? {
    return try {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val bytes: ByteArray? = inputStream?.readBytes()
        // Si bytes es nulo, retorna nulo
        bytes ?: return null
        // Codifica bytes a Base64
        Base64.encodeToString(bytes, Base64.NO_WRAP)
    } catch (e: Exception) {
        Log.e("ImageConversion", "Error al convertir URI a base64: ${e.message}")
        null
    }
}

@Composable
fun AddOrEditProductScreen(
    navController: NavHostController,
    servicio: ProductoApiService,
    productoId: Int = 0
) {
    val context = LocalContext.current

    // Definir los estados
    var name by remember { mutableStateOf("") }
    var imgUri by remember { mutableStateOf<Uri?>(null) }
    var price by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var categoryId by remember { mutableStateOf("") }
    var categories by remember { mutableStateOf<List<CategoryModel>>(emptyList()) }
    var isSaveRequested by remember { mutableStateOf(false) }
    var isSaveSuccessful by remember { mutableStateOf(false) } // Nuevo estado para navegación

    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imgUri = uri
    }

    LaunchedEffect(Unit) {
        categories = servicio.selectCategories()
        if (productoId != 0) {
            val producto = servicio.selectProducto(productoId)
            producto.body()?.let {
                name = it.name
                price = it.price.toString()
                description = it.description
                categoryId = it.category.toString()
                imgUri = it.img?.let { uriString -> Uri.parse(uriString) }
            }
        }
    }

    // Ejecuta la lógica de guardado solo cuando `isSaveRequested` es `true`
    LaunchedEffect(isSaveRequested) {
        if (isSaveRequested) {
            try {
                val imageBase64 = imgUri?.let { convertUriToBase64(it, context) }
                val newProduct = ProductModel(
                    id = productoId,
                    name = name,
                    img = imageBase64,
                    price = price.toDoubleOrNull() ?: 0.0,
                    description = description,
                    category = categoryId.toIntOrNull() ?: 0
                )

                val response = if (productoId == 0) {
                    servicio.insertProducto(newProduct)
                } else {
                    servicio.updateProducto(productoId, newProduct)
                }

                if (response.isSuccessful) {
                    isSaveRequested = false // Evita re-renderizados innecesarios
                    isSaveSuccessful = true // Actualiza el estado de éxito
                } else {
                    Log.e("AddOrEditProductScreen", "Error en la respuesta de guardado.")
                    isSaveRequested = false // Reinicia en caso de error
                }
            } catch (e: Exception) {
                Log.e("AddOrEditProductScreen", "Error al guardar o actualizar el producto: ${e.message}")
                isSaveRequested = false // Reinicia en caso de error
            }
            navController.navigate("admProducts")
        }

    }



    // Layout principal
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Campo de nombre
        item {
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre del Producto") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Campo de precio
        item {
            TextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Precio") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Campo de descripción
        item {
            TextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Selección de categoría
        item {
            var isCategoryMenuExpanded by remember { mutableStateOf(false) }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isCategoryMenuExpanded = !isCategoryMenuExpanded }
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val selectedCategory = categories.find { it.id.toString() == categoryId }
                    Text(selectedCategory?.name ?: "Seleccionar categoría")
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Abrir menú")
                }
                DropdownMenu(
                    expanded = isCategoryMenuExpanded,
                    onDismissRequest = { isCategoryMenuExpanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    categories.forEach { category ->
                        DropdownMenuItem(
                            onClick = {
                                categoryId = category.id.toString()
                                isCategoryMenuExpanded = false
                            },
                            modifier = Modifier.fillMaxWidth(),
                            text = { Text(category.name) }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Botón para subir imagen
        item {
            Button(onClick = { imageLauncher.launch("image/*") }) {
                Text("Seleccionar Imagen")
            }
        }

        // Botón Guardar/Actualizar
        item {
            Button(
                onClick = {
                    // Validar los campos y solicitar guardado
                    if (name.isNotEmpty() && price.isNotEmpty() && categoryId.isNotEmpty()) {
                        isSaveRequested = true
                    } else {
                        Log.e("AddOrEditProductScreen", "Campos obligatorios faltantes.")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (productoId == 0) "Guardar Producto" else "Actualizar Producto")
            }
        }
    }
}

