package com.hyperlocal.marketplace.presentation.screens.auth

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.hyperlocal.marketplace.data.models.UserRole
import com.hyperlocal.marketplace.data.repository.AuthRepository
import com.hyperlocal.marketplace.data.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()
    
    private var verificationId: String? = null
    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null
    
    init {
        checkLoginStatus()
    }
    
    private fun checkLoginStatus() {
        viewModelScope.launch {
            try {
                val isLoggedIn = userPreferencesRepository.isLoggedIn.first()
                if (isLoggedIn) {
                    val userRole = userPreferencesRepository.userRole.first()
                    val userName = userPreferencesRepository.userName.first()
                    val userEmail = userPreferencesRepository.userEmail.first()
                    val userPhone = userPreferencesRepository.userPhone.first()
                    
                    _uiState.update { it.copy(
                        isLoggedIn = true,
                        userRole = userRole?.name,
                        userName = userName,
                        userEmail = userEmail,
                        userPhone = userPhone
                    ) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message ?: "Failed to check login status") }
            }
        }
    }
    
    // Phone Authentication
    fun sendPhoneVerificationCode(phoneNumber: String) {
        _uiState.update { it.copy(isLoading = true, error = "") }
        
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                signInWithPhoneAuthCredential(credential)
            }
            
            override fun onVerificationFailed(e: FirebaseException) {
                _uiState.update { it.copy(
                    isLoading = false,
                    error = e.message ?: "Verification failed"
                ) }
            }
            
            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                this@AuthViewModel.verificationId = verificationId
                this@AuthViewModel.resendToken = token
                _uiState.update { it.copy(isLoading = false) }
            }
        }
        
        val options = PhoneAuthOptions.newBuilder()
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setCallbacks(callbacks)
            .build()
        
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
    
    fun verifyPhoneCode(code: String) {
        _uiState.update { it.copy(isLoading = true, error = "") }
        
        val credential = PhoneAuthProvider.getCredential(verificationId ?: "", code)
        signInWithPhoneAuthCredential(credential)
    }
    
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        viewModelScope.launch {
            try {
                val user = authRepository.signInWithPhoneAuthCredential(credential)
                if (user != null) {
                    // Get Firebase token
                    val firebaseToken = authRepository.getIdToken()
                    if (firebaseToken != null) {
                        // Login with backend
                        val response = authRepository.loginWithFirebaseToken(firebaseToken)
                        if (response.isSuccessful && response.body() != null) {
                            val data = response.body()!!.data
                            if (data != null) {
                                // Save user data
                                authRepository.saveUserData(data.token, data.user)
                                _uiState.update { it.copy(
                                    isLoading = false,
                                    isLoggedIn = true,
                                    userRole = data.user.role.name,
                                    userName = data.user.name,
                                    userEmail = data.user.email,
                                    userPhone = data.user.phone
                                ) }
                            } else {
                                _uiState.update { it.copy(
                                    isLoading = false,
                                    error = "Failed to get user data"
                                ) }
                            }
                        } else {
                            _uiState.update { it.copy(
                                isLoading = false,
                                error = response.errorBody()?.string() ?: "Login failed"
                            ) }
                        }
                    } else {
                        _uiState.update { it.copy(
                            isLoading = false,
                            error = "Failed to get Firebase token"
                        ) }
                    }
                } else {
                    _uiState.update { it.copy(
                        isLoading = false,
                        error = "Authentication failed"
                    ) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    isLoading = false,
                    error = e.message ?: "Authentication failed"
                ) }
            }
        }
    }
    
    // Email/Password Authentication
    fun loginWithEmailPassword(email: String, password: String) {
        _uiState.update { it.copy(isLoading = true, error = "") }
        
        viewModelScope.launch {
            try {
                val user = authRepository.signInWithEmailAndPassword(email, password)
                if (user != null) {
                    // Get Firebase token
                    val firebaseToken = authRepository.getIdToken()
                    if (firebaseToken != null) {
                        // Login with backend
                        val response = authRepository.loginWithFirebaseToken(firebaseToken)
                        if (response.isSuccessful && response.body() != null) {
                            val data = response.body()!!.data
                            if (data != null) {
                                // Save user data
                                authRepository.saveUserData(data.token, data.user)
                                _uiState.update { it.copy(
                                    isLoading = false,
                                    isLoggedIn = true,
                                    userRole = data.user.role.name,
                                    userName = data.user.name,
                                    userEmail = data.user.email,
                                    userPhone = data.user.phone
                                ) }
                            } else {
                                _uiState.update { it.copy(
                                    isLoading = false,
                                    error = "Failed to get user data"
                                ) }
                            }
                        } else {
                            _uiState.update { it.copy(
                                isLoading = false,
                                error = response.errorBody()?.string() ?: "Login failed"
                            ) }
                        }
                    } else {
                        _uiState.update { it.copy(
                            isLoading = false,
                            error = "Failed to get Firebase token"
                        ) }
                    }
                } else {
                    _uiState.update { it.copy(
                        isLoading = false,
                        error = "Authentication failed"
                    ) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    isLoading = false,
                    error = e.message ?: "Authentication failed"
                ) }
            }
        }
    }
    
    fun registerWithEmailPassword(name: String, email: String, phone: String, password: String) {
        _uiState.update { it.copy(isLoading = true, error = "") }
        
        viewModelScope.launch {
            try {
                val user = authRepository.createUserWithEmailAndPassword(email, password)
                if (user != null) {
                    // Register with backend
                    val response = authRepository.registerUser(name, email, phone, UserRole.CUSTOMER)
                    if (response.isSuccessful && response.body() != null) {
                        _uiState.update { it.copy(
                            isLoading = false,
                            isRegistered = true
                        ) }
                    } else {
                        _uiState.update { it.copy(
                            isLoading = false,
                            error = response.errorBody()?.string() ?: "Registration failed"
                        ) }
                    }
                } else {
                    _uiState.update { it.copy(
                        isLoading = false,
                        error = "Registration failed"
                    ) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    isLoading = false,
                    error = e.message ?: "Registration failed"
                ) }
            }
        }
    }
    
    fun updateUserRole(role: UserRole) {
        _uiState.update { it.copy(isLoading = true, error = "") }
        
        viewModelScope.launch {
            try {
                userPreferencesRepository.saveUserRole(role)
                _uiState.update { it.copy(
                    isLoading = false,
                    userRole = role.name
                ) }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to update role"
                ) }
            }
        }
    }
    
    fun signOut() {
        viewModelScope.launch {
            try {
                authRepository.signOut()
                _uiState.update { it.copy(
                    isLoggedIn = false,
                    userRole = null
                ) }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    error = e.message ?: "Failed to sign out"
                ) }
            }
        }
    }
}

data class AuthUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val isRegistered: Boolean = false,
    val userRole: String? = null,
    val userName: String? = null,
    val userEmail: String? = null,
    val userPhone: String? = null,
    val error: String = ""
)