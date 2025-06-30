package com.hyperlocal.marketplace.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyperlocal.marketplace.data.models.UserRole
import com.hyperlocal.marketplace.utils.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ViewModel() {
    
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    val isLoggedIn: StateFlow<Boolean> = preferencesManager.isLoggedIn
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )
    
    val userRole: StateFlow<UserRole?> = preferencesManager.userRole
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
    
    init {
        checkAuthState()
    }
    
    private fun checkAuthState() {
        viewModelScope.launch {
            // Simulate splash screen delay
            kotlinx.coroutines.delay(1500)
            _isLoading.value = false
        }
    }
    
    fun onLoginSuccess(role: UserRole) {
        viewModelScope.launch {
            // The login state will be automatically updated through PreferencesManager
            // This function can be used for any additional logic after login
        }
    }
    
    fun logout() {
        viewModelScope.launch {
            preferencesManager.clearUserData()
        }
    }
}