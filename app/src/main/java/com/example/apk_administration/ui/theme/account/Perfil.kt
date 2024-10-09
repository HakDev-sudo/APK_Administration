package com.example.apk_administration.ui.theme.account

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileImageScreen(
    onNextClick: (Uri?) -> Unit,
    navController: NavHostController
) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    // Lanzador para seleccionar imágenes
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri // Almacena la URI de la imagen seleccionada
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Imagen de Perfil") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) { // Navegar hacia atrás
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF63A7E1))
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text("Imagen de Perfil", style = MaterialTheme.typography.headlineMedium)

            imageUri?.let {
                AsyncImage(
                    model = it,
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.Gray, CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    // Abre el selector de imágenes
                    launcher.launch("image/*")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Seleccionar Imagen")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botones "Cancelar" y "Siguiente" en la misma fila
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        // Navegar a la pantalla de login
                        navController.navigate("login") {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(Color.Red)
                ) {
                    Text("Cancelar")
                }

                Button(
                    onClick = { onNextClick(imageUri)
                    navController.navigate("password_screen") },
                    colors = ButtonDefaults.buttonColors(Color(0xFF63A7E1))
                ) {
                    Text("Siguiente")
                }
            }
        }
    }
}



