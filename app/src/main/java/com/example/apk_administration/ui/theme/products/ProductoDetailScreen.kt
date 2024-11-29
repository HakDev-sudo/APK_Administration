package com.example.apk_administration.ui.theme.products

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.example.apk_administration.ui.theme.NFC.NfcApiService
import com.example.apk_administration.ui.theme.NFC.NfcModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoDetailScreen(
    productId: Int,
    navController: NavController,
    servicio: ProductoApiService,
    nfcService: NfcApiService
) {
    // Estados para producto y etiquetas NFC
    var product by remember { mutableStateOf<ProductModel?>(null) }
    var nfcTags by remember { mutableStateOf<List<NfcModel>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // Cargar detalles del producto y etiquetas NFC relacionadas
    LaunchedEffect(productId) {
        try {
            val productResponse = servicio.selectProducto(productId)
            if (productResponse.isSuccessful) {
                product = productResponse.body()
                // Filtrar las etiquetas NFC relacionadas al producto
                val nfcs = nfcService.selectNfcs()
                nfcTags = nfcs.filter { it.product == productId }
            }
        } catch (e: Exception) {
            Log.e("ProductoDetailScreen", "Error: ${e.message}")
        }
        isLoading = false
    }

    if (isLoading) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator()
        }
    } else {
        product?.let { producto ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Nombre del producto
                Text(
                    text = producto.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                // Imagen del producto
                producto.img?.let { imgUrl ->
                    AsyncImage(
                        model = imgUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.Gray),
                        contentScale = ContentScale.Crop
                    )
                }

                // Descripción del producto
                Text(
                    text = producto.description,
                    style = MaterialTheme.typography.bodyMedium
                )

                // Precio y Stock
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Precio: $${producto.price}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Stock: ${producto.stock ?: 0}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                // Etiquetas NFC asociadas
                Text(
                    text = "Etiquetas NFC asociadas:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                if (nfcTags.isNotEmpty()) {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxHeight(0.5f)
                    ) {
                        items(nfcTags) { nfc ->
                            Card(
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(Color(0xFFE3F2FD)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        // Acciones al presionar la etiqueta NFC
                                        Log.d("ProductoDetailScreen", "Etiqueta NFC seleccionada: ${nfc.idTag}")
                                    }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "ID Tag: ${nfc.idTag}",
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                        Text(
                                            text = "Estado: ${nfc.status}",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                    Text(
                                        text = nfc.fechaAsignado,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }
                    }
                } else {
                    Text(
                        text = "No hay etiquetas NFC asociadas.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Red
                    )
                }

                // Botón para regresar
                Button(
                    onClick = { navController.navigateUp() },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(text = "Regresar")
                }
            }
        } ?: run {
            // Mostrar mensaje de error si el producto no existe
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Producto no encontrado.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Red
                )
            }
        }
    }
}

