package com.hyperlocal.marketplace.data.api

import com.hyperlocal.marketplace.config.Config
import com.hyperlocal.marketplace.data.models.*
import retrofit2.Response
import retrofit2.http.*

interface AdminApiService {
    
    @GET(Config.Endpoints.ADMIN_STATS)
    suspend fun getStats(): Response<ApiResponse<AdminStats>>
    
    @GET(Config.Endpoints.ADMIN_USERS)
    suspend fun getUsers(): Response<ApiResponse<List<User>>>
    
    @GET(Config.Endpoints.ADMIN_SHOPS)
    suspend fun getShops(): Response<ApiResponse<List<Shop>>>
    
    // Placeholder endpoints for future implementation
    @PUT("admin/shops/{id}/approve")
    suspend fun approveShop(
        @Path("id") shopId: String
    ): Response<ApiResponse<Shop>>
    
    @PUT("admin/shops/{id}/reject")
    suspend fun rejectShop(
        @Path("id") shopId: String
    ): Response<ApiResponse<Shop>>
}