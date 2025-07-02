package com.hyperlocal.marketplace.data.api

import com.hyperlocal.marketplace.config.Config
import com.hyperlocal.marketplace.data.models.*
import retrofit2.Response
import retrofit2.http.*

interface UserApiService {
    
    @POST(Config.Endpoints.REGISTER)
    suspend fun registerUser(
        @Body request: RegisterRequest
    ): Response<ApiResponse<User>>
    
    @POST(Config.Endpoints.LOGIN)
    suspend fun loginUser(
        @Body request: LoginRequest
    ): Response<ApiResponse<LoginResponseData>>
    
    @POST(Config.Endpoints.VERIFY_TOKEN)
    suspend fun verifyToken(
        @Header("Authorization") token: String
    ): Response<ApiResponse<User>>
    
    @GET(Config.Endpoints.GET_USER_PROFILE)
    suspend fun getUserProfile(
        @Header("Authorization") token: String
    ): Response<ApiResponse<User>>
    
    @PUT(Config.Endpoints.UPDATE_USER_PROFILE)
    suspend fun updateUserProfile(
        @Header("Authorization") token: String,
        @Body request: Map<String, Any>
    ): Response<ApiResponse<User>>
    
    @POST(Config.Endpoints.LOGIN)
    suspend fun loginWithFirebaseToken(
        @Body request: FirebaseAuthRequest
    ): Response<ApiResponse<LoginResponseData>>
}