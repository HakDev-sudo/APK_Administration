package com.example.apk_administration.ui.theme.Widget

import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent

import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.apk_administration.R
import com.example.apk_administration.ui.theme.products.ProductViewModel
import com.example.apk_administration.ui.theme.products.ProductoApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit



class SimpleWidget : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
    get() {
        val apiService = RetrofitInstance.apiService
        val viewModel = ProductViewModel(apiService)
        return SimpleWidgetContent(viewModel)
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        // Programar actualizaciones periódicas
        setupPeriodicUpdate(context)
    }

    private fun setupPeriodicUpdate(context: Context) {
        val workManager = WorkManager.getInstance(context)

        // Configurar restricciones de la tarea
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        // Crear solicitud de trabajo periódico
        val updateRequest = PeriodicWorkRequestBuilder<WidgetUpdateWorker>(
            15, TimeUnit.MINUTES, // Actualizar cada 15 minutos
            5, TimeUnit.MINUTES  // Flexibilidad de 5 minutos
        )
            .setConstraints(constraints)
            .build()

        // Programar el trabajo
        workManager.enqueueUniquePeriodicWork(
            "widget_update",
            ExistingPeriodicWorkPolicy.REPLACE,
            updateRequest
        )
    }
}


object RetrofitInstance {
    private const val BASE_URL = "https://octopus-app-o74mu.ondigitalocean.app/"

    val apiService: ProductoApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductoApiService::class.java)
    }
}
// Worker para actualizar el widget
class WidgetUpdateWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        try {
            // Actualizar el widget
            val manager = GlanceAppWidgetManager(applicationContext)
            val widgets = manager.getGlanceIds(SimpleWidgetContent::class.java)
            widgets.forEach { widgetId ->
                updateAppWidget(applicationContext, widgetId)
            }
            return Result.success()
        } catch (e: Exception) {
            return Result.retry()
        }
    }

    private suspend fun updateAppWidget(context: Context, glanceId: GlanceId) {
        val apiService = RetrofitInstance.apiService
        val viewModel = ProductViewModel(apiService)
        viewModel.refreshProducts()
        SimpleWidgetContent(viewModel).update(context, glanceId)
    }
}

