package com.hyperlocal.marketplace.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.hyperlocal.marketplace.data.api.UserApiService
import com.hyperlocal.marketplace.data.models.ApiResponse
import com.hyperlocal.marketplace.data.models.FirebaseAuthRequest
import com.hyperlocal.marketplace.data.models.FirebaseUserCreateRequest
import com.hyperlocal.marketplace.data.models.LoginResponseData
import com.hyperlocal.marketplace.data.models.RegisterRequest
import com.hyperlocal.marketplace.data.models.TraditionalLoginRequest
import com.hyperlocal.marketplace.data.models.TraditionalLoginResponse
import com.hyperlocal.marketplace.data.models.TraditionalRegisterRequest
import com.hyperlocal.marketplace.data.models.User
import com.hyperlocal.marketplace.data.models.UserRole
import kotlinx.coroutines.tasks.await
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val userApiService: UserApiService,
    private val userPreferencesRepository: UserPreferencesRepository
) {
    private val auth: FirebaseAuth = Firebase.auth

    // Firebase Authentication
    suspend fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential): FirebaseUser? {
        return try {
            val result = auth.signInWithCredential(credential).await()
            result.user
        } catch (e: Exception) {
            null
        }
    }

    suspend fun signInWithEmailAndPassword(email: String, password: String): FirebaseUser? {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            result.user
        } catch (e: Exception) {
            null
        }
    }

    suspend fun createUserWithEmailAndPassword(email: String, password: String): FirebaseUser? {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            result.user
        } catch (e: Exception) {
            null
        }
    }

    suspend fun signOut() {
        auth.signOut()
        userPreferencesRepository.clearUserData()
    }

    // Traditional Backend Authentication
    suspend fun registerUserTraditional(name: String, email: String, password: String, role: String = "user"): Response<ApiResponse<User>> {
        val request = TraditionalRegisterRequest(
            name = name,
            email = email,
            password = password,
            role = role
        )
        return userApiService.registerUserTraditional(request)
    }

    suspend fun loginUserTraditional(email: String, password: String): Response<ApiResponse<TraditionalLoginResponse>> {
        val request = TraditionalLoginRequest(email, password)
        return userApiService.loginUserTraditional(request)
    }

    // Firebase Backend Authentication (existing)
    suspend fun registerUser(name: String, role: UserRole, firebaseToken: String): Response<ApiResponse<User>> {
        val request = FirebaseUserCreateRequest(
            name = name,
            role = role,
            firebaseToken = firebaseToken
        )
        return userApiService.registerUser(request)
    }

    suspend fun loginWithFirebaseToken(firebaseToken: String): Response<ApiResponse<LoginResponseData>> {
        val request = FirebaseAuthRequest(firebaseToken)
        return userApiService.loginWithFirebaseToken(request)
    }

    suspend fun verifyToken(token: String): Response<ApiResponse<Map<String, Any>>> {
        return userApiService.verifyToken("Bearer $token")
    }

    suspend fun getUserProfile(token: String): Response<ApiResponse<User>> {
        return userApiService.getUserProfile("Bearer $token")
    }

    // Save user data to preferences
    suspend fun saveUserData(token: String, user: User) {
        userPreferencesRepository.saveAuthToken(token)
        userPreferencesRepository.saveUserRole(user.role)
        userPreferencesRepository.saveUserInfo(
            userId = user.id.toString(),
            name = user.name,
            email = user.email,
            phone = user.phone,
            firebaseUid = user.firebaseUid
        )
    }

    // Save traditional user data to preferences
    suspend fun saveTraditionalUserData(token: String, loginResponse: TraditionalLoginResponse) {
        userPreferencesRepository.saveAuthToken(token)
        // Convert role string to UserRole enum, default to CUSTOMER
        val userRole = when (loginResponse.role?.lowercase()) {
            "admin" -> UserRole.ADMIN
            "seller" -> UserRole.SELLER
            else -> UserRole.CUSTOMER
        }
        userPreferencesRepository.saveUserRole(userRole)
        userPreferencesRepository.saveUserInfo(
            userId = "0", // Traditional auth might not have user ID
            name = loginResponse.name,
            email = loginResponse.email,
            phone = null,
            firebaseUid = ""
        )
    }

    // Get current Firebase user
    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    // Get Firebase ID token
    suspend fun getIdToken(): String? {
        return auth.currentUser?.getIdToken(true)?.await()?.token
    }
}