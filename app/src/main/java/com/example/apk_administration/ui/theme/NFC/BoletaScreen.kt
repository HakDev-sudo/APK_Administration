package com.example.apk_administration.ui.theme.NFC

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavHostController
import androidx.wear.compose.material3.Button
import com.google.gson.Gson

@Composable
fun ReceiptScreen(receiptDataJson: String, navController: NavHostController) {
    val productDetails = remember {
        Gson().fromJson(receiptDataJson, Array<ProductDetail>::class.java).toList()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Boleta de Pago", style = MaterialTheme.typography.titleLarge)

        LazyColumn {
            items(productDetails) { detail ->
                Text("Nombre: ${detail.name}")
                Text("Cantidad: ${detail.quantity}")
                Text("Precio Unitario: ${detail.unitPrice}")
                Text("Subtotal: ${detail.subtotal}")
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        val totalAmount = productDetails.sumOf { it.subtotal }
        Text(
            text = "Total: $${"%.2f".format(totalAmount)}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Button(
            onClick = {
                navController.navigate("nfc") // Redirigir a otra pantalla después de finalizar
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Finalizar")
        }
    }
}




