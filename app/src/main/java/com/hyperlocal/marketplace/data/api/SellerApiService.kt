package com.hyperlocal.marketplace.data.api

import com.hyperlocal.marketplace.config.Config
import com.hyperlocal.marketplace.data.models.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface SellerApiService {
    
    // Shop Management Endpoints
    @POST("shops")
    suspend fun createShop(
        @Header("Authorization") token: String,
        @Body request: ShopCreateRequest
    ): Response<ApiResponse<Shop>>
    
    @GET("shops/my")
    suspend fun getMyShop(
        @Header("Authorization") token: String
    ): Response<ApiResponse<Shop>>
    
    @PUT("shops/my")
    suspend fun updateMyShop(
        @Header("Authorization") token: String,
        @Body request: ShopUpdateRequest
    ): Response<ApiResponse<Shop>>
    
    @GET("shops/{id}")
    suspend fun getShopById(
        @Path("id") shopId: Int
    ): Response<ApiResponse<Shop>>
    
    @POST("upload/shop-image")
    suspend fun uploadShopImage(
        @Header("Authorization") token: String,
        @Query("field_name") fieldName: String
    ): Response<ApiResponse<ImageUploadResponse>>
    
    // Inventory Management Endpoints
    @GET("inventory")
    suspend fun getVendorProducts(
        @Header("Authorization") token: String
    ): Response<ApiResponse<List<Product>>>
    
    @POST("inventory/add-from-catalog")
    suspend fun addProductFromCatalog(
        @Header("Authorization") token: String,
        @Body request: AddProductFromCatalogRequest
    ): Response<ApiResponse<Product>>
    
    @PUT("inventory/{id}")
    suspend fun updateInventoryItem(
        @Header("Authorization") token: String,
        @Path("id") itemId: Int,
        @Body request: Map<String, Any>
    ): Response<ApiResponse<Product>>
    
    @DELETE("inventory/{id}")
    suspend fun deleteInventoryItem(
        @Header("Authorization") token: String,
        @Path("id") itemId: Int
    ): Response<ApiResponse<Any>>
}