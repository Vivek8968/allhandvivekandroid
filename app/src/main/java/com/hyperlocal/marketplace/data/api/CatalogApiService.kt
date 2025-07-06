package com.hyperlocal.marketplace.data.api

import com.hyperlocal.marketplace.config.Config
import com.hyperlocal.marketplace.data.models.*
import retrofit2.Response
import retrofit2.http.*

interface CatalogApiService {
    
    @GET("items")
    suspend fun getCatalog(
        @Query("search") search: String? = null,
        @Query("category") category: String? = null
    ): Response<ApiResponse<List<CatalogItem>>>
    
    @GET("categories")
    suspend fun getCatalogCategories(): Response<ApiResponse<List<String>>>
    
    @GET("items/{id}")
    suspend fun getProductById(
        @Path("id") productId: String
    ): Response<ApiResponse<Product>>
}