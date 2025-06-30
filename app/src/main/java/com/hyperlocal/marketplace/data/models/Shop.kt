package com.hyperlocal.marketplace.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Shop(
    @SerializedName("id")
    val id: Int,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("whatsapp_number")
    val whatsappNumber: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("image_url")
    val imageUrl: String?,
    @SerializedName("banner_url")
    val bannerUrl: String?,
    @SerializedName("is_approved")
    val isApproved: Boolean = false,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("distance")
    val distance: Double? = null // Distance from user location in km
) : Parcelable

data class ShopCreateRequest(
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("whatsapp_number")
    val whatsappNumber: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("image_url")
    val imageUrl: String? = null,
    @SerializedName("banner_url")
    val bannerUrl: String? = null
)

data class ShopUpdateRequest(
    @SerializedName("name")
    val name: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("whatsapp_number")
    val whatsappNumber: String?,
    @SerializedName("address")
    val address: String?,
    @SerializedName("latitude")
    val latitude: Double?,
    @SerializedName("longitude")
    val longitude: Double?,
    @SerializedName("image_url")
    val imageUrl: String?,
    @SerializedName("banner_url")
    val bannerUrl: String?
)

data class ShopSearchResponse(
    @SerializedName("shops")
    val shops: List<Shop>,
    @SerializedName("total")
    val total: Int,
    @SerializedName("page")
    val page: Int,
    @SerializedName("page_size")
    val pageSize: Int,
    @SerializedName("total_pages")
    val totalPages: Int
)

data class LocationQuery(
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("radius")
    val radius: Double = 10.0 // Default 10km radius
)