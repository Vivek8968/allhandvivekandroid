package com.hyperlocal.marketplace.presentation.customer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyperlocal.marketplace.config.Config
import com.hyperlocal.marketplace.data.models.Shop
import com.hyperlocal.marketplace.data.repository.CustomerRepository
import com.hyperlocal.marketplace.utils.PreferencesManager
import com.hyperlocal.marketplace.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CustomerUiState(
    val isLoading: Boolean = false,
    val shops: List<Shop> = emptyList(),
    val currentLocation: Pair<Double, Double>? = null,
    val searchRadius: Double = Config.App.DEFAULT_SEARCH_RADIUS_KM,
    val errorMessage: String? = null,
    val selectedShop: Shop? = null
)

@HiltViewModel
class CustomerViewModel @Inject constructor(
    private val customerRepository: CustomerRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(CustomerUiState())
    val uiState: StateFlow<CustomerUiState> = _uiState.asStateFlow()
    
    init {
        loadUserLocation()
        loadSearchRadius()
    }
    
    private fun loadUserLocation() {
        viewModelScope.launch {
            preferencesManager.lastLocation.collect { location ->
                _uiState.value = _uiState.value.copy(
                    currentLocation = location ?: Pair(Config.Demo.DEMO_LATITUDE, Config.Demo.DEMO_LONGITUDE)
                )
            }
        }
    }
    
    private fun loadSearchRadius() {
        viewModelScope.launch {
            preferencesManager.searchRadius.collect { radius ->
                _uiState.value = _uiState.value.copy(
                    searchRadius = radius
                )
            }
        }
    }
    
    fun loadNearbyShops() {
        val currentState = _uiState.value
        val location = currentState.currentLocation
        
        if (location == null) {
            _uiState.value = currentState.copy(
                errorMessage = "Location not available. Please enable location services."
            )
            return
        }
        
        viewModelScope.launch {
            preferencesManager.authToken.first()?.let { token ->
                customerRepository.getNearbyShops(
                    token = token,
                    latitude = location.first,
                    longitude = location.second,
                    radius = currentState.searchRadius
                ).collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            _uiState.value = currentState.copy(
                                isLoading = true,
                                errorMessage = null
                            )
                        }
                        is Resource.Success -> {
                            _uiState.value = currentState.copy(
                                isLoading = false,
                                shops = resource.data?.shops ?: emptyList(),
                                errorMessage = null
                            )
                        }
                        is Resource.Error -> {
                            _uiState.value = currentState.copy(
                                isLoading = false,
                                errorMessage = resource.message ?: "Failed to load shops"
                            )
                            
                            // Load demo data in debug mode
                            if (Config.IS_DEBUG) {
                                loadDemoShops()
                            }
                        }
                    }
                }
            } ?: run {
                _uiState.value = currentState.copy(
                    errorMessage = "Authentication required"
                )
            }
        }
    }
    
    private fun loadDemoShops() {
        val demoShops = listOf(
            Shop(
                id = 1,
                userId = 1,
                name = "Electronics Hub",
                description = "Latest smartphones, laptops, and accessories",
                whatsappNumber = "+919999999999",
                address = "123 Main Street, Tech City",
                latitude = Config.Demo.DEMO_LATITUDE + 0.001,
                longitude = Config.Demo.DEMO_LONGITUDE + 0.001,
                imageUrl = null,
                bannerUrl = null,
                isApproved = true,
                createdAt = "2024-01-01T00:00:00Z",
                updatedAt = "2024-01-01T00:00:00Z",
                distance = 0.2
            ),
            Shop(
                id = 2,
                userId = 2,
                name = "Mobile World",
                description = "Best deals on mobile phones and repairs",
                whatsappNumber = "+918888888888",
                address = "456 Tech Avenue, Digital Plaza",
                latitude = Config.Demo.DEMO_LATITUDE + 0.002,
                longitude = Config.Demo.DEMO_LONGITUDE - 0.001,
                imageUrl = null,
                bannerUrl = null,
                isApproved = true,
                createdAt = "2024-01-01T00:00:00Z",
                updatedAt = "2024-01-01T00:00:00Z",
                distance = 0.5
            ),
            Shop(
                id = 3,
                userId = 3,
                name = "Gadget Store",
                description = "Computers, gaming, and smart home devices",
                whatsappNumber = "+917777777777",
                address = "789 Innovation Road, Smart City",
                latitude = Config.Demo.DEMO_LATITUDE - 0.001,
                longitude = Config.Demo.DEMO_LONGITUDE + 0.002,
                imageUrl = null,
                bannerUrl = null,
                isApproved = true,
                createdAt = "2024-01-01T00:00:00Z",
                updatedAt = "2024-01-01T00:00:00Z",
                distance = 0.8
            )
        )
        
        _uiState.value = _uiState.value.copy(
            isLoading = false,
            shops = demoShops,
            errorMessage = null
        )
    }
    
    fun refreshLocation() {
        // TODO: Implement location refresh
        viewModelScope.launch {
            // For demo, use current location or demo location
            val demoLocation = Pair(Config.Demo.DEMO_LATITUDE, Config.Demo.DEMO_LONGITUDE)
            preferencesManager.saveLocation(demoLocation.first, demoLocation.second)
            loadNearbyShops()
        }
    }
    
    fun refreshShops() {
        loadNearbyShops()
    }
    
    fun openShop(shop: Shop) {
        _uiState.value = _uiState.value.copy(
            selectedShop = shop
        )
        // TODO: Navigate to shop details screen
    }
    
    fun updateSearchRadius(radius: Double) {
        viewModelScope.launch {
            preferencesManager.saveSearchRadius(radius)
            loadNearbyShops()
        }
    }
}