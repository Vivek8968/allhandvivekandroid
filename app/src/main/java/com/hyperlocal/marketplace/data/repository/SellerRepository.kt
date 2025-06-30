package com.hyperlocal.marketplace.data.repository

import com.hyperlocal.marketplace.data.api.SellerApiService
import com.hyperlocal.marketplace.data.models.*
import com.hyperlocal.marketplace.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SellerRepository @Inject constructor(
    private val sellerApiService: SellerApiService
) {
    
    suspend fun createShop(token: String, request: ShopCreateRequest): Flow<Resource<Shop>> = flow {
        try {
            emit(Resource.Loading())
            val response = sellerApiService.createShop("Bearer $token", request)
            if (response.isSuccessful) {
                response.body()?.let { shop ->
                    emit(Resource.Success(shop))
                } ?: emit(Resource.Error("Empty response body"))
            } else {
                emit(Resource.Error("Shop creation failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.localizedMessage}"))
        }
    }
    
    suspend fun getMyShop(token: String): Flow<Resource<Shop>> = flow {
        try {
            emit(Resource.Loading())
            val response = sellerApiService.getMyShop("Bearer $token")
            if (response.isSuccessful) {
                response.body()?.let { shop ->
                    emit(Resource.Success(shop))
                } ?: emit(Resource.Error("Empty response body"))
            } else {
                emit(Resource.Error("Failed to get shop: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.localizedMessage}"))
        }
    }
    
    suspend fun updateShop(token: String, request: ShopUpdateRequest): Flow<Resource<Shop>> = flow {
        try {
            emit(Resource.Loading())
            val response = sellerApiService.updateShop("Bearer $token", request)
            if (response.isSuccessful) {
                response.body()?.let { shop ->
                    emit(Resource.Success(shop))
                } ?: emit(Resource.Error("Empty response body"))
            } else {
                emit(Resource.Error("Shop update failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.localizedMessage}"))
        }
    }
    
    suspend fun addProduct(token: String, request: ShopInventoryCreateRequest): Flow<Resource<ShopInventory>> = flow {
        try {
            emit(Resource.Loading())
            val response = sellerApiService.addProduct("Bearer $token", request)
            if (response.isSuccessful) {
                response.body()?.let { product ->
                    emit(Resource.Success(product))
                } ?: emit(Resource.Error("Empty response body"))
            } else {
                emit(Resource.Error("Product addition failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.localizedMessage}"))
        }
    }
    
    suspend fun getMyProducts(token: String, page: Int = 1, pageSize: Int = 20): Flow<Resource<List<ShopInventory>>> = flow {
        try {
            emit(Resource.Loading())
            val response = sellerApiService.getMyProducts("Bearer $token", page, pageSize)
            if (response.isSuccessful) {
                response.body()?.let { products ->
                    emit(Resource.Success(products))
                } ?: emit(Resource.Error("Empty response body"))
            } else {
                emit(Resource.Error("Failed to get products: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.localizedMessage}"))
        }
    }
    
    suspend fun updateProduct(token: String, productId: Int, request: ShopInventoryUpdateRequest): Flow<Resource<ShopInventory>> = flow {
        try {
            emit(Resource.Loading())
            val response = sellerApiService.updateProduct("Bearer $token", productId, request)
            if (response.isSuccessful) {
                response.body()?.let { product ->
                    emit(Resource.Success(product))
                } ?: emit(Resource.Error("Empty response body"))
            } else {
                emit(Resource.Error("Product update failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.localizedMessage}"))
        }
    }
    
    suspend fun deleteProduct(token: String, productId: Int): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            val response = sellerApiService.deleteProduct("Bearer $token", productId)
            if (response.isSuccessful) {
                emit(Resource.Success(Unit))
            } else {
                emit(Resource.Error("Product deletion failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.localizedMessage}"))
        }
    }
    
    suspend fun uploadImage(token: String, image: MultipartBody.Part, type: String): Flow<Resource<Map<String, String>>> = flow {
        try {
            emit(Resource.Loading())
            val response = sellerApiService.uploadImage("Bearer $token", image, type)
            if (response.isSuccessful) {
                response.body()?.let { result ->
                    emit(Resource.Success(result))
                } ?: emit(Resource.Error("Empty response body"))
            } else {
                emit(Resource.Error("Image upload failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.localizedMessage}"))
        }
    }
}