package com.example.apk_administration.ui.theme.products

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.rounded.FilterAltOff
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import kotlinx.coroutines.launch

// Barra superior con botones de agregar y filtro
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductManagementTopBar(navController: NavHostController, onFilterClick: () -> Unit) {
    var showFilterSheet by remember { mutableStateOf(false) }
    TopAppBar(
        title = { Text("Administrar Productos", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
        actions = {
            IconButton(onClick = { navController.navigate("AddProduct") }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Producto")
            }
            IconButton(onClick = { onFilterClick() }) {
                Icon(Icons.Default.FilterList, contentDescription = "Filtrar Productos")
            }
        }
    )
}

// Componente para mostrar cada producto en una tarjeta
@Composable
fun ProductCard(
    product: ProductModel,
    navController: NavHostController,
    categoryName: String, // Nombre de la categoría

) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        // Navega a la pantalla de detalles del producto al presionar prolongadamente
                        navController.navigate("productoVer/${product.id}")
                    }
                )
            }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen circular al inicio
            Box(
                modifier = Modifier
                    .size(60.dp) // Tamaño del círculo
                    .clip(CircleShape) // Forma circular
                    .background(Color.LightGray), // Color predeterminado si no hay imagen
                contentAlignment = Alignment.Center
            ) {
                if (product.img.isNullOrEmpty()) {
                    // Mostramos un icono o texto si no hay imagen
                    Icon(
                        imageVector = Icons.Default.Image, // Icono por defecto
                        contentDescription = "Sin Imagen",
                        tint = Color.White,
                        modifier = Modifier.size(30.dp) // Tamaño del icono
                    )
                } else {
                    // Cargamos la imagen desde la URL
                    AsyncImage(
                        model = product.img, // URL de la imagen
                        contentDescription = "Imagen del Producto",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop // Escalado de la imagen
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = product.name, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Cantidad: ${product.stock ?: "N/A"}", fontSize = 14.sp, color = if (product.stock ?: 0 > 0) Color.Gray else Color.Red)
                Text(text = "Precio: $${product.price}", fontSize = 14.sp, color = Color.Gray)
                Text(text = "Categoría: $categoryName", fontSize = 14.sp, color = Color.Gray)


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
fun ProductList(
    products: List<ProductModel>,
    navController: NavHostController,
    categories: Map<Int, String>, // Mapear categoría ID a nombre
    nfcs: Map<Int, String> // Mapear NFC ID a etiqueta
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(products) { product ->
            val categoryName = categories[product.category] ?: "Sin categoría"

            ProductCard(
                product = product,
                navController = navController,
                categoryName = categoryName

            )
        }
    }
}

// Pantalla completa de administración de productos
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductManagementScreen(
    servicio: ProductoApiService,
    navController: NavHostController
) {
    var productos by remember { mutableStateOf(emptyList<ProductModel>()) }
    var categorias by remember { mutableStateOf(mapOf<Int, String>()) }
    var nfcs by remember { mutableStateOf(mapOf<Int, String>()) }
    var selectedCategory by remember { mutableStateOf<Int?>(null) }
    var showFilterSheet by remember { mutableStateOf(false)}
    val scope = rememberCoroutineScope()
    var allProductos by remember { mutableStateOf(emptyList<ProductModel>()) }
    LaunchedEffect(Unit) {
        allProductos = servicio.selectProductos()
        productos = allProductos

        // Cargar categorías y NFCs
        categorias = servicio.selectCategories().associate { it.id to it.name }

    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Barra superior
        ProductManagementTopBar(navController = navController,onFilterClick = { showFilterSheet = true })
        // Mostrar el filtro en un ModalBottomSheet
        if (showFilterSheet) {
            ModalBottomSheet(
                onDismissRequest = { showFilterSheet = false }
            ) {
                CategoryFilterSheet(
                    categories = categorias,
                    selectedCategory = selectedCategory,
                    onCategorySelected = { selectedCategory = it },
                    onApplyFilter = {
                        scope.launch {
                            showFilterSheet = false
                            productos = if (selectedCategory != null) {
                                // Filtrar productos por la categoría seleccionada
                                allProductos.filter { it.category == selectedCategory }
                            } else {
                                // Resetear a todos los productos si no hay categoría seleccionada
                                allProductos
                            }
                        }
                    }
                )
                Spacer(modifier = Modifier.height(2.dp))

                // Botón "Quitar Filtros"
                Button(
                    onClick = {
                        selectedCategory = null // Restablecer la categoría seleccionada
                        productos = allProductos // Mostrar todos los productos
                        showFilterSheet = false // Cerrar el modal
                    },
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(bottom = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.FilterAltOff,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Quitar Filtros",
                        style = MaterialTheme.typography.labelLarge
                    )
                }

            }
        }

        // Lista de productos
        ProductList(
            products = productos,
            navController = navController,
            categories = categorias,
            nfcs = nfcs
        )
    }
}