package com.hyperlocal.marketplace.data.api

import com.hyperlocal.marketplace.config.Config
import com.hyperlocal.marketplace.data.models.*
import retrofit2.Response
import retrofit2.http.*

interface UserApiService {
    
    // Traditional email/password authentication endpoints
    @POST("api/users/register")
    suspend fun registerUserTraditional(
        @Body request: TraditionalRegisterRequest
    ): Response<ApiResponse<User>>
    
    @POST("api/users/login")
    suspend fun loginUserTraditional(
        @Body request: TraditionalLoginRequest
    ): Response<ApiResponse<TraditionalLoginResponse>>
    
    // Firebase authentication endpoints (existing)
    @POST("auth/register")
    suspend fun registerUser(
        @Body request: FirebaseUserCreateRequest
    ): Response<ApiResponse<User>>
    
    @POST("auth/firebase-login")
    suspend fun loginWithFirebaseToken(
        @Body request: FirebaseAuthRequest
    ): Response<ApiResponse<LoginResponseData>>
    
    @POST("auth/verify-token")
    suspend fun verifyToken(
        @Header("Authorization") token: String
    ): Response<ApiResponse<Map<String, Any>>>
    
    @POST("auth/refresh-token")
    suspend fun refreshToken(
        @Header("Authorization") token: String
    ): Response<ApiResponse<LoginResponseData>>
    
    @GET("profile")
    suspend fun getUserProfile(
        @Header("Authorization") token: String
    ): Response<ApiResponse<User>>
    
    @PUT("profile")
    suspend fun updateUserProfile(
        @Header("Authorization") token: String,
        @Body request: Map<String, Any>
    ): Response<ApiResponse<User>>
}