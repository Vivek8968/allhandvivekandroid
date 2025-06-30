package com.hyperlocal.marketplace.data.api

import com.hyperlocal.marketplace.config.Config
import com.hyperlocal.marketplace.data.models.*
import retrofit2.Response
import retrofit2.http.*

interface CustomerApiService {
    
    @GET(Config.Endpoints.GET_ALL_SHOPS)
    suspend fun getAllShops(
        @Query("latitude") latitude: Double? = null,
        @Query("longitude") longitude: Double? = null
    ): Response<ApiResponse<List<Shop>>>
    
    @GET(Config.Endpoints.GET_SHOP_BY_ID)
    suspend fun getShopById(
        @Path("id") shopId: String
    ): Response<ApiResponse<Shop>>
    
    @GET(Config.Endpoints.GET_SHOP_PRODUCTS)
    suspend fun getShopProducts(
        @Path("id") shopId: String,
        @Query("search") search: String? = null,
        @Query("category") category: String? = null,
        @Query("sort") sort: String? = null
    ): Response<ApiResponse<List<Product>>>
}