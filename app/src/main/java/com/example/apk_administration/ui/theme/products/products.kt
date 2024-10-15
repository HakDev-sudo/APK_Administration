package com.example.apk_administration.ui.theme.products

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.runtime.LaunchedEffect

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
fun ProductCard(product: ProductoModelGet, navController: NavHostController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                navController.navigate("productoVer/${product.id}")
            }
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
                Text(text = "Cantidad: ${product.stock}", fontSize = 14.sp, color = if (product.stock > 0) Color.Gray else Color.Red)
                Text(text = "Precio: $${product.price}", fontSize = 14.sp, color = Color.Gray)
                Text(text = "Categoría: ${product.category.name}", fontSize = 14.sp, color = Color.Gray)

                product.idNFC?.let {
                    Text(text = "NFC: ${it.id_tag}", fontSize = 14.sp, color = Color.Gray)
                }
            }

            // Botones de editar y eliminar producto
            Row {
                IconButton(onClick = { navController.navigate("productoEditar/${product.id}") }) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar Producto")
                }
                IconButton(onClick = { navController.navigate("productoDel/${product.id}")} ) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar Producto")
                }
            }
        }
    }
}


// Lista de productos usando LazyColumn
@Composable
fun ProductList(products: List<ProductoModelGet>, navController: NavHostController) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(products) { product ->
            ProductCard(
                product = product,
                navController = navController,
            )
        }
    }
}


// Pantalla completa de administración de productos
@Composable
fun ProductManagementScreen(servicio: ProductoApiServiceC, navController: NavHostController) {
    var productos by remember { mutableStateOf(emptyList<ProductoModelGet>()) }

    LaunchedEffect(Unit) {
        productos = servicio.selectProductos()
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Barra superior
        ProductManagementTopBar(navController = navController)

        // Lista de productos
        ProductList(
            products = productos,
            navController = navController
        )
    }
}



