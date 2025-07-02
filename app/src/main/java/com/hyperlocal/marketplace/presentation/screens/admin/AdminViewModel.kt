package com.hyperlocal.marketplace.presentation.screens.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyperlocal.marketplace.data.models.Shop
import com.hyperlocal.marketplace.data.models.User
import com.hyperlocal.marketplace.data.repository.AdminRepository
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
class AdminViewModel @Inject constructor(
    private val adminRepository: AdminRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(AdminUiState())
    val uiState: StateFlow<AdminUiState> = _uiState.asStateFlow()
    
    fun loadAdminData() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, error = "") }
                
                val token = userPreferencesRepository.authToken.first()
                if (token != null) {
                    // Load shops
                    val shopsResponse = adminRepository.getAllShops("Bearer $token")
                    if (shopsResponse.isSuccessful && shopsResponse.body() != null) {
                        val shops = shopsResponse.body()!!.data ?: emptyList()
                        
                        // Load users
                        val usersResponse = adminRepository.getAllUsers("Bearer $token")
                        if (usersResponse.isSuccessful && usersResponse.body() != null) {
                            val users = usersResponse.body()!!.data ?: emptyList()
                            
                            // Load stats
                            val statsResponse = adminRepository.getStats("Bearer $token")
                            if (statsResponse.isSuccessful && statsResponse.body() != null) {
                                val stats = statsResponse.body()!!.data ?: emptyMap()
                                
                                _uiState.update { it.copy(
                                    isLoading = false,
                                    shops = shops,
                                    users = users,
                                    stats = stats
                                ) }
                            } else {
                                _uiState.update { it.copy(
                                    isLoading = false,
                                    shops = shops,
                                    users = users,
                                    error = "Failed to load statistics"
                                ) }
                            }
                        } else {
                            _uiState.update { it.copy(
                                isLoading = false,
                                shops = shops,
                                error = "Failed to load users"
                            ) }
                        }
                    } else {
                        _uiState.update { it.copy(
                            isLoading = false,
                            error = "Failed to load shops"
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
                    error = e.message ?: "Failed to load admin data"
                ) }
            }
        }
    }
    
    fun approveShop(shopId: String) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, error = "") }
                
                val token = userPreferencesRepository.authToken.first()
                if (token != null) {
                    val response = adminRepository.approveShop("Bearer $token", shopId)
                    if (response.isSuccessful) {
                        // Reload data
                        loadAdminData()
                    } else {
                        _uiState.update { it.copy(
                            isLoading = false,
                            error = response.errorBody()?.string() ?: "Failed to approve shop"
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
                    error = e.message ?: "Failed to approve shop"
                ) }
            }
        }
    }
    
    fun rejectShop(shopId: String) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, error = "") }
                
                val token = userPreferencesRepository.authToken.first()
                if (token != null) {
                    val response = adminRepository.rejectShop("Bearer $token", shopId)
                    if (response.isSuccessful) {
                        // Reload data
                        loadAdminData()
                    } else {
                        _uiState.update { it.copy(
                            isLoading = false,
                            error = response.errorBody()?.string() ?: "Failed to reject shop"
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
                    error = e.message ?: "Failed to reject shop"
                ) }
            }
        }
    }
    
    fun deleteShop(shopId: String) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, error = "") }
                
                val token = userPreferencesRepository.authToken.first()
                if (token != null) {
                    val response = adminRepository.deleteShop("Bearer $token", shopId)
                    if (response.isSuccessful) {
                        // Reload data
                        loadAdminData()
                    } else {
                        _uiState.update { it.copy(
                            isLoading = false,
                            error = response.errorBody()?.string() ?: "Failed to delete shop"
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
                    error = e.message ?: "Failed to delete shop"
                ) }
            }
        }
    }
    
    fun deleteUser(userId: Int) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, error = "") }
                
                val token = userPreferencesRepository.authToken.first()
                if (token != null) {
                    val response = adminRepository.deleteUser("Bearer $token", userId)
                    if (response.isSuccessful) {
                        // Reload data
                        loadAdminData()
                    } else {
                        _uiState.update { it.copy(
                            isLoading = false,
                            error = response.errorBody()?.string() ?: "Failed to delete user"
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
                    error = e.message ?: "Failed to delete user"
                ) }
            }
        }
    }
}

data class AdminUiState(
    val isLoading: Boolean = false,
    val shops: List<Shop> = emptyList(),
    val users: List<User> = emptyList(),
    val stats: Map<String, Int> = emptyMap(),
    val error: String = ""
)