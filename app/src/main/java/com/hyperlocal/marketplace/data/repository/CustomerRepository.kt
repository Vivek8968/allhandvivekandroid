package com.hyperlocal.marketplace.data.repository

import com.hyperlocal.marketplace.data.api.CustomerApiService
import com.hyperlocal.marketplace.data.models.*
import com.hyperlocal.marketplace.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomerRepository @Inject constructor(
    private val customerApiService: CustomerApiService
) {
    
    suspend fun getAllShops(
        latitude: Double? = null,
        longitude: Double? = null
    ): Flow<Resource<List<Shop>>> = flow {
        try {
            emit(Resource.Loading())
            val response = customerApiService.getAllShops(latitude, longitude)
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    if (apiResponse.status && apiResponse.data != null) {
                        emit(Resource.Success(apiResponse.data))
                    } else {
                        emit(Resource.Error(apiResponse.message))
                    }
                } ?: emit(Resource.Error("Empty response body"))
            } else {
                emit(Resource.Error("Failed to get shops: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.localizedMessage}"))
        }
    }
    
    suspend fun getShopById(shopId: String): Flow<Resource<Shop>> = flow {
        try {
            emit(Resource.Loading())
            val response = customerApiService.getShopById(shopId)
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    if (apiResponse.status && apiResponse.data != null) {
                        emit(Resource.Success(apiResponse.data))
                    } else {
                        emit(Resource.Error(apiResponse.message))
                    }
                } ?: emit(Resource.Error("Empty response body"))
            } else {
                emit(Resource.Error("Failed to get shop details: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.localizedMessage}"))
        }
    }
    
    suspend fun getShopProducts(
        shopId: String,
        search: String? = null,
        category: String? = null,
        sort: String? = null
    ): Flow<Resource<List<Product>>> = flow {
        try {
            emit(Resource.Loading())
            val response = customerApiService.getShopProducts(shopId, search, category, sort)
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    if (apiResponse.status && apiResponse.data != null) {
                        emit(Resource.Success(apiResponse.data))
                    } else {
                        emit(Resource.Error(apiResponse.message))
                    }
                } ?: emit(Resource.Error("Empty response body"))
            } else {
                emit(Resource.Error("Failed to get shop products: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.localizedMessage}"))
        }
    }
}