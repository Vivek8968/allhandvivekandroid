package com.hyperlocal.marketplace.presentation.screens.seller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyperlocal.marketplace.data.models.Product
import com.hyperlocal.marketplace.data.models.Shop
import com.hyperlocal.marketplace.data.models.ShopCreateRequest
import com.hyperlocal.marketplace.data.repository.AuthRepository
import com.hyperlocal.marketplace.data.repository.SellerRepository
import com.hyperlocal.marketplace.data.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SellerViewModel @Inject constructor(
    private val sellerRepository: SellerRepository,
    private val authRepository: AuthRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(SellerUiState())
    val uiState: StateFlow<SellerUiState> = _uiState.asStateFlow()
    
    fun loadSellerShop() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, error = "") }
                
                val token = userPreferencesRepository.authToken.first()
                if (token != null) {
                    val shopResponse = sellerRepository.getMyShop(token)
                    if (shopResponse.isSuccessful && shopResponse.body() != null) {
                        val shop = shopResponse.body()!!.data
                        if (shop != null) {
                            // Load products for this shop
                            val productsResponse = sellerRepository.getVendorProducts(token)
                            if (productsResponse.isSuccessful && productsResponse.body() != null) {
                                val products = productsResponse.body()!!.data ?: emptyList()
                                _uiState.update { it.copy(
                                    isLoading = false,
                                    shop = shop,
                                    products = products
                                ) }
                            } else {
                                _uiState.update { it.copy(
                                    isLoading = false,
                                    shop = shop,
                                    error = "Failed to load products"
                                ) }
                            }
                        } else {
                            _uiState.update { it.copy(
                                isLoading = false,
                                shop = null
                            ) }
                        }
                    } else {
                        _uiState.update { it.copy(
                            isLoading = false,
                            shop = null
                        ) }
                    }
                } else {
                    _uiState.update { it.copy(
                        isLoading = false,
                        error = "Not authenticated"
                    ) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load shop"
                ) }
            }
        }
    }
    
    fun createShop(name: String, description: String, address: String, category: String) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, error = "") }
                
                val token = userPreferencesRepository.authToken.first()
                if (token != null) {
                    // For simplicity, using fixed location coordinates
                    val request = ShopCreateRequest(
                        name = name,
                        description = description,
                        address = address,
                        latitude = 28.6139, // Default location (can be replaced with actual location)
                        longitude = 77.2090
                    )
                    
                    val response = sellerRepository.createShop(token, request)
                    if (response.isSuccessful && response.body() != null) {
                        val shop = response.body()!!.data
                        if (shop != null) {
                            _uiState.update { it.copy(
                                isLoading = false,
                                shop = shop
                            ) }
                        } else {
                            _uiState.update { it.copy(
                                isLoading = false,
                                error = "Failed to create shop"
                            ) }
                        }
                    } else {
                        _uiState.update { it.copy(
                            isLoading = false,
                            error = response.errorBody()?.string() ?: "Failed to create shop"
                        ) }
                    }
                } else {
                    _uiState.update { it.copy(
                        isLoading = false,
                        error = "Not authenticated"
                    ) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to create shop"
                ) }
            }
        }
    }
    
    fun addProduct(
        name: String,
        description: String,
        price: Double,
        category: String,
        quantity: Int,
        unit: String
    ) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, error = "") }
                
                val token = userPreferencesRepository.authToken.first()
                if (token != null) {
                    val shopId = _uiState.value.shop?.id
                    if (shopId != null) {
                        // TODO: API only supports adding products from catalog, not creating new products
                        // Need to implement product creation API endpoint or use catalog-based approach
                        val request = com.hyperlocal.marketplace.data.models.AddProductFromCatalogRequest(
                            catalogItemId = 1, // TODO: Get from catalog selection
                            quantity = quantity
                        )
                        
                        val response = sellerRepository.addProductFromCatalog(token, request)
                        if (response.isSuccessful && response.body() != null) {
                            // Reload products
                            loadSellerShop()
                        } else {
                            _uiState.update { it.copy(
                                isLoading = false,
                                error = response.errorBody()?.string() ?: "Failed to add product"
                            ) }
                        }
                    } else {
                        _uiState.update { it.copy(
                            isLoading = false,
                            error = "Shop not found"
                        ) }
                    }
                } else {
                    _uiState.update { it.copy(
                        isLoading = false,
                        error = "Not authenticated"
                    ) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to add product"
                ) }
            }
        }
    }
    
    fun updateProduct(
        productId: String,
        name: String,
        description: String,
        price: Double,
        category: String,
        quantity: Int,
        unit: String,
        inStock: Boolean
    ) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, error = "") }
                
                val token = userPreferencesRepository.authToken.first()
                if (token != null) {
                    val shopId = _uiState.value.shop?.id
                    if (shopId != null) {
                        val request = mapOf(
                            "name" to name,
                            "description" to description,
                            "price" to price,
                            "category" to category,
                            "quantity" to quantity,
                            "unit" to unit,
                            "inStock" to inStock
                        )
                        
                        val response = sellerRepository.updateInventoryItem(token, productId.toInt(), request)
                        if (response.isSuccessful) {
                            // Reload products
                            loadSellerShop()
                        } else {
                            _uiState.update { it.copy(
                                isLoading = false,
                                error = response.errorBody()?.string() ?: "Failed to update product"
                            ) }
                        }
                    } else {
                        _uiState.update { it.copy(
                            isLoading = false,
                            error = "Shop not found"
                        ) }
                    }
                } else {
                    _uiState.update { it.copy(
                        isLoading = false,
                        error = "Not authenticated"
                    ) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to update product"
                ) }
            }
        }
    }
    
    fun deleteProduct(productId: String) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, error = "") }
                
                val token = userPreferencesRepository.authToken.first()
                if (token != null) {
                    val shopId = _uiState.value.shop?.id
                    if (shopId != null) {
                        val response = sellerRepository.deleteInventoryItem(token, productId.toInt())
                        if (response.isSuccessful) {
                            // Reload products
                            loadSellerShop()
                        } else {
                            _uiState.update { it.copy(
                                isLoading = false,
                                error = response.errorBody()?.string() ?: "Failed to delete product"
                            ) }
                        }
                    } else {
                        _uiState.update { it.copy(
                            isLoading = false,
                            error = "Shop not found"
                        ) }
                    }
                } else {
                    _uiState.update { it.copy(
                        isLoading = false,
                        error = "Not authenticated"
                    ) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to delete product"
                ) }
            }
        }
    }
}

data class SellerUiState(
    val isLoading: Boolean = false,
    val shop: Shop? = null,
    val products: List<Product> = emptyList(),
    val error: String = ""
)