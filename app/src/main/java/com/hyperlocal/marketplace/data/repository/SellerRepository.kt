package com.hyperlocal.marketplace.data.repository

import com.hyperlocal.marketplace.data.api.SellerApiService
import com.hyperlocal.marketplace.data.models.*
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SellerRepository @Inject constructor(
    private val sellerApiService: SellerApiService
) {
    
    suspend fun getMyShop(token: String): Response<ApiResponse<Shop>> {
        return sellerApiService.getMyShop("Bearer $token")
    }
    
    suspend fun createShop(token: String, request: ShopCreateRequest): Response<ApiResponse<Shop>> {
        return sellerApiService.createShop("Bearer $token", request)
    }
    
    suspend fun updateMyShop(token: String, request: ShopUpdateRequest): Response<ApiResponse<Shop>> {
        return sellerApiService.updateMyShop("Bearer $token", request)
    }
    
    suspend fun getShopById(shopId: Int): Response<ApiResponse<Shop>> {
        return sellerApiService.getShopById(shopId)
    }
    
    suspend fun uploadShopImage(token: String, fieldName: String): Response<ApiResponse<ImageUploadResponse>> {
        return sellerApiService.uploadShopImage("Bearer $token", fieldName)
    }
    
    suspend fun getVendorProducts(token: String): Response<ApiResponse<List<Product>>> {
        return sellerApiService.getVendorProducts("Bearer $token")
    }
    
    suspend fun addProductFromCatalog(token: String, request: AddProductFromCatalogRequest): Response<ApiResponse<Product>> {
        return sellerApiService.addProductFromCatalog("Bearer $token", request)
    }
    
    suspend fun updateInventoryItem(token: String, itemId: Int, request: Map<String, Any>): Response<ApiResponse<Product>> {
        return sellerApiService.updateInventoryItem("Bearer $token", itemId, request)
    }
    
    suspend fun deleteInventoryItem(token: String, itemId: Int): Response<ApiResponse<Any>> {
        return sellerApiService.deleteInventoryItem("Bearer $token", itemId)
    }
}