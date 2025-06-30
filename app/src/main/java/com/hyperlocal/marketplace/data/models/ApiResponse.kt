package com.hyperlocal.marketplace.data.models

import com.google.gson.annotations.SerializedName

/**
 * Generic API response wrapper that matches the backend API gateway format
 */
data class ApiResponse<T>(
    @SerializedName("status")
    val status: Boolean,
    
    @SerializedName("message")
    val message: String,
    
    @SerializedName("data")
    val data: T?
)

/**
 * Login request for phone-based authentication
 */
data class LoginRequest(
    @SerializedName("phone")
    val phone: String
)

/**
 * Registration request
 */
data class RegisterRequest(
    @SerializedName("name")
    val name: String,
    
    @SerializedName("email")
    val email: String,
    
    @SerializedName("phone")
    val phone: String,
    
    @SerializedName("role")
    val role: String
)

/**
 * Login response data
 */
data class LoginResponseData(
    @SerializedName("token")
    val token: String,
    
    @SerializedName("user")
    val user: User
)



/**
 * Product creation request
 */
data class ProductCreateRequest(
    @SerializedName("name")
    val name: String,
    
    @SerializedName("description")
    val description: String,
    
    @SerializedName("price")
    val price: Double,
    
    @SerializedName("category")
    val category: String,
    
    @SerializedName("imageUrl")
    val imageUrl: String? = null,
    
    @SerializedName("inStock")
    val inStock: Boolean = true,
    
    @SerializedName("quantity")
    val quantity: Int,
    
    @SerializedName("unit")
    val unit: String
)

/**
 * Add product from catalog request
 */
data class AddProductFromCatalogRequest(
    @SerializedName("catalogId")
    val catalogId: String,
    
    @SerializedName("shopId")
    val shopId: String,
    
    @SerializedName("quantity")
    val quantity: Int
)