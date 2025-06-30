package com.hyperlocal.marketplace.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyperlocal.marketplace.config.Config
import com.hyperlocal.marketplace.data.models.*
import com.hyperlocal.marketplace.data.repository.AdminRepository
import com.hyperlocal.marketplace.utils.PreferencesManager
import com.hyperlocal.marketplace.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AdminUiState(
    val isLoading: Boolean = false,
    val stats: AdminStats? = null,
    val users: List<User> = emptyList(),
    val pendingShops: List<Shop> = emptyList(),
    val errorMessage: String? = null
)

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val adminRepository: AdminRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(AdminUiState())
    val uiState: StateFlow<AdminUiState> = _uiState.asStateFlow()
    
    fun loadAdminData() {
        viewModelScope.launch {
            preferencesManager.authToken.first()?.let { token ->
                _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
                
                // Load stats
                loadStats(token)
                // Load users
                loadUsers(token)
                // Load pending shops
                loadPendingShops(token)
                
            } ?: run {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Authentication required"
                )
            }
        }
    }
    
    private fun loadStats(token: String) {
        viewModelScope.launch {
            adminRepository.getStats(token).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        // Keep loading state
                    }
                    is Resource.Success -> {
                        _uiState.value = _uiState.value.copy(
                            stats = resource.data,
                            isLoading = false
                        )
                    }
                    is Resource.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = resource.message
                        )
                        
                        // Load demo data in debug mode
                        if (Config.IS_DEBUG) {
                            loadDemoStats()
                        }
                    }
                }
            }
        }
    }
    
    private fun loadUsers(token: String) {
        viewModelScope.launch {
            adminRepository.getUsers(token).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        // Users loading
                    }
                    is Resource.Success -> {
                        _uiState.value = _uiState.value.copy(
                            users = resource.data?.users ?: emptyList()
                        )
                    }
                    is Resource.Error -> {
                        // Load demo users in debug mode
                        if (Config.IS_DEBUG) {
                            loadDemoUsers()
                        }
                    }
                }
            }
        }
    }
    
    private fun loadPendingShops(token: String) {
        viewModelScope.launch {
            adminRepository.getShops(token, status = "pending").collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        // Shops loading
                    }
                    is Resource.Success -> {
                        _uiState.value = _uiState.value.copy(
                            pendingShops = resource.data?.shops ?: emptyList()
                        )
                    }
                    is Resource.Error -> {
                        // Load demo pending shops in debug mode
                        if (Config.IS_DEBUG) {
                            loadDemoPendingShops()
                        }
                    }
                }
            }
        }
    }
    
    private fun loadDemoStats() {
        val demoStats = AdminStats(
            totalUsers = 150,
            totalCustomers = 120,
            totalSellers = 30,
            totalShops = 45,
            approvedShops = 35,
            pendingShops = 8,
            totalProducts = 1250,
            activeProducts = 1200
        )
        
        _uiState.value = _uiState.value.copy(
            stats = demoStats,
            isLoading = false,
            errorMessage = null
        )
    }
    
    private fun loadDemoUsers() {
        val demoUsers = listOf(
            User(
                id = 1,
                firebaseUid = "demo_customer_1",
                name = "John Customer",
                email = "john@example.com",
                phone = "+919999999999",
                role = UserRole.CUSTOMER,
                createdAt = "2024-01-01T00:00:00Z",
                updatedAt = "2024-01-01T00:00:00Z"
            ),
            User(
                id = 2,
                firebaseUid = "demo_seller_1",
                name = "Jane Seller",
                email = "jane@example.com",
                phone = "+918888888888",
                role = UserRole.SELLER,
                createdAt = "2024-01-02T00:00:00Z",
                updatedAt = "2024-01-02T00:00:00Z"
            ),
            User(
                id = 3,
                firebaseUid = "demo_customer_2",
                name = "Bob Customer",
                email = "bob@example.com",
                phone = "+917777777777",
                role = UserRole.CUSTOMER,
                createdAt = "2024-01-03T00:00:00Z",
                updatedAt = "2024-01-03T00:00:00Z"
            )
        )
        
        _uiState.value = _uiState.value.copy(
            users = demoUsers
        )
    }
    
    private fun loadDemoPendingShops() {
        val demoPendingShops = listOf(
            Shop(
                id = 10,
                userId = 10,
                name = "New Electronics Store",
                description = "Latest gadgets and electronics",
                whatsappNumber = "+916666666666",
                address = "456 New Street, Tech City",
                latitude = Config.Demo.DEMO_LATITUDE + 0.003,
                longitude = Config.Demo.DEMO_LONGITUDE + 0.003,
                imageUrl = null,
                bannerUrl = null,
                isApproved = false,
                createdAt = "2024-01-10T00:00:00Z",
                updatedAt = "2024-01-10T00:00:00Z"
            ),
            Shop(
                id = 11,
                userId = 11,
                name = "Fashion Boutique",
                description = "Trendy clothes and accessories",
                whatsappNumber = "+915555555555",
                address = "789 Fashion Avenue, Style City",
                latitude = Config.Demo.DEMO_LATITUDE - 0.002,
                longitude = Config.Demo.DEMO_LONGITUDE - 0.002,
                imageUrl = null,
                bannerUrl = null,
                isApproved = false,
                createdAt = "2024-01-11T00:00:00Z",
                updatedAt = "2024-01-11T00:00:00Z"
            )
        )
        
        _uiState.value = _uiState.value.copy(
            pendingShops = demoPendingShops
        )
    }
    
    fun refreshData() {
        loadAdminData()
    }
    
    fun approveShop(shopId: Int) {
        viewModelScope.launch {
            preferencesManager.authToken.first()?.let { token ->
                val request = ShopApprovalRequest(
                    isApproved = true,
                    rejectionReason = null
                )
                
                adminRepository.updateShopStatus(token, shopId, request).collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            // Show loading if needed
                        }
                        is Resource.Success -> {
                            // Refresh pending shops
                            loadPendingShops(token)
                            loadStats(token)
                        }
                        is Resource.Error -> {
                            _uiState.value = _uiState.value.copy(
                                errorMessage = resource.message ?: "Failed to approve shop"
                            )
                        }
                    }
                }
            }
        }
    }
    
    fun rejectShop(shopId: Int) {
        viewModelScope.launch {
            preferencesManager.authToken.first()?.let { token ->
                val request = ShopApprovalRequest(
                    isApproved = false,
                    rejectionReason = "Does not meet platform requirements"
                )
                
                adminRepository.updateShopStatus(token, shopId, request).collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            // Show loading if needed
                        }
                        is Resource.Success -> {
                            // Refresh pending shops
                            loadPendingShops(token)
                            loadStats(token)
                        }
                        is Resource.Error -> {
                            _uiState.value = _uiState.value.copy(
                                errorMessage = resource.message ?: "Failed to reject shop"
                            )
                        }
                    }
                }
            }
        }
    }
    
    fun deleteUser(userId: Int) {
        viewModelScope.launch {
            preferencesManager.authToken.first()?.let { token ->
                adminRepository.deleteUser(token, userId).collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            // Show loading if needed
                        }
                        is Resource.Success -> {
                            // Refresh users list
                            loadUsers(token)
                            loadStats(token)
                        }
                        is Resource.Error -> {
                            _uiState.value = _uiState.value.copy(
                                errorMessage = resource.message ?: "Failed to delete user"
                            )
                        }
                    }
                }
            }
        }
    }
}