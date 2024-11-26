package com.example.apk_administration.ui.theme.products
import com.example.apk_administration.ui.theme.Category.CategoryModel
import com.example.apk_administration.ui.theme.NFC.NfcModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ProductoApiService {
    @GET("api/productos") // Ajustado según la nueva ruta
    suspend fun selectProductos(): List<ProductModel>

    @GET("api/producto/{id}") // Ajustado según la nueva ruta para detalles de un producto
    suspend fun selectProducto(@Path("id") id: Int): Response<ProductModel>

    @Multipart
    @POST("api/productos")
    suspend fun insertProducto(
        @Part("name") name: RequestBody,
        @Part("price") price: RequestBody,
        @Part("description") description: RequestBody,
        @Part("category") category: RequestBody,
        @Part img: MultipartBody.Part? // La imagen es opcional
    ): Response<ProductModel>

    @Multipart
    @PUT("api/producto/{id}")
    suspend fun updateProducto(
        @Path("id") id: Int,
        @Part("name") name: RequestBody,
        @Part("price") price: RequestBody,
        @Part("description") description: RequestBody,
        @Part("category") category: RequestBody,
        @Part img: MultipartBody.Part?
    ): Response<ProductModel>

    @DELETE("api/producto/{id}") // Ajustado según la nueva ruta para eliminar un producto
    suspend fun deleteProducto(@Path("id") id: Int): Response<ProductModel>
    // Nueva función para obtener categorías
    @GET("api/categorias")
    suspend fun selectCategories(): List<CategoryModel>

    // Nueva función para obtener etiquetas RFID
    @GET("api/nfc")
    suspend fun selectnfcs(): List<NfcModel>
}