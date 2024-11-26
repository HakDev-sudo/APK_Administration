package com.example.apk_administration.ui.theme.Category

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.apk_administration.ui.theme.products.ProductViewModel
import com.example.apk_administration.ui.theme.products.ProductoApiService
import com.example.apk_administration.ui.theme.products.toRequestBody
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import coil.compose.rememberImagePainter
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody


@OptIn(UnstableApi::class)
@Composable
fun ContenidoCategoryEditar(
    navController: NavHostController,
    servicio: CategoryApiService,
    productoApiService: ProductoApiService,
    categoryId: Int = 0
) {
    val context = LocalContext.current

    // Estados para los campos
    var name by remember { mutableStateOf("") }
    var imgUri by remember { mutableStateOf<Uri?>(null) }
    var existingImgUrl by remember { mutableStateOf<String?>(null) }
    var isSaveRequested by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(categoryId != 0) }
    var isProcessing by remember { mutableStateOf(false) }

    // Lector de imágenes
    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imgUri = uri
    }

    // Cargar datos si es edición
    LaunchedEffect(Unit) {
        if (categoryId != 0) {
            val response = servicio.selectCategory(categoryId.toString())
            if (response.isSuccessful) {
                response.body()?.let {
                    name = it.name
                    existingImgUrl = it.img
                }
            }
            isLoading = false
        } else {
            isLoading = false
        }
    }

    // Manejar la lógica de guardado
    LaunchedEffect(isSaveRequested) {
        if (isSaveRequested) {
            try {
                val namePart = name.toRequestBody("text/plain".toMediaType())

                val imgPart = when {
                    imgUri != null -> {
                        val inputStream = context.contentResolver.openInputStream(imgUri!!)
                        val bytes = inputStream?.readBytes()
                        val requestBody = bytes?.toRequestBody("image/*".toMediaType())
                        MultipartBody.Part.createFormData("img", "image.jpg", requestBody!!)
                    }
                    existingImgUrl != null -> {
                        val requestBody = existingImgUrl!!.toRequestBody("text/plain".toMediaType())
                        MultipartBody.Part.createFormData("existingImg", "image.jpg", requestBody)
                    }
                    else -> null
                }

                val response = if (categoryId == 0) {
                    servicio.insertCategory(namePart, "".toRequestBody("text/plain".toMediaType()), imgPart)
                } else {
                    servicio.updateCategory(categoryId.toString(), namePart, "".toRequestBody("text/plain".toMediaType()), imgPart)
                }

                if (response.isSuccessful) {
                    navController.navigate("categoryList") {
                        popUpTo("categoryList") { inclusive = true }
                    }
                }
            } catch (e: Exception) {
                Log.e("AddOrEditCategoryScreen", "Error: ${e.message}")
            } finally {
                isSaveRequested = false
                isProcessing = false
            }
        }
    }

    // Layout
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (isLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        } else {
            // Campo de nombre
            item {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre de la Categoría") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Subir imagen
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(onClick = { imageLauncher.launch("image/*") }) {
                        Text("Seleccionar Imagen")
                    }
                    if (imgUri != null) {
                        Image(
                            painter = rememberAsyncImagePainter(imgUri),
                            contentDescription = "Nueva imagen seleccionada",
                            modifier = Modifier
                                .size(200.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                    } else if (existingImgUrl != null) {
                        Image(
                            painter = rememberAsyncImagePainter(existingImgUrl),
                            contentDescription = "Imagen existente",
                            modifier = Modifier
                                .size(200.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Botón Guardar
            item {
                Button(
                    onClick = {
                        if (name.isNotEmpty()) {
                            isSaveRequested = true
                            isProcessing = true
                        } else {
                            Log.e("AddOrEditCategoryScreen", "Campos obligatorios faltantes.")
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isProcessing
                ) {
                    Text(if (categoryId == 0) "Guardar Categoría" else "Actualizar Categoría")
                }
            }
        }
    }
}

