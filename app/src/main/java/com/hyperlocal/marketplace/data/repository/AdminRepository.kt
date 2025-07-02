package com.hyperlocal.marketplace.data.repository

import com.hyperlocal.marketplace.data.api.AdminApiService
import com.hyperlocal.marketplace.data.models.*
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdminRepository @Inject constructor(
    private val adminApiService: AdminApiService
) {
    
    suspend fun getAllShops(token: String): Response<ApiResponse<List<Shop>>> {
        return adminApiService.getAllShops(token)
    }
    
    suspend fun getAllUsers(token: String): Response<ApiResponse<List<User>>> {
        return adminApiService.getAllUsers(token)
    }
    
    suspend fun getStats(token: String): Response<ApiResponse<Map<String, Int>>> {
        return adminApiService.getStats(token)
    }
    
    suspend fun approveShop(token: String, shopId: String): Response<ApiResponse<Shop>> {
        return adminApiService.approveShop(token, shopId)
    }
    
    suspend fun rejectShop(token: String, shopId: String): Response<ApiResponse<Shop>> {
        return adminApiService.rejectShop(token, shopId)
    }
    
    suspend fun deleteShop(token: String, shopId: String): Response<ApiResponse<Any>> {
        return adminApiService.deleteShop(token, shopId)
    }
    
    suspend fun deleteUser(token: String, userId: Int): Response<ApiResponse<Any>> {
        return adminApiService.deleteUser(token, userId)
    }
}