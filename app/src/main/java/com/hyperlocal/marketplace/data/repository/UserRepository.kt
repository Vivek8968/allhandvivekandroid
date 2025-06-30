package com.hyperlocal.marketplace.data.repository

import com.hyperlocal.marketplace.data.api.UserApiService
import com.hyperlocal.marketplace.data.models.*
import com.hyperlocal.marketplace.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userApiService: UserApiService
) {
    
    suspend fun registerUser(request: UserCreateRequest): Flow<Resource<User>> = flow {
        try {
            emit(Resource.Loading())
            val response = userApiService.registerUser(request)
            if (response.isSuccessful) {
                response.body()?.let { user ->
                    emit(Resource.Success(user))
                } ?: emit(Resource.Error("Empty response body"))
            } else {
                emit(Resource.Error("Registration failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.localizedMessage}"))
        }
    }
    
    suspend fun loginUser(request: FirebaseAuthRequest): Flow<Resource<TokenResponse>> = flow {
        try {
            emit(Resource.Loading())
            val response = userApiService.loginUser(request)
            if (response.isSuccessful) {
                response.body()?.let { tokenResponse ->
                    emit(Resource.Success(tokenResponse))
                } ?: emit(Resource.Error("Empty response body"))
            } else {
                emit(Resource.Error("Login failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.localizedMessage}"))
        }
    }
    
    suspend fun verifyToken(token: String): Flow<Resource<Map<String, Any>>> = flow {
        try {
            emit(Resource.Loading())
            val response = userApiService.verifyToken("Bearer $token")
            if (response.isSuccessful) {
                response.body()?.let { result ->
                    emit(Resource.Success(result))
                } ?: emit(Resource.Error("Empty response body"))
            } else {
                emit(Resource.Error("Token verification failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.localizedMessage}"))
        }
    }
    
    suspend fun refreshToken(token: String): Flow<Resource<TokenResponse>> = flow {
        try {
            emit(Resource.Loading())
            val response = userApiService.refreshToken("Bearer $token")
            if (response.isSuccessful) {
                response.body()?.let { tokenResponse ->
                    emit(Resource.Success(tokenResponse))
                } ?: emit(Resource.Error("Empty response body"))
            } else {
                emit(Resource.Error("Token refresh failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.localizedMessage}"))
        }
    }
}