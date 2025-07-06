package com.hyperlocal.marketplace.data.api

import com.hyperlocal.marketplace.config.Config
import com.hyperlocal.marketplace.data.models.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface SellerApiService {
    
    // Shop Management Endpoints
    @POST(Config.Endpoints.CREATE_SHOP)
    suspend fun createShop(
        @Header("Authorization") token: String,
        @Body request: ShopCreateRequest
    ): Response<ApiResponse<Shop>>
    
    @GET(Config.Endpoints.GET_MY_SHOP)
    suspend fun getMyShop(
        @Header("Authorization") token: String
    ): Response<ApiResponse<Shop>>
    
    @PUT(Config.Endpoints.UPDATE_MY_SHOP)
    suspend fun updateMyShop(
        @Header("Authorization") token: String,
        @Body request: ShopUpdateRequest
    ): Response<ApiResponse<Shop>>
    
    @GET(Config.Endpoints.GET_SHOP_BY_ID)
    suspend fun getShopById(
        @Path("id") shopId: Int
    ): Response<ApiResponse<Shop>>
    
    @POST(Config.Endpoints.UPLOAD_SHOP_IMAGE)
    suspend fun uploadShopImage(
        @Header("Authorization") token: String,
        @Query("field_name") fieldName: String
    ): Response<ApiResponse<ImageUploadResponse>>
    
    // Inventory Management Endpoints
    @GET(Config.Endpoints.GET_VENDOR_PRODUCTS)
    suspend fun getVendorProducts(
        @Header("Authorization") token: String
    ): Response<ApiResponse<List<Product>>>
    
    @POST(Config.Endpoints.ADD_PRODUCT_FROM_CATALOG)
    suspend fun addProductFromCatalog(
        @Header("Authorization") token: String,
        @Body request: AddProductFromCatalogRequest
    ): Response<ApiResponse<Product>>
    
    @PUT(Config.Endpoints.UPDATE_INVENTORY_ITEM)
    suspend fun updateInventoryItem(
        @Header("Authorization") token: String,
        @Path("id") itemId: Int,
        @Body request: Map<String, Any>
    ): Response<ApiResponse<Product>>
    
    @DELETE(Config.Endpoints.DELETE_INVENTORY_ITEM)
    suspend fun deleteInventoryItem(
        @Header("Authorization") token: String,
        @Path("id") itemId: Int
    ): Response<ApiResponse<Any>>
}