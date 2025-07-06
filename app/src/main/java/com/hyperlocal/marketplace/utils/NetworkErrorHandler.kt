package com.hyperlocal.marketplace.utils

import android.content.Context
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuthException
import com.hyperlocal.marketplace.data.models.ApiResponse
import retrofit2.Response
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Utility class for handling network and API errors
 */
object NetworkErrorHandler {
    
    /**
     * Extract user-friendly error message from API response
     */
    fun <T> getErrorMessage(response: Response<ApiResponse<T>>): String {
        return when {
            response.isSuccessful -> {
                response.body()?.message ?: "Unknown error occurred"
            }
            response.code() == 400 -> "Invalid request. Please check your input."
            response.code() == 401 -> "Authentication failed. Please login again."
            response.code() == 403 -> "Access denied. You don't have permission for this action."
            response.code() == 404 -> "Resource not found."
            response.code() == 409 -> "Conflict. This resource already exists."
            response.code() == 422 -> "Validation error. Please check your input."
            response.code() == 500 -> "Server error. Please try again later."
            response.code() == 503 -> "Service unavailable. Please try again later."
            else -> "Network error (${response.code()}). Please try again."
        }
    }
    
    /**
     * Handle network exceptions and return user-friendly messages
     */
    fun handleNetworkException(exception: Throwable): String {
        return when (exception) {
            is UnknownHostException -> "No internet connection. Please check your network."
            is ConnectException -> "Cannot connect to server. Please check your network."
            is SocketTimeoutException -> "Request timeout. Please try again."
            is IOException -> "Network error. Please check your connection."
            else -> "An unexpected error occurred: ${exception.message}"
        }
    }
    
    /**
     * Handle Firebase authentication errors
     */
    fun handleFirebaseAuthError(exception: FirebaseException): String {
        return when (exception) {
            is FirebaseAuthException -> {
                when (exception.errorCode) {
                    "ERROR_INVALID_PHONE_NUMBER" -> "Invalid phone number format."
                    "ERROR_INVALID_VERIFICATION_CODE" -> "Invalid verification code."
                    "ERROR_QUOTA_EXCEEDED" -> "SMS quota exceeded. Please try again later."
                    "ERROR_SESSION_EXPIRED" -> "Verification session expired. Please try again."
                    "ERROR_INVALID_VERIFICATION_ID" -> "Invalid verification ID."
                    "ERROR_USER_DISABLED" -> "This account has been disabled."
                    "ERROR_TOO_MANY_REQUESTS" -> "Too many requests. Please try again later."
                    "ERROR_OPERATION_NOT_ALLOWED" -> "Phone authentication is not enabled."
                    "billing_not_enabled" -> "Phone authentication is temporarily unavailable. Please try email authentication."
                    else -> "Authentication error: ${exception.message}"
                }
            }
            else -> "Firebase error: ${exception.message}"
        }
    }
    
    /**
     * Check if error is related to network connectivity
     */
    fun isNetworkError(exception: Throwable): Boolean {
        return exception is UnknownHostException || 
               exception is ConnectException || 
               exception is SocketTimeoutException ||
               exception is IOException
    }
    
    /**
     * Check if error is related to authentication
     */
    fun isAuthError(responseCode: Int): Boolean {
        return responseCode == 401 || responseCode == 403
    }
    
    /**
     * Check if error is a server error
     */
    fun isServerError(responseCode: Int): Boolean {
        return responseCode >= 500
    }
    
    /**
     * Check if error is a client error
     */
    fun isClientError(responseCode: Int): Boolean {
        return responseCode in 400..499
    }
    
    /**
     * Get retry suggestion based on error type
     */
    fun shouldRetry(exception: Throwable): Boolean {
        return when (exception) {
            is SocketTimeoutException -> true
            is ConnectException -> true
            is IOException -> true
            else -> false
        }
    }
    
    fun shouldRetry(responseCode: Int): Boolean {
        return responseCode >= 500 || responseCode == 408 || responseCode == 429
    }
}