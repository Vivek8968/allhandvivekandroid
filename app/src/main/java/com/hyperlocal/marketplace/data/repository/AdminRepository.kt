package com.hyperlocal.marketplace.data.repository

import com.hyperlocal.marketplace.data.api.AdminApiService
import com.hyperlocal.marketplace.data.models.*
import com.hyperlocal.marketplace.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdminRepository @Inject constructor(
    private val adminApiService: AdminApiService
) {
    
    suspend fun getUsers(
        token: String,
        page: Int = 1,
        pageSize: Int = 20,
        role: String? = null
    ): Flow<Resource<AdminUsersResponse>> = flow {
        try {
            emit(Resource.Loading())
            val response = adminApiService.getUsers("Bearer $token", page, pageSize, role)
            if (response.isSuccessful) {
                response.body()?.let { usersResponse ->
                    emit(Resource.Success(usersResponse))
                } ?: emit(Resource.Error("Empty response body"))
            } else {
                emit(Resource.Error("Failed to get users: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.localizedMessage}"))
        }
    }
    
    suspend fun getShops(
        token: String,
        page: Int = 1,
        pageSize: Int = 20,
        status: String? = null
    ): Flow<Resource<AdminShopsResponse>> = flow {
        try {
            emit(Resource.Loading())
            val response = adminApiService.getShops("Bearer $token", page, pageSize, status)
            if (response.isSuccessful) {
                response.body()?.let { shopsResponse ->
                    emit(Resource.Success(shopsResponse))
                } ?: emit(Resource.Error("Empty response body"))
            } else {
                emit(Resource.Error("Failed to get shops: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.localizedMessage}"))
        }
    }
    
    suspend fun updateShopStatus(
        token: String,
        shopId: Int,
        request: ShopApprovalRequest
    ): Flow<Resource<Shop>> = flow {
        try {
            emit(Resource.Loading())
            val response = adminApiService.updateShopStatus("Bearer $token", shopId, request)
            if (response.isSuccessful) {
                response.body()?.let { shop ->
                    emit(Resource.Success(shop))
                } ?: emit(Resource.Error("Empty response body"))
            } else {
                emit(Resource.Error("Failed to update shop status: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.localizedMessage}"))
        }
    }
    
    suspend fun getLogs(
        token: String,
        page: Int = 1,
        pageSize: Int = 20
    ): Flow<Resource<AdminLogsResponse>> = flow {
        try {
            emit(Resource.Loading())
            val response = adminApiService.getLogs("Bearer $token", page, pageSize)
            if (response.isSuccessful) {
                response.body()?.let { logsResponse ->
                    emit(Resource.Success(logsResponse))
                } ?: emit(Resource.Error("Empty response body"))
            } else {
                emit(Resource.Error("Failed to get logs: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.localizedMessage}"))
        }
    }
    
    suspend fun getStats(token: String): Flow<Resource<AdminStats>> = flow {
        try {
            emit(Resource.Loading())
            val response = adminApiService.getStats("Bearer $token")
            if (response.isSuccessful) {
                response.body()?.let { stats ->
                    emit(Resource.Success(stats))
                } ?: emit(Resource.Error("Empty response body"))
            } else {
                emit(Resource.Error("Failed to get stats: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.localizedMessage}"))
        }
    }
    
    suspend fun deleteUser(token: String, userId: Int): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            val response = adminApiService.deleteUser("Bearer $token", userId)
            if (response.isSuccessful) {
                emit(Resource.Success(Unit))
            } else {
                emit(Resource.Error("Failed to delete user: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.localizedMessage}"))
        }
    }
    
    suspend fun deleteShop(token: String, shopId: Int): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            val response = adminApiService.deleteShop("Bearer $token", shopId)
            if (response.isSuccessful) {
                emit(Resource.Success(Unit))
            } else {
                emit(Resource.Error("Failed to delete shop: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.localizedMessage}"))
        }
    }
}