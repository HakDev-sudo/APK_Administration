package com.example.apk_administration.ui.theme.home

import androidx.compose.foundation.Image
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
import com.example.apk_administration.R

data class Category(val name: String, val imageResId: Int)

@Composable
fun CategoryItem(category: Category) {
    Card(
        modifier = Modifier
            .size(150.dp)
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box {
            Image(
                painter = painterResource(id = category.imageResId),
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

@Composable
fun CategoryCarousel(categories: List<Category>) {
    Column {
        Text(
            text = "Categorías",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )
        LazyRow {
            items(categories) { category ->
                CategoryItem(category = category)
            }
        }
    }
}





@Preview(showBackground = true)
@Composable
fun PreviewFullScreen() {
    MaterialTheme {
        Column {
            // Datos ficticios mientras no tienes imágenes reales
            val categories = listOf(
                Category("Filtros", R.drawable.filtros_1),  // Usar una imagen de placeholder
                Category("Aceite", R.drawable.filtros_1),
                Category("Motores", R.drawable.filtros_1),
                Category("Respuestos", R.drawable.filtros_1)
            )

            // Llamar a tu componente con los datos de prueba
            CategoryCarousel(categories = categories)
        }
    }
}


