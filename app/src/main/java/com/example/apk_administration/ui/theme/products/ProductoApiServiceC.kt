package com.example.apk_administration.ui.theme.products

import retrofit2.http.GET
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProductoApiServiceC {
    @GET("productos")
    suspend fun selectProductos(): List<ProductoModelGet>

    @GET("productos/{id}/")
    suspend fun selectProducto(@Path("id") id: String): Response<ProductoModelGet>

    @Headers("Content-Type: application/json")
    @POST("productos/")
    suspend fun insertProducto(@Body producto: ProductoModelPost): Response<ProductoModelGet>

    @PUT("productos/{id}/")
    suspend fun updateProducto(@Path("id") id: String, @Body producto: ProductoModelPost): Response<ProductoModelGet>

    @DELETE("productos/{id}/")
    suspend fun deleteProducto(@Path("id") id: String): Response<ProductoModelGet>
}