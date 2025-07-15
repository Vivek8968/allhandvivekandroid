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
    private val userPreferencesRepository: UserPreferencesRepository,
    private val googleSignInHelper: GoogleSignInHelper
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
    fun sendPhoneVerificationCode(phoneNumber: String, activity: Activity) {
        _uiState.update { it.copy(isLoading = true, error = "") }
        
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                signInWithPhoneAuthCredential(credential)
            }
            
            override fun onVerificationFailed(e: FirebaseException) {
                val errorMessage = when {
                    e.message?.contains("billing_not_enabled") == true -> 
                        "Phone authentication is temporarily unavailable due to billing configuration. Please use email/password authentication instead."
                    e.message?.contains("invalid-phone-number") == true -> 
                        "Invalid phone number format. Please include country code (+1, +91, etc.)."
                    e.message?.contains("too-many-requests") == true -> 
                        "Too many requests. Please try again in a few minutes."
                    e.message?.contains("app-not-authorized") == true -> 
                        "App not authorized for phone authentication. Please contact support."
                    e.message?.contains("quota-exceeded") == true -> 
                        "SMS quota exceeded. Please try again later or use email authentication."
                    else -> "Phone verification failed: ${e.message ?: "Unknown error"}"
                }
                
                _uiState.update { it.copy(
                    isLoading = false,
                    error = errorMessage,
                    showAlternativeAuth = e.message?.contains("billing_not_enabled") == true
                ) }
            }
            
            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                this@AuthViewModel.verificationId = verificationId
                this@AuthViewModel.resendToken = token
                _uiState.update { it.copy(
                    isLoading = false,
                    isCodeSent = true
                ) }
            }
        }
        
        val options = PhoneAuthOptions.newBuilder()
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
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
    
    fun registerWithEmailPassword(name: String, email: String, phone: String, password: String, role: UserRole = UserRole.CUSTOMER) {
        _uiState.update { it.copy(isLoading = true, error = "") }
        
        viewModelScope.launch {
            try {
                // Create Firebase user with email and password
                val user = authRepository.createUserWithEmailAndPassword(email, password)
                if (user != null) {
                    // Get Firebase ID token
                    val firebaseToken = authRepository.getIdToken()
                    if (firebaseToken != null) {
                        // Register with backend using Firebase token
                        val response = authRepository.registerUser(name, role, firebaseToken)
                        if (response.isSuccessful && response.body() != null) {
                            val userData = response.body()!!.data
                            if (userData != null) {
                                // Save user data locally
                                authRepository.saveUserData("", userData)
                                _uiState.update { it.copy(
                                    isLoading = false,
                                    isRegistered = true,
                                    isLoggedIn = true,
                                    userRole = role.name,
                                    userName = name,
                                    userEmail = email,
                                    userPhone = phone
                                ) }
                            } else {
                                _uiState.update { it.copy(
                                    isLoading = false,
                                    error = "Registration successful but no user data received"
                                ) }
                            }
                        } else {
                            val errorMessage = try {
                                response.errorBody()?.string() ?: "Registration failed"
                            } catch (e: Exception) {
                                "Registration failed: ${response.message()}"
                            }
                            _uiState.update { it.copy(
                                isLoading = false,
                                error = errorMessage
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
                        error = "Failed to create Firebase account"
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
    
    // Traditional Email/Password Authentication
    fun loginWithTraditionalAuth(email: String, password: String) {
        _uiState.update { it.copy(isLoading = true, error = "") }
        
        viewModelScope.launch {
            try {
                val response = authRepository.loginUserTraditional(email, password)
                if (response.isSuccessful && response.body() != null) {
                    val apiResponse = response.body()!!
                    if (apiResponse.status && apiResponse.data != null) {
                        val loginData = apiResponse.data
                        // Save user data
                        authRepository.saveTraditionalUserData(loginData.token, loginData)
                        _uiState.update { it.copy(
                            isLoading = false,
                            isLoggedIn = true,
                            userRole = loginData.role ?: "user",
                            userName = loginData.name,
                            userEmail = loginData.email,
                            userPhone = null
                        ) }
                    } else {
                        _uiState.update { it.copy(
                            isLoading = false,
                            error = apiResponse.message ?: "Login failed"
                        ) }
                    }
                } else {
                    val errorMessage = try {
                        response.errorBody()?.string() ?: "Login failed"
                    } catch (e: Exception) {
                        "Login failed: ${response.message()}"
                    }
                    _uiState.update { it.copy(
                        isLoading = false,
                        error = errorMessage
                    ) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    isLoading = false,
                    error = e.message ?: "Login failed"
                ) }
            }
        }
    }
    
    fun registerWithTraditionalAuth(name: String, email: String, password: String, role: String = "user") {
        _uiState.update { it.copy(isLoading = true, error = "") }
        
        viewModelScope.launch {
            try {
                val response = authRepository.registerUserTraditional(name, email, password, role)
                if (response.isSuccessful && response.body() != null) {
                    val apiResponse = response.body()!!
                    if (apiResponse.status && apiResponse.data != null) {
                        // Registration successful, now login
                        loginWithTraditionalAuth(email, password)
                    } else {
                        _uiState.update { it.copy(
                            isLoading = false,
                            error = apiResponse.message ?: "Registration failed"
                        ) }
                    }
                } else {
                    val errorMessage = try {
                        response.errorBody()?.string() ?: "Registration failed"
                    } catch (e: Exception) {
                        "Registration failed: ${response.message()}"
                    }
                    _uiState.update { it.copy(
                        isLoading = false,
                        error = errorMessage
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
    
    // Google Sign-In Authentication
    fun isGoogleSignInAvailable(): Boolean {
        return googleSignInHelper.isConfigured()
    }
    
    fun signInWithGoogle() {
        if (!isGoogleSignInAvailable()) {
            _uiState.update { it.copy(
                isLoading = false,
                error = "Google Sign-In is not configured. Please use email/password authentication."
            ) }
            return
        }
        
        _uiState.update { it.copy(isLoading = true, error = "") }
        
        // The actual sign-in intent is launched from MainActivity
        // This method will be called when checking for the result
        viewModelScope.launch {
            try {
                val task = com.hyperlocal.marketplace.presentation.MainActivity.googleSignInTask
                if (task != null) {
                    // Process the Google Sign-In result
                    val account = googleSignInHelper.handleSignInResult(task)
                    if (account != null) {
                        // Get Firebase credential from Google account
                        val authResult = googleSignInHelper.firebaseAuthWithGoogle(account)
                        if (authResult != null) {
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
                                error = "Failed to authenticate with Firebase"
                            ) }
                        }
                    } else {
                        _uiState.update { it.copy(
                            isLoading = false,
                            error = "Google Sign-In failed"
                        ) }
                    }
                } else {
                    _uiState.update { it.copy(
                        isLoading = false,
                        error = "No Google Sign-In result found"
                    ) }
                }
                
                // Clear the task after processing
                com.hyperlocal.marketplace.presentation.MainActivity.googleSignInTask = null
                
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    isLoading = false,
                    error = e.message ?: "Google Sign-In failed"
                ) }
            }
        }
    }
}

data class AuthUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val isRegistered: Boolean = false,
    val isCodeSent: Boolean = false,
    val userRole: String? = null,
    val userName: String? = null,
    val userEmail: String? = null,
    val userPhone: String? = null,
    val error: String = "",
    val showAlternativeAuth: Boolean = false
)