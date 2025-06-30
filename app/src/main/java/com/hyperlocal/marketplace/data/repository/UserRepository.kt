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
    
    suspend fun registerUser(request: RegisterRequest): Flow<Resource<User>> = flow {
        try {
            emit(Resource.Loading())
            val response = userApiService.registerUser(request)
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    if (apiResponse.status && apiResponse.data != null) {
                        emit(Resource.Success(apiResponse.data))
                    } else {
                        emit(Resource.Error(apiResponse.message))
                    }
                } ?: emit(Resource.Error("Empty response body"))
            } else {
                emit(Resource.Error("Registration failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.localizedMessage}"))
        }
    }
    
    suspend fun loginUser(request: LoginRequest): Flow<Resource<LoginResponseData>> = flow {
        try {
            emit(Resource.Loading())
            val response = userApiService.loginUser(request)
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    if (apiResponse.status && apiResponse.data != null) {
                        emit(Resource.Success(apiResponse.data))
                    } else {
                        emit(Resource.Error(apiResponse.message))
                    }
                } ?: emit(Resource.Error("Empty response body"))
            } else {
                emit(Resource.Error("Login failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.localizedMessage}"))
        }
    }
    
    suspend fun verifyToken(token: String): Flow<Resource<User>> = flow {
        try {
            emit(Resource.Loading())
            val response = userApiService.verifyToken("Bearer $token")
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    if (apiResponse.status && apiResponse.data != null) {
                        emit(Resource.Success(apiResponse.data))
                    } else {
                        emit(Resource.Error(apiResponse.message))
                    }
                } ?: emit(Resource.Error("Empty response body"))
            } else {
                emit(Resource.Error("Token verification failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.localizedMessage}"))
        }
    }
    
    suspend fun getUserProfile(token: String): Flow<Resource<User>> = flow {
        try {
            emit(Resource.Loading())
            val response = userApiService.getUserProfile("Bearer $token")
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    if (apiResponse.status && apiResponse.data != null) {
                        emit(Resource.Success(apiResponse.data))
                    } else {
                        emit(Resource.Error(apiResponse.message))
                    }
                } ?: emit(Resource.Error("Empty response body"))
            } else {
                emit(Resource.Error("Failed to get user profile: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.localizedMessage}"))
        }
    }
}