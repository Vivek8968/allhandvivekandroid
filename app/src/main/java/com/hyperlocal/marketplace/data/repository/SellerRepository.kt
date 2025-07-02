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
    
    suspend fun getSellerShop(token: String): Response<ApiResponse<Shop>> {
        return sellerApiService.getVendorShop(token)
    }
    
    suspend fun createShop(token: String, request: ShopCreateRequest): Response<ApiResponse<Shop>> {
        return sellerApiService.createVendorShop(token, request)
    }
    
    suspend fun updateShop(token: String, request: ShopUpdateRequest): Response<ApiResponse<Shop>> {
        return sellerApiService.updateVendorShop(token, request)
    }
    
    suspend fun getSellerProducts(token: String): Response<ApiResponse<List<Product>>> {
        return sellerApiService.getVendorProducts(token)
    }
    
    suspend fun addProduct(token: String, shopId: String, request: ProductCreateRequest): Response<ApiResponse<Product>> {
        return sellerApiService.addProductToShop(token, shopId, request)
    }
    
    suspend fun updateProduct(token: String, shopId: String, productId: String, request: Map<String, Any>): Response<ApiResponse<Product>> {
        return sellerApiService.updateShopProduct(token, shopId, productId, request)
    }
    
    suspend fun deleteProduct(token: String, shopId: String, productId: String): Response<ApiResponse<Any>> {
        return sellerApiService.deleteShopProduct(token, shopId, productId)
    }
    
    suspend fun addProductFromCatalog(token: String, request: AddProductFromCatalogRequest): Response<ApiResponse<Product>> {
        return sellerApiService.addProductFromCatalog(token, request)
    }
}