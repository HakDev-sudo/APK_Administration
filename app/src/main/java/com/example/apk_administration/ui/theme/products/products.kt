package com.example.apk_administration.ui.theme.products

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController

// Barra superior con botones de agregar y filtro
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductManagementTopBar(navController: NavHostController) {
    TopAppBar(
        title = { Text("Administrar Productos", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
        actions = {
            IconButton(onClick = { navController.navigate("product_form") }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Producto")
            }
            IconButton(onClick = { /* Acción para filtrar productos */ }) {
                Icon(Icons.Default.FilterList, contentDescription = "Filtrar Productos")
            }
        }
    )
}

// Componente para mostrar cada producto en una tarjeta
@Composable
fun ProductCard(product: Product, onDeleteClick: () -> Unit) {
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
                Text(text = product.name, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Cantidad: ${product.quantity}", fontSize = 14.sp, color = Color.Gray)
                Text(text = "Precio: $${product.price}", fontSize = 14.sp, color = Color.Gray)
            }

            // Botón de eliminar producto (Icono de basura)
            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar Producto")
            }
        }
    }
}

// Lista de productos usando LazyColumn
@Composable
fun ProductList(products: List<Product>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(products) { product ->
            ProductCard(product = product, onDeleteClick = { /* Acción de eliminar producto */ })
        }
    }
}

// Pantalla completa de administración de productos
@Composable
fun ProductManagementScreen(products: List<Product>,padding: PaddingValues, navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize().padding(padding)
    ) {
        // Barra superior
        ProductManagementTopBar(navController = navController)

        // Lista de productos
        ProductList(products = products)
    }
}

// Datos ficticios de ejemplo
data class Product(val name: String, val quantity: Int, val price: Double)

@Preview(showBackground = true)
@Composable
fun PreviewProductManagementScreen() {
    val sampleProducts = listOf(
        Product(name = "Producto A", quantity = 10, price = 15.0),
        Product(name = "Producto B", quantity = 5, price = 25.0),
        Product(name = "Producto C", quantity = 12, price = 10.0)
    )
    ProductManagementScreen(products = sampleProducts,padding = PaddingValues(0.dp), navController = NavHostController(LocalContext.current))
}
