package com.hyperlocal.marketplace.data.api

import com.hyperlocal.marketplace.config.Config
import com.hyperlocal.marketplace.data.models.*
import retrofit2.Response
import retrofit2.http.*

interface UserApiService {
    
    @POST(Config.Endpoints.REGISTER)
    suspend fun registerUser(
        @Body request: UserCreateRequest
    ): Response<User>
    
    @POST(Config.Endpoints.LOGIN)
    suspend fun loginUser(
        @Body request: FirebaseAuthRequest
    ): Response<TokenResponse>
    
    @GET(Config.Endpoints.VERIFY_TOKEN)
    suspend fun verifyToken(
        @Header("Authorization") token: String
    ): Response<Map<String, Any>>
    
    @POST(Config.Endpoints.REFRESH_TOKEN)
    suspend fun refreshToken(
        @Header("Authorization") token: String
    ): Response<TokenResponse>
}