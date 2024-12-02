package com.example.apk_administration.ui.theme.Widget

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.actionStartActivity
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.Text
import com.example.apk_administration.MainActivity


import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.dp
import androidx.glance.*
import androidx.glance.action.*
import androidx.glance.appwidget.*
import androidx.glance.layout.*
import androidx.glance.text.Text
import androidx.compose.ui.unit.dp
import androidx.glance.*
import androidx.glance.action.*
import androidx.glance.appwidget.*
import androidx.glance.layout.*
import androidx.glance.text.Text
import kotlinx.coroutines.*


import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.Button

import androidx.glance.action.actionStartActivity
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback

import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.*
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle


import com.example.apk_administration.R
import com.example.apk_administration.ui.theme.products.ProductViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create



class SimpleWidgetContent(
    private val viewModel: ProductViewModel
) : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        viewModel.productList.collect { products ->
            provideContent {
                GlanceTheme {
                    MyContent(products.size, id)
                }
            }
        }
    }

    @Composable
    private fun MyContent(productCount: Int, glanceId: GlanceId) {
        // Container principal con fondo con gradiente suave
        Box(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(GlanceTheme.colors.background)
                .padding(8.dp)
        ) {
            // Contenido principal
            Column(
                modifier = GlanceModifier
                    .fillMaxSize(),
                verticalAlignment = Alignment.Top,
                horizontalAlignment = Alignment.Start
            ) {
                // Header con título y botón de sync
                Row(
                    modifier = GlanceModifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,

                ) {
                    // Título principal
                    Text(
                        text = "Storekeeper",
                        style = TextStyle(
                            fontWeight = FontWeight.Medium,
                            fontSize = 25.sp,
                            color = GlanceTheme.colors.primary
                        )
                    )


                }

                // Tarjeta de información principal
                Box(
                    modifier = GlanceModifier
                        .fillMaxWidth()
                        .cornerRadius(12.dp)
                        .padding(12.dp)
                        .background(GlanceTheme.colors.primaryContainer)
                ) {
                    Row(
                        modifier = GlanceModifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalAlignment = Alignment.Start // Distribuye los elementos uniformemente
                    ) {
                        // Contenedor para icono e información
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalAlignment = Alignment.Start
                        ) {
                            // Icono de inventario
                            Image(
                                provider = ImageProvider(R.drawable.ic_inventory),
                                contentDescription = "Inventario",
                                modifier = GlanceModifier
                                    .size(25.dp),
                                colorFilter = ColorFilter.tint(GlanceTheme.colors.primary)
                            )

                            Spacer(modifier = GlanceModifier.width(12.dp))

                            // Información del conteo
                            Column(
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(
                                    text = productCount.toString(),
                                    style = TextStyle(
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = GlanceTheme.colors.onPrimaryContainer
                                    )
                                )
                                Text(
                                    text = "Productos en inventario",
                                    style = TextStyle(
                                        fontSize = 12.sp,
                                        color = GlanceTheme.colors.onPrimaryContainer
                                    )
                                )
                            }
                        }

                        // Botón de sincronización (alineado a la derecha)
                        Image(
                            provider = ImageProvider(R.drawable.ic_sync),
                            contentDescription = "Actualizar",
                            modifier = GlanceModifier
                                .size(20.dp)
                                .clickable(actionRunCallback<SyncAction>()),
                            colorFilter = ColorFilter.tint(GlanceTheme.colors.primary)
                        )
                    }
                }
                Spacer(modifier = GlanceModifier.height(16.dp))
                // Botón de acción principal
                Box(
                    modifier = GlanceModifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .background(GlanceTheme.colors.primary)
                        .cornerRadius(8.dp)
                        .clickable(onClick = actionStartActivity<MainActivity>())
                        .padding(vertical = 8.dp, horizontal = 12.dp),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,

                    ) {
                        Image(
                            provider = ImageProvider(R.drawable.ic_home),
                            contentDescription = "Abrir app",
                            modifier = GlanceModifier
                                .size(30.dp)
                                .padding(end = 8.dp)
                        )
                        Text(
                            text = "Abrir aplicación",
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = GlanceTheme.colors.onPrimary
                            )
                        )
                    }
                }
            }
        }
    }
}

class SyncAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        // Crear una nueva instancia del ViewModel y actualizar
        val apiService = RetrofitInstance.apiService
        val viewModel = ProductViewModel(apiService)
        viewModel.refreshProducts()

        // Actualizar el widget
        SimpleWidgetContent(viewModel).update(context, glanceId)
    }
}

