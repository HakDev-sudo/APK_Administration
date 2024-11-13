package com.example.apk_administration.ui.theme.products

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter

@SuppressLint("RememberReturnType")
@Composable
fun ProductoDetailScreen(productId: Int, navController: NavController, servicio: ProductoApiService) {
    // Obtén el ViewModel si estás usando uno para el manejo de datos o llama directamente al servicio

    var product by remember { mutableStateOf<ProductModel?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    // Llama al API para obtener detalles
    LaunchedEffect(productId) {
        val response = servicio.selectProducto(productId)
        if (response.isSuccessful) {
            product = response.body()
        }
        isLoading = false
    }

    if (isLoading) {
        CircularProgressIndicator() // Muestra un indicador de carga mientras obtienes los datos
    } else {
        product?.let { producto ->
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = producto.name, fontSize = 24.sp, fontWeight = FontWeight.Bold)

                producto.img?.let { imgUrl ->
                    Image(
                        painter = rememberImagePainter(imgUrl),
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth().height(200.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Descripción: ${producto.description}", fontSize = 16.sp)
                Text(text = "Precio: $${producto.price}", fontSize = 16.sp)

                Text(text = "Stock: ${producto.stock}", fontSize = 16.sp)



                Button(
                    onClick = { navController.navigateUp() },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text("Regresar")
                }
            }
        } ?: Text(text = "Producto no encontrado", color = Color.Red)
    }
}
