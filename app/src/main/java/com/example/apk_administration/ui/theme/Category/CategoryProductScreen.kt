package com.example.apk_administration.ui.theme.Category

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.wear.compose.material.ChipDefaults
import com.example.apk_administration.ui.theme.products.ProductCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.sp
import androidx.glance.text.FontWeight
import coil.compose.AsyncImage


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDetailsScreen(
    navController: NavHostController,
    categoryId: Int,
    viewModel: CategoryViewModel
) {
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val products by viewModel.productsByCategory.collectAsState()
    val categories by viewModel.categories.collectAsState()
    // Cargar las categorías al iniciar la pantalla
    LaunchedEffect(Unit) {
        viewModel.loadCategories()
        viewModel.loadProductsByCategory(categoryId)
    }
    // Cargar los productos cuando el ID de la categoría cambie
    LaunchedEffect(categoryId) {
        viewModel.loadProductsByCategory(categoryId)
    }
    // Obtener el nombre de la categoría seleccionada (solo si las categorías ya están cargadas)
    val categoryName = if (categories.isNotEmpty()) {
        viewModel.getCategoryName(categoryId)
    } else {
        "Cargando..."
    }

    // Contenedor general
    Box(modifier = Modifier.fillMaxSize()) {

        // TopAppBar para mostrar el nombre de la categoría
        TopAppBar(
            title = {
                Text(
                    text = "Listado de Productos de: $categoryName",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        )

        // Contenido principal debajo de la TopAppBar
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(top = 56.dp) // Ajustamos el contenido debajo de la AppBar
        ) {
            // Mostrar detalles de la categoría
            selectedCategory?.let { category ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        AsyncImage(
                            model = category.img,
                            contentDescription = "Imagen de categoría",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = category.name, fontSize = 24.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Mostrar productos de la categoría
            if (products.isEmpty()) {
                // Mostrar mensaje si no hay productos
                Text(
                    text = "No hay productos en esta categoría.",
                    fontSize = 18.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(products) { product ->
                        ProductCard(
                            product = product,
                            navController = navController,
                            categoryName = selectedCategory?.name.orEmpty()
                        )
                    }
                }
            }
        }
    }
}


