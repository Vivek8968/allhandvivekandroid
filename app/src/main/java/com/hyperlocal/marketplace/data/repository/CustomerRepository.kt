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
    
    suspend fun getNearbyShops(
        token: String,
        latitude: Double,
        longitude: Double,
        radius: Double = 10.0,
        page: Int = 1,
        pageSize: Int = 20
    ): Flow<Resource<ShopSearchResponse>> = flow {
        try {
            emit(Resource.Loading())
            val response = customerApiService.getNearbyShops(
                "Bearer $token", latitude, longitude, radius, page, pageSize
            )
            if (response.isSuccessful) {
                response.body()?.let { shopResponse ->
                    emit(Resource.Success(shopResponse))
                } ?: emit(Resource.Error("Empty response body"))
            } else {
                emit(Resource.Error("Failed to get nearby shops: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.localizedMessage}"))
        }
    }
    
    suspend fun getShopDetails(token: String, shopId: Int): Flow<Resource<Shop>> = flow {
        try {
            emit(Resource.Loading())
            val response = customerApiService.getShopDetails("Bearer $token", shopId)
            if (response.isSuccessful) {
                response.body()?.let { shop ->
                    emit(Resource.Success(shop))
                } ?: emit(Resource.Error("Empty response body"))
            } else {
                emit(Resource.Error("Failed to get shop details: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.localizedMessage}"))
        }
    }
    
    suspend fun getShopProducts(
        token: String,
        shopId: Int,
        page: Int = 1,
        pageSize: Int = 20
    ): Flow<Resource<ShopProductsResponse>> = flow {
        try {
            emit(Resource.Loading())
            val response = customerApiService.getShopProducts("Bearer $token", shopId, page, pageSize)
            if (response.isSuccessful) {
                response.body()?.let { productsResponse ->
                    emit(Resource.Success(productsResponse))
                } ?: emit(Resource.Error("Empty response body"))
            } else {
                emit(Resource.Error("Failed to get shop products: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.localizedMessage}"))
        }
    }
    
    suspend fun searchShops(
        token: String,
        query: String,
        latitude: Double? = null,
        longitude: Double? = null,
        radius: Double = 10.0,
        page: Int = 1,
        pageSize: Int = 20
    ): Flow<Resource<ShopSearchResponse>> = flow {
        try {
            emit(Resource.Loading())
            val response = customerApiService.searchShops(
                "Bearer $token", query, latitude, longitude, radius, page, pageSize
            )
            if (response.isSuccessful) {
                response.body()?.let { shopResponse ->
                    emit(Resource.Success(shopResponse))
                } ?: emit(Resource.Error("Empty response body"))
            } else {
                emit(Resource.Error("Search failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.localizedMessage}"))
        }
    }
}