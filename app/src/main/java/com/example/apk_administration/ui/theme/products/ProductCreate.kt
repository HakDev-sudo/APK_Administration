package com.example.apk_administration.ui.theme.products

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay

@Composable
fun ProductForm(
    navController: NavHostController, servicio: ProductoApiServiceC, productoId: Int = 0
) {

    var id by remember { mutableStateOf(productoId) }
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var categoryId by remember { mutableStateOf("") } // Usamos solo el ID de la categoría
    var nfcTag by remember { mutableStateOf("") } // Cambiamos para manejar el ID NFC si está presente
    var description by remember { mutableStateOf("Descripción del Producto") }
    var stock by remember { mutableStateOf("") } // Nuevo campo para el stock
    var imgUrl by remember { mutableStateOf("") } // Campo para la URL de la imagen (opcional)
    var grabar by remember { mutableStateOf(false) }

    // Cargar los datos si estamos editando un producto existente
    if (id != 0) {
        LaunchedEffect(Unit) {
            val objProducto = servicio.selectProducto(id.toString())
            delay(100)
            objProducto.body()?.let {
                name = it.name
                price = it.price.toString()
                categoryId = it.category.id.toString() // Solo el ID de la categoría
                nfcTag = it.idNFC?.id_tag ?: "" // ID NFC o vacío si es nulo
                description = it.description
                stock = it.stock.toString() // Asignar el valor de stock
                imgUrl = it.img ?: ""
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre del Producto") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Precio") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = categoryId,
            onValueChange = { categoryId = it },
            label = { Text("ID de Categoría") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = nfcTag,
            onValueChange = { nfcTag = it },
            label = { Text("ID Tag NFC (opcional)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = stock, // Campo de stock
            onValueChange = { stock = it },
            label = { Text("Stock") }, // Nuevo campo para el stock
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number), // Aseguramos que solo números sean ingresados
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = imgUrl,
            onValueChange = { imgUrl = it },
            label = { Text("URL de la Imagen (opcional)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                grabar = true
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar", fontSize = 16.sp)
        }
    }

    // Manejar la acción de guardar los datos del producto
    if (grabar) {
        val idNfc = if (nfcTag.isNotEmpty()) nfcTag.toIntOrNull() else null
        val nuevoProducto = ProductoModelPost(
            name = name,
            img = if (imgUrl.isNotEmpty()) imgUrl else null, // Guardar la imagen solo si hay una URL
            price = price.toDoubleOrNull() ?: 0.0,
            description = description,
            categoryId = categoryId.toIntOrNull() ?: 0, // Validar que el ID de categoría sea un número
            stock = stock.toIntOrNull() ?: 0, // Convertir el valor de stock a entero
            idNfc = idNfc // Enviar null si no hay NFC
        )

        LaunchedEffect(Unit) {
            if (id == 0) {
                servicio.insertProducto(nuevoProducto)
            } else {
                servicio.updateProducto(id.toString(), nuevoProducto)
            }
            grabar = false // Reinicia la variable para evitar re-render
            navController.navigate("admProducts") // Redirige después de guardar
        }
    }
}
