package com.hyperlocal.marketplace.data.api

import com.hyperlocal.marketplace.config.Config
import com.hyperlocal.marketplace.data.models.*
import retrofit2.Response
import retrofit2.http.*

interface AdminApiService {
    
    @GET(Config.Endpoints.ADMIN_USERS)
    suspend fun getUsers(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 20,
        @Query("role") role: String? = null
    ): Response<AdminUsersResponse>
    
    @GET(Config.Endpoints.ADMIN_SHOPS)
    suspend fun getShops(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 20,
        @Query("status") status: String? = null // "approved", "pending", "rejected"
    ): Response<AdminShopsResponse>
    
    @PUT("admin/shops/{id}")
    suspend fun updateShopStatus(
        @Header("Authorization") token: String,
        @Path("id") shopId: Int,
        @Body request: ShopApprovalRequest
    ): Response<Shop>
    
    @GET(Config.Endpoints.ADMIN_LOGS)
    suspend fun getLogs(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 20
    ): Response<AdminLogsResponse>
    
    @GET(Config.Endpoints.ADMIN_STATS)
    suspend fun getStats(
        @Header("Authorization") token: String
    ): Response<AdminStats>
    
    @DELETE("admin/users/{id}")
    suspend fun deleteUser(
        @Header("Authorization") token: String,
        @Path("id") userId: Int
    ): Response<Unit>
    
    @DELETE("admin/shops/{id}")
    suspend fun deleteShop(
        @Header("Authorization") token: String,
        @Path("id") shopId: Int
    ): Response<Unit>
}