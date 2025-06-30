package com.hyperlocal.marketplace.presentation.seller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyperlocal.marketplace.config.Config
import com.hyperlocal.marketplace.data.models.Shop
import com.hyperlocal.marketplace.data.models.ShopInventory
import com.hyperlocal.marketplace.data.models.CatalogItem
import com.hyperlocal.marketplace.data.repository.SellerRepository
import com.hyperlocal.marketplace.utils.PreferencesManager
import com.hyperlocal.marketplace.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SellerUiState(
    val isLoading: Boolean = false,
    val shop: Shop? = null,
    val products: List<ShopInventory> = emptyList(),
    val errorMessage: String? = null,
    val showShopRegistrationDialog: Boolean = false,
    val showEditShopDialog: Boolean = false,
    val showAddProductDialog: Boolean = false,
    val selectedProduct: ShopInventory? = null
)

@HiltViewModel
class SellerViewModel @Inject constructor(
    private val sellerRepository: SellerRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(SellerUiState())
    val uiState: StateFlow<SellerUiState> = _uiState.asStateFlow()
    
    fun loadSellerData() {
        viewModelScope.launch {
            preferencesManager.authToken.first()?.let { token ->
                _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
                
                // Load shop data
                sellerRepository.getMyShop(token).collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            // Keep loading state
                        }
                        is Resource.Success -> {
                            _uiState.value = _uiState.value.copy(
                                shop = resource.data,
                                isLoading = false
                            )
                            // Load products if shop exists
                            resource.data?.let { loadProducts(token) }
                        }
                        is Resource.Error -> {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                errorMessage = resource.message
                            )
                            
                            // Load demo data in debug mode
                            if (Config.IS_DEBUG) {
                                loadDemoData()
                            }
                        }
                    }
                }
            } ?: run {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Authentication required"
                )
            }
        }
    }
    
    private fun loadProducts(token: String) {
        viewModelScope.launch {
            sellerRepository.getMyProducts(token).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        // Products loading
                    }
                    is Resource.Success -> {
                        _uiState.value = _uiState.value.copy(
                            products = resource.data ?: emptyList()
                        )
                    }
                    is Resource.Error -> {
                        // Load demo products in debug mode
                        if (Config.IS_DEBUG) {
                            loadDemoProducts()
                        }
                    }
                }
            }
        }
    }
    
    private fun loadDemoData() {
        val demoShop = Shop(
            id = 1,
            userId = 1,
            name = "Demo Electronics Store",
            description = "Your one-stop shop for all electronic needs",
            whatsappNumber = Config.Demo.DEMO_SELLER_PHONE,
            address = "123 Demo Street, Tech City, 12345",
            latitude = Config.Demo.DEMO_LATITUDE,
            longitude = Config.Demo.DEMO_LONGITUDE,
            imageUrl = null,
            bannerUrl = null,
            isApproved = true,
            createdAt = "2024-01-01T00:00:00Z",
            updatedAt = "2024-01-01T00:00:00Z"
        )
        
        _uiState.value = _uiState.value.copy(
            isLoading = false,
            shop = demoShop,
            errorMessage = null
        )
        
        loadDemoProducts()
    }
    
    private fun loadDemoProducts() {
        val demoProducts = listOf(
            ShopInventory(
                id = 1,
                shopId = 1,
                catalogItemId = 1,
                catalogItem = CatalogItem(
                    id = 1,
                    name = "iPhone 15 Pro",
                    description = "Latest iPhone with advanced camera system",
                    category = "Electronics",
                    brand = "Apple",
                    model = "iPhone 15 Pro",
                    imageUrl = null,
                    specifications = null,
                    createdAt = "2024-01-01T00:00:00Z",
                    updatedAt = "2024-01-01T00:00:00Z"
                ),
                price = 129900.0,
                stock = 5,
                createdAt = "2024-01-01T00:00:00Z",
                updatedAt = "2024-01-01T00:00:00Z"
            ),
            ShopInventory(
                id = 2,
                shopId = 1,
                catalogItemId = 2,
                catalogItem = CatalogItem(
                    id = 2,
                    name = "Samsung Galaxy S24",
                    description = "Flagship Android phone with AI features",
                    category = "Electronics",
                    brand = "Samsung",
                    model = "Galaxy S24",
                    imageUrl = null,
                    specifications = null,
                    createdAt = "2024-01-01T00:00:00Z",
                    updatedAt = "2024-01-01T00:00:00Z"
                ),
                price = 89999.0,
                stock = 8,
                createdAt = "2024-01-01T00:00:00Z",
                updatedAt = "2024-01-01T00:00:00Z"
            ),
            ShopInventory(
                id = 3,
                shopId = 1,
                catalogItemId = 3,
                catalogItem = CatalogItem(
                    id = 3,
                    name = "MacBook Air M3",
                    description = "Lightweight laptop with M3 chip",
                    category = "Electronics",
                    brand = "Apple",
                    model = "MacBook Air M3",
                    imageUrl = null,
                    specifications = null,
                    createdAt = "2024-01-01T00:00:00Z",
                    updatedAt = "2024-01-01T00:00:00Z"
                ),
                price = 114900.0,
                stock = 3,
                createdAt = "2024-01-01T00:00:00Z",
                updatedAt = "2024-01-01T00:00:00Z"
            )
        )
        
        _uiState.value = _uiState.value.copy(
            products = demoProducts
        )
    }
    
    fun refreshData() {
        loadSellerData()
    }
    
    fun showShopRegistrationDialog() {
        _uiState.value = _uiState.value.copy(
            showShopRegistrationDialog = true
        )
    }
    
    fun hideShopRegistrationDialog() {
        _uiState.value = _uiState.value.copy(
            showShopRegistrationDialog = false
        )
    }
    
    fun showEditShopDialog() {
        _uiState.value = _uiState.value.copy(
            showEditShopDialog = true
        )
    }
    
    fun hideEditShopDialog() {
        _uiState.value = _uiState.value.copy(
            showEditShopDialog = false
        )
    }
    
    fun showAddProductDialog() {
        _uiState.value = _uiState.value.copy(
            showAddProductDialog = true,
            selectedProduct = null
        )
    }
    
    fun showEditProductDialog(product: ShopInventory) {
        _uiState.value = _uiState.value.copy(
            showAddProductDialog = true,
            selectedProduct = product
        )
    }
    
    fun hideProductDialog() {
        _uiState.value = _uiState.value.copy(
            showAddProductDialog = false,
            selectedProduct = null
        )
    }
    
    fun deleteProduct(productId: Int) {
        viewModelScope.launch {
            preferencesManager.authToken.first()?.let { token ->
                sellerRepository.deleteProduct(token, productId).collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            // Show loading if needed
                        }
                        is Resource.Success -> {
                            // Refresh products list
                            loadProducts(token)
                        }
                        is Resource.Error -> {
                            _uiState.value = _uiState.value.copy(
                                errorMessage = resource.message ?: "Failed to delete product"
                            )
                        }
                    }
                }
            }
        }
    }
}