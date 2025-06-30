package com.hyperlocal.marketplace.presentation.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyperlocal.marketplace.config.Config
import com.hyperlocal.marketplace.data.models.UserRole
import com.hyperlocal.marketplace.data.repository.UserRepository
import com.hyperlocal.marketplace.utils.PreferencesManager
import com.hyperlocal.marketplace.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val isLoading: Boolean = false,
    val selectedRole: UserRole? = null,
    val phoneNumber: String = "",
    val phoneError: String? = null,
    val errorMessage: String? = null,
    val isLoginSuccessful: Boolean = false,
    val userRole: UserRole? = null,
    val showDemoMode: Boolean = Config.IS_DEBUG
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()
    
    fun selectRole(role: UserRole) {
        _uiState.value = _uiState.value.copy(
            selectedRole = role,
            errorMessage = null
        )
    }
    
    fun updatePhoneNumber(phone: String) {
        _uiState.value = _uiState.value.copy(
            phoneNumber = phone,
            phoneError = null,
            errorMessage = null
        )
    }
    
    fun loginWithPhone(context: Context) {
        val currentState = _uiState.value
        
        if (currentState.selectedRole == null) {
            _uiState.value = currentState.copy(
                errorMessage = "Please select your role first"
            )
            return
        }
        
        if (!isValidPhoneNumber(currentState.phoneNumber)) {
            _uiState.value = currentState.copy(
                phoneError = "Please enter a valid phone number"
            )
            return
        }
        
        _uiState.value = currentState.copy(
            isLoading = true,
            errorMessage = null,
            phoneError = null
        )
        
        // TODO: Implement Firebase Phone Authentication
        // For now, simulate authentication
        viewModelScope.launch {
            try {
                kotlinx.coroutines.delay(2000) // Simulate network call
                
                // Simulate successful authentication
                val mockFirebaseToken = "mock_firebase_token_${System.currentTimeMillis()}"
                
                // Save user data
                preferencesManager.saveAuthToken(mockFirebaseToken)
                preferencesManager.saveUserData(
                    userId = 1,
                    role = currentState.selectedRole!!,
                    name = "Demo User",
                    email = "demo@example.com",
                    phone = currentState.phoneNumber
                )
                
                _uiState.value = currentState.copy(
                    isLoading = false,
                    isLoginSuccessful = true,
                    userRole = currentState.selectedRole
                )
                
            } catch (e: Exception) {
                _uiState.value = currentState.copy(
                    isLoading = false,
                    errorMessage = "Authentication failed: ${e.localizedMessage}"
                )
            }
        }
    }
    
    fun loginWithGoogle(context: Context) {
        val currentState = _uiState.value
        
        if (currentState.selectedRole == null) {
            _uiState.value = currentState.copy(
                errorMessage = "Please select your role first"
            )
            return
        }
        
        _uiState.value = currentState.copy(
            isLoading = true,
            errorMessage = null
        )
        
        // TODO: Implement Firebase Google Authentication
        // For now, simulate authentication
        viewModelScope.launch {
            try {
                kotlinx.coroutines.delay(2000) // Simulate network call
                
                // Simulate successful authentication
                val mockFirebaseToken = "mock_google_token_${System.currentTimeMillis()}"
                
                // Save user data
                preferencesManager.saveAuthToken(mockFirebaseToken)
                preferencesManager.saveUserData(
                    userId = 1,
                    role = currentState.selectedRole!!,
                    name = "Google User",
                    email = "google@example.com",
                    phone = null
                )
                
                _uiState.value = currentState.copy(
                    isLoading = false,
                    isLoginSuccessful = true,
                    userRole = currentState.selectedRole
                )
                
            } catch (e: Exception) {
                _uiState.value = currentState.copy(
                    isLoading = false,
                    errorMessage = "Google authentication failed: ${e.localizedMessage}"
                )
            }
        }
    }
    
    fun enterDemoMode() {
        val currentState = _uiState.value
        val demoRole = currentState.selectedRole ?: UserRole.CUSTOMER
        
        viewModelScope.launch {
            // Save demo user data
            preferencesManager.saveAuthToken("demo_token")
            preferencesManager.saveUserData(
                userId = 999,
                role = demoRole,
                name = "Demo ${demoRole.name.lowercase().replaceFirstChar { it.uppercase() }}",
                email = "demo@hyperlocal.com",
                phone = when (demoRole) {
                    UserRole.CUSTOMER -> Config.Demo.DEMO_CUSTOMER_PHONE
                    UserRole.SELLER -> Config.Demo.DEMO_SELLER_PHONE
                    UserRole.ADMIN -> Config.Demo.DEMO_ADMIN_PHONE
                }
            )
            
            _uiState.value = currentState.copy(
                isLoginSuccessful = true,
                userRole = demoRole
            )
        }
    }
    
    private fun isValidPhoneNumber(phone: String): Boolean {
        // Basic phone number validation
        val cleanPhone = phone.replace(Regex("[^+\\d]"), "")
        return cleanPhone.length >= 10 && cleanPhone.startsWith("+")
    }
}