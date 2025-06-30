package com.hyperlocal.marketplace.data.api

import com.hyperlocal.marketplace.config.Config
import com.hyperlocal.marketplace.data.models.*
import retrofit2.Response
import retrofit2.http.*

interface CustomerApiService {
    
    @GET(Config.Endpoints.NEARBY_SHOPS)
    suspend fun getNearbyShops(
        @Header("Authorization") token: String,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("radius") radius: Double = 10.0,
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 20
    ): Response<ShopSearchResponse>
    
    @GET(Config.Endpoints.SHOP_DETAILS)
    suspend fun getShopDetails(
        @Header("Authorization") token: String,
        @Path("id") shopId: Int
    ): Response<Shop>
    
    @GET(Config.Endpoints.SHOP_PRODUCTS)
    suspend fun getShopProducts(
        @Header("Authorization") token: String,
        @Path("id") shopId: Int,
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 20
    ): Response<ShopProductsResponse>
    
    @GET(Config.Endpoints.SEARCH_SHOPS)
    suspend fun searchShops(
        @Header("Authorization") token: String,
        @Query("query") query: String,
        @Query("latitude") latitude: Double? = null,
        @Query("longitude") longitude: Double? = null,
        @Query("radius") radius: Double = 10.0,
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 20
    ): Response<ShopSearchResponse>
}