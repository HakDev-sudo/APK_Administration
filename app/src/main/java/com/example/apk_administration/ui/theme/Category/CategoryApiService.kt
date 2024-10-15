package com.example.apk_administration.ui.theme.Category

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CategoryApiService {
    @GET("categories")
    suspend fun selectCategories(): List<CategoryModel>

    @GET("categories/{id}/")
    suspend fun selectCategory(@Path("id") id: String): Response<CategoryModel>

    @Headers("Content-Type: application/json")
    @POST("categories/")
    suspend fun insertCategory(@Body category: CategoryModel): Response<CategoryModel>

    @PUT("categories/{id}/")
    suspend fun updateCategory(@Path("id") id: String, @Body category: CategoryModel): Response<CategoryModel>

    @DELETE("categories/{id}/")
    suspend fun deleteCategory(@Path("id") id: String): Response<CategoryModel>
}