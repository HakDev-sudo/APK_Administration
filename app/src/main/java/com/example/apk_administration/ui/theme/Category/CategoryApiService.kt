package com.example.apk_administration.ui.theme.Category

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface CategoryApiService {
    @GET("api/categorias")
    suspend fun selectCategories(): List<CategoryModel>

    @GET("pi/categoria/{id}")
    suspend fun selectCategory(@Path("id") id: String): Response<CategoryModel>

    @Multipart
    @POST("api/categorias")
    suspend fun insertCategory(
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part img: MultipartBody.Part? // La imagen es opcional
    ): Response<CategoryModel>

    @Multipart
    @PUT("api/categoria/{id}")
    suspend fun updateCategory(
        @Path("id") id: String,
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part img: MultipartBody.Part? // La imagen es opcional
    ): Response<CategoryModel>

    @DELETE("api/categoria/{id}")
    suspend fun deleteCategory(@Path("id") id: String): Response<CategoryModel>
}