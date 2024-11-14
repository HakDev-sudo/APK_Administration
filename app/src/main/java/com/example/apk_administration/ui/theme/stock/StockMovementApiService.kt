package com.example.apk_administration.ui.theme.stock

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface StockMovementApiService {
    @GET("api/stockmovements")
    suspend fun selectStockMovements(): List<StockMovementModel> // Obtener todos los movimientos de stock

    @GET("api/stockmovement/{id}")
    suspend fun selectStockMovement(@Path("id") id: Int): Response<StockMovementModel> // Obtener un movimiento de stock específico por ID

    @Headers("Content-Type: application/json")
    @POST("api/stockmovements")
    suspend fun insertStockMovement(@Body stockMovement: StockMovementModel): Response<StockMovementModel> // Crear un nuevo movimiento de stock

    @PUT("api/stockmovement/{id}")
    suspend fun updateStockMovement(@Path("id") id: Int, @Body stockMovement: StockMovementModel): Response<StockMovementModel> // Actualizar un movimiento de stock

    @DELETE("api/stockmovement/{id}")
    suspend fun deleteStockMovement(@Path("id") id: Int): Response<StockMovementModel> // Eliminar un movimiento de stock
}