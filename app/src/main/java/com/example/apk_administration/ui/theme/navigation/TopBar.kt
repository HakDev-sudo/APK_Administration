package com.example.apk_administration.ui.theme.navigation

import androidx.compose.material3.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.apk_administration.R
import com.example.apk_administration.ui.theme.products.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopBar(
    navController: NavHostController,
    viewModel: ProductViewModel,
    isSearching: MutableState<Boolean>
) {
    // Estado para controlar si la barra de búsqueda está activa

    val searchQuery = remember { mutableStateOf("") }

    TopAppBar(
        title = {
            if (isSearching.value) {
                // Barra de búsqueda expandida
                TextField(
                    value = searchQuery.value,
                    onValueChange = { query ->
                        searchQuery.value = query
                        viewModel.searchProducts(query) // Actualiza la lista filtrada en tiempo real
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Buscar productos...") },
                    singleLine = true,
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        cursorColor = Color.Black
                    )
                )
            } else {
                // Título normal cuando no se está buscando
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Logo",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "StoreKeeper", style = MaterialTheme.typography.titleMedium)
                }
            }
        },
        actions = {
            if (isSearching.value) {
                // Botón para cerrar la barra de búsqueda
                IconButton(onClick = {
                    isSearching.value = false
                    navController.popBackStack()
                }) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "Cerrar búsqueda"
                    )
                }
            } else {
                // Botón para abrir la barra de búsqueda
                IconButton(onClick = {
                    navController.navigate("productoSearch")
                    isSearching.value = true

                }) {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = "Buscar"
                    )
                }
            }
            // Botón de notificaciones
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "Notificaciones"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White,
            titleContentColor = Color.Black,
            actionIconContentColor = Color.Black
        )
    )
}



