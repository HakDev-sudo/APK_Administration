package com.example.apk_administration.ui.theme.Category

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryListScreen(navController: NavHostController, servicio: CategoryApiService) {
    var listaCategorias: SnapshotStateList<CategoryModel> = remember { mutableStateListOf() }

    // Cargamos las categorías desde el servicio
    LaunchedEffect(Unit) {
        val categorias = servicio.selectCategories()
        listaCategorias.addAll(categorias)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Barra superior
        TopAppBar(
            title = { Text(text = "Listado de Categorías") },
            actions = {
                // Botón de agregar nueva categoría
                IconButton(onClick = {
                    // Navegar a la vista de agregar una nueva categoría
                    navController.navigate("categoriaNueva")
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar Categoría")
                }
            },
        )

        // Lista de categorías
        LazyColumn(modifier = Modifier.padding(8.dp)) {
            items(listaCategorias) { categoria ->
                // Cada categoría se muestra en una tarjeta
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable {
                            // Navegar a la vista de detalles al hacer clic en la tarjeta
                            navController.navigate("categoriaVer/${categoria.id}")
                        },
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(Color(0xFFA49BEF)) // Diferente color para categorías
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Imagen circular al inicio
                        Box(
                            modifier = Modifier
                                .size(60.dp) // Tamaño del círculo
                                .clip(CircleShape) // Forma circular
                                .background(Color.LightGray), // Color predeterminado si no hay imagen
                            contentAlignment = Alignment.Center
                        ) {
                            if (categoria.img.isNullOrEmpty()) {
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
                                    model = categoria.img, // URL de la imagen
                                    contentDescription = "Imagen de la Categoría",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop // Escalado de la imagen
                                )
                            }
                        }
                        // Mostrar la información de la categoría
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = categoria.name, style = MaterialTheme.typography.headlineMedium)
                        }

                        // Botones de Editar y Eliminar
                        Row {
                            IconButton(onClick = {
                                // Navegar a la vista de editar categoría
                                navController.navigate("categoriaEditar/${categoria.id}")
                            }) {
                                Icon(Icons.Default.Edit, contentDescription = "Editar Categoría")
                            }
                            IconButton(onClick = {
                                // Navegar a la vista de eliminar categoría
                                navController.navigate("categoriaDel/${categoria.id}")
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar Categoría")
                            }
                        }
                    }
                }
            }
        }
    }
}
