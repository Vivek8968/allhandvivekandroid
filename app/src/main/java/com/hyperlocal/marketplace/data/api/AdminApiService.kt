package com.hyperlocal.marketplace.data.api

import com.hyperlocal.marketplace.config.Config
import com.hyperlocal.marketplace.data.models.*
import retrofit2.Response
import retrofit2.http.*

interface AdminApiService {
    
    @GET(Config.Endpoints.ADMIN_STATS)
    suspend fun getStats(
        @Header("Authorization") token: String
    ): Response<ApiResponse<Map<String, Int>>>
    
    @GET(Config.Endpoints.ADMIN_USERS)
    suspend fun getAllUsers(
        @Header("Authorization") token: String
    ): Response<ApiResponse<List<User>>>
    
    @GET(Config.Endpoints.ADMIN_SHOPS)
    suspend fun getAllShops(
        @Header("Authorization") token: String
    ): Response<ApiResponse<List<Shop>>>
    
    @PUT("admin/shops/{id}/approve")
    suspend fun approveShop(
        @Header("Authorization") token: String,
        @Path("id") shopId: String
    ): Response<ApiResponse<Shop>>
    
    @PUT("admin/shops/{id}/reject")
    suspend fun rejectShop(
        @Header("Authorization") token: String,
        @Path("id") shopId: String
    ): Response<ApiResponse<Shop>>
    
    @DELETE("admin/shops/{id}")
    suspend fun deleteShop(
        @Header("Authorization") token: String,
        @Path("id") shopId: String
    ): Response<ApiResponse<Any>>
    
    @DELETE("admin/users/{id}")
    suspend fun deleteUser(
        @Header("Authorization") token: String,
        @Path("id") userId: Int
    ): Response<ApiResponse<Any>>
}