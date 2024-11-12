package com.example.apk_administration.ui.theme.products
import com.example.apk_administration.ui.theme.Category.CategoryModel
import com.example.apk_administration.ui.theme.NFC.NfcModel
import retrofit2.http.GET
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProductoApiService {
    @GET("api/productos") // Ajustado según la nueva ruta
    suspend fun selectProductos(): List<ProductModel>

    @GET("api/producto/{id}") // Ajustado según la nueva ruta para detalles de un producto
    suspend fun selectProducto(@Path("id") id: Int): Response<ProductModel>

    @Headers("Content-Type: application/json")
    @POST("api/productos") // Ajustado según la nueva ruta para crear un producto
    suspend fun insertProducto(@Body producto: ProductModel): Response<ProductoModelGet>

    @PUT("api/producto/{id}") // Ajustado según la nueva ruta para actualizar un producto
    suspend fun updateProducto(@Path("id") id: Int, @Body producto: ProductModel): Response<ProductModel>

    @DELETE("api/producto/{id}") // Ajustado según la nueva ruta para eliminar un producto
    suspend fun deleteProducto(@Path("id") id: Int): Response<ProductModel>
    // Nueva función para obtener categorías
    @GET("api/categorias")
    suspend fun selectCategories(): List<CategoryModel>

    // Nueva función para obtener etiquetas RFID
    @GET("api/uid")
    suspend fun selectnfcs(): List<NfcModel>
}