package com.hyperlocal.marketplace.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

enum class UserRole {
    @SerializedName("customer")
    CUSTOMER,
    @SerializedName("seller")
    SELLER,
    @SerializedName("admin")
    ADMIN
}

@Parcelize
data class User(
    @SerializedName("id")
    val id: Int,
    @SerializedName("firebase_uid")
    val firebaseUid: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String?,
    @SerializedName("phone")
    val phone: String?,
    @SerializedName("role")
    val role: UserRole,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String
) : Parcelable

data class UserCreateRequest(
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String?,
    @SerializedName("phone")
    val phone: String?,
    @SerializedName("role")
    val role: UserRole,
    @SerializedName("firebase_token")
    val firebaseToken: String
)

data class FirebaseUserCreateRequest(
    @SerializedName("name")
    val name: String,
    @SerializedName("role")
    val role: UserRole,
    @SerializedName("firebase_token")
    val firebaseToken: String
)

data class FirebaseAuthRequest(
    @SerializedName("firebase_token")
    val firebaseToken: String
)

data class TokenResponse(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("token_type")
    val tokenType: String,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("role")
    val role: UserRole
)