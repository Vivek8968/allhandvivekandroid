package com.hyperlocal.marketplace.data.api

import com.hyperlocal.marketplace.config.Config
import com.hyperlocal.marketplace.data.models.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface SellerApiService {
    
    @GET(Config.Endpoints.GET_VENDOR_SHOP)
    suspend fun getVendorShop(
        @Header("Authorization") token: String
    ): Response<ApiResponse<Shop>>
    
    @POST(Config.Endpoints.CREATE_VENDOR_SHOP)
    suspend fun createVendorShop(
        @Header("Authorization") token: String,
        @Body request: ShopCreateRequest
    ): Response<ApiResponse<Shop>>
    
    @PUT(Config.Endpoints.UPDATE_VENDOR_SHOP)
    suspend fun updateVendorShop(
        @Header("Authorization") token: String,
        @Body request: ShopUpdateRequest
    ): Response<ApiResponse<Shop>>
    
    @GET(Config.Endpoints.GET_VENDOR_PRODUCTS)
    suspend fun getVendorProducts(
        @Header("Authorization") token: String
    ): Response<ApiResponse<List<Product>>>
    
    @POST(Config.Endpoints.ADD_PRODUCT_FROM_CATALOG)
    suspend fun addProductFromCatalog(
        @Header("Authorization") token: String,
        @Body request: AddProductFromCatalogRequest
    ): Response<ApiResponse<Product>>
    
    // Generic shop and product endpoints
    @POST(Config.Endpoints.CREATE_SHOP)
    suspend fun createShop(
        @Header("Authorization") token: String,
        @Body request: ShopCreateRequest
    ): Response<ApiResponse<Shop>>
    
    @POST(Config.Endpoints.ADD_PRODUCT_TO_SHOP)
    suspend fun addProductToShop(
        @Header("Authorization") token: String,
        @Path("id") shopId: String,
        @Body request: ProductCreateRequest
    ): Response<ApiResponse<Product>>
    
    @PUT(Config.Endpoints.UPDATE_SHOP_PRODUCT)
    suspend fun updateShopProduct(
        @Header("Authorization") token: String,
        @Path("shop_id") shopId: String,
        @Path("product_id") productId: String,
        @Body request: Map<String, Any>
    ): Response<ApiResponse<Product>>
    
    @DELETE(Config.Endpoints.DELETE_SHOP_PRODUCT)
    suspend fun deleteShopProduct(
        @Header("Authorization") token: String,
        @Path("shop_id") shopId: String,
        @Path("product_id") productId: String
    ): Response<ApiResponse<Any>>
}