package com.hyperlocal.marketplace.data.api

import com.hyperlocal.marketplace.config.Config
import com.hyperlocal.marketplace.data.models.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface SellerApiService {
    
    @POST(Config.Endpoints.CREATE_SHOP)
    suspend fun createShop(
        @Header("Authorization") token: String,
        @Body request: ShopCreateRequest
    ): Response<Shop>
    
    @GET(Config.Endpoints.GET_MY_SHOP)
    suspend fun getMyShop(
        @Header("Authorization") token: String
    ): Response<Shop>
    
    @PUT(Config.Endpoints.UPDATE_SHOP)
    suspend fun updateShop(
        @Header("Authorization") token: String,
        @Body request: ShopUpdateRequest
    ): Response<Shop>
    
    @POST(Config.Endpoints.ADD_PRODUCT)
    suspend fun addProduct(
        @Header("Authorization") token: String,
        @Body request: ShopInventoryCreateRequest
    ): Response<ShopInventory>
    
    @GET(Config.Endpoints.GET_MY_PRODUCTS)
    suspend fun getMyProducts(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 20
    ): Response<List<ShopInventory>>
    
    @PUT(Config.Endpoints.UPDATE_PRODUCT)
    suspend fun updateProduct(
        @Header("Authorization") token: String,
        @Path("id") productId: Int,
        @Body request: ShopInventoryUpdateRequest
    ): Response<ShopInventory>
    
    @DELETE(Config.Endpoints.DELETE_PRODUCT)
    suspend fun deleteProduct(
        @Header("Authorization") token: String,
        @Path("id") productId: Int
    ): Response<Unit>
    
    @Multipart
    @POST(Config.Endpoints.UPLOAD_IMAGE)
    suspend fun uploadImage(
        @Header("Authorization") token: String,
        @Part image: MultipartBody.Part,
        @Part("type") type: String // "shop" or "banner"
    ): Response<Map<String, String>>
}