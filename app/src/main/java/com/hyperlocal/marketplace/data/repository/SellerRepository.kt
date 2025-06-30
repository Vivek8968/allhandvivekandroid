package com.hyperlocal.marketplace.data.repository

import com.hyperlocal.marketplace.data.api.SellerApiService
import com.hyperlocal.marketplace.data.models.*
import com.hyperlocal.marketplace.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SellerRepository @Inject constructor(
    private val sellerApiService: SellerApiService
) {
    
    suspend fun createShop(request: ShopCreateRequest): Flow<Resource<Shop>> = flow {
        try {
            emit(Resource.Loading())
            val response = sellerApiService.createShop(request)
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    if (apiResponse.status && apiResponse.data != null) {
                        emit(Resource.Success(apiResponse.data))
                    } else {
                        emit(Resource.Error(apiResponse.message))
                    }
                } ?: emit(Resource.Error("Empty response body"))
            } else {
                emit(Resource.Error("Shop creation failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.localizedMessage}"))
        }
    }
    
    suspend fun getVendorShop(): Flow<Resource<Shop>> = flow {
        try {
            emit(Resource.Loading())
            val response = sellerApiService.getVendorShop()
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    if (apiResponse.status && apiResponse.data != null) {
                        emit(Resource.Success(apiResponse.data))
                    } else {
                        emit(Resource.Error(apiResponse.message))
                    }
                } ?: emit(Resource.Error("Empty response body"))
            } else {
                emit(Resource.Error("Failed to get shop: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.localizedMessage}"))
        }
    }
    
    suspend fun getVendorProducts(): Flow<Resource<List<Product>>> = flow {
        try {
            emit(Resource.Loading())
            val response = sellerApiService.getVendorProducts()
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    if (apiResponse.status && apiResponse.data != null) {
                        emit(Resource.Success(apiResponse.data))
                    } else {
                        emit(Resource.Error(apiResponse.message))
                    }
                } ?: emit(Resource.Error("Empty response body"))
            } else {
                emit(Resource.Error("Failed to get products: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.localizedMessage}"))
        }
    }
    
    suspend fun addProductFromCatalog(request: AddProductFromCatalogRequest): Flow<Resource<Product>> = flow {
        try {
            emit(Resource.Loading())
            val response = sellerApiService.addProductFromCatalog(request)
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    if (apiResponse.status && apiResponse.data != null) {
                        emit(Resource.Success(apiResponse.data))
                    } else {
                        emit(Resource.Error(apiResponse.message))
                    }
                } ?: emit(Resource.Error("Empty response body"))
            } else {
                emit(Resource.Error("Product addition failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.localizedMessage}"))
        }
    }
    
    suspend fun addProductToShop(shopId: String, request: ProductCreateRequest): Flow<Resource<Product>> = flow {
        try {
            emit(Resource.Loading())
            val response = sellerApiService.addProductToShop(shopId, request)
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    if (apiResponse.status && apiResponse.data != null) {
                        emit(Resource.Success(apiResponse.data))
                    } else {
                        emit(Resource.Error(apiResponse.message))
                    }
                } ?: emit(Resource.Error("Empty response body"))
            } else {
                emit(Resource.Error("Product addition failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.localizedMessage}"))
        }
    }
}