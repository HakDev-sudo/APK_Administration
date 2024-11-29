package com.example.apk_administration.ui.theme.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.apk_administration.R
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.apk_administration.ui.theme.Category.CategoryApiService
import com.example.apk_administration.ui.theme.Category.CategoryModel

@Composable
fun CategoryCarousel(
    navController: NavHostController,
    servicio: CategoryApiService
) {
    // Estado para las categorías
    var categories by remember { mutableStateOf<List<CategoryModel>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // Llamada a la API para obtener las categorías
    LaunchedEffect(Unit) {
        try {
            categories = servicio.selectCategories()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            isLoading = false
        }
    }

    Column {
        Text(
            text = "Categorías",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(16.dp)
                .clickable {
                    navController.navigate("categoryList") // Navegar a la lista completa
                }
        )
        if (isLoading) {
            // Indicador de carga mientras se obtienen las categorías
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            LazyRow {
                items(categories) { category ->
                    CategoryItem(category = category)
                }
            }
        }
    }
}

@Composable
fun CategoryItem(category: CategoryModel) {
    Card(
        modifier = Modifier
            .size(180.dp)
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box {
            // Carga de la imagen desde la URL usando Coil
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(category.img)
                    .crossfade(true)
                    .build(),
                contentDescription = category.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Surface(
                color = Color.Black.copy(alpha = 0.6f),
                modifier = Modifier.align(Alignment.BottomStart)
            ) {
                Text(
                    text = category.name,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}
