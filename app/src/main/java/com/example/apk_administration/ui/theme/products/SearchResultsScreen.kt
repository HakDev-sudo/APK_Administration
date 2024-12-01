package com.example.apk_administration.ui.theme.products

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.apk_administration.ui.theme.navigation.CustomTopBar


@Composable
fun ProductSearchScreen(viewModel: ProductViewModel, navController: NavHostController) {

    val filteredProducts by viewModel.filteredProductList.collectAsState() // Usar el flujo filtrado

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Barra de búsqueda

        Spacer(modifier = Modifier.height(16.dp))
        // Lista de resultados
        LazyColumn {
            items(filteredProducts) { product ->
                ProductItem(product = product, navController)
            }
        }
    }
}

@Composable
fun ProductItem(product: ProductModel, navController: NavHostController) {
    Column(modifier = Modifier
        .padding(8.dp)
        .clickable {
            // Al hacer clic, navegar a la pantalla de detalles del producto
            navController.navigate("productoVer/${product.id}")
        }
    ) {
        Text(text = product.name, style = MaterialTheme.typography.titleMedium)
        Text(text = "Stock: ${product.stock}", style = MaterialTheme.typography.labelMedium)
    }
}


