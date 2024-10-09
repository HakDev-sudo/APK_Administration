package com.example.apk_administration.ui.theme.NFC

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import com.example.apk_administration.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NFCTopBar(navController: NavHostController) {
    TopAppBar(
        title = { Text("Administrar NFC", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
        actions = {
            IconButton(onClick = { /* Acción para agregar registro NFC */ }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar NFC")
            }
            IconButton(onClick = { /* Acción para filtrar registros NFC */ }) {
                Icon(Icons.Default.FilterList, contentDescription = "Filtrar NFC")
            }
        }
    )
}

@Composable
fun NFCWindow(padding: PaddingValues, navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        // Barra superior con agregar y filtros
        NFCTopBar(navController = navController)

        // Contenido de la pantalla NFC
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Imagen NFC en la parte superior
            Image(
                painter = painterResource(id = R.drawable.ice_nfc), // Cambia por tu recurso NFC
                contentDescription = "NFC Image",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(150.dp)
                    .padding(top = 16.dp)
            )

            // Título grande centrado
            Text(
                text = "Registro NFC",
                fontSize = 32.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )

            // Botones de "Registro de entrada" y "Registro de salida"
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Botón de "Registro de Entrada"
                Button(
                    onClick = { /* Acción de registrar entrada */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp, vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowForward,
                        contentDescription = "Registro de entrada",
                        modifier = Modifier.size(24.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Registrar Entrada", fontSize = 18.sp)
                }

                // Botón de "Registro de Salida"
                Button(
                    onClick = { /* Acción de registrar salida */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp, vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Registro de salida",
                        modifier = Modifier.size(24.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Registrar Salida", fontSize = 18.sp)
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewNFCWindow() {
    MaterialTheme {
        NFCWindow(padding = PaddingValues(0.dp), navController = NavHostController(LocalContext.current) )
    }
}
