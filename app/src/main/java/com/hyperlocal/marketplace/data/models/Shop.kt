package com.hyperlocal.marketplace.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Shop(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("address")
    val address: String,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("distance")
    val distance: Double? = null,
    @SerializedName("distanceFormatted")
    val distanceFormatted: String? = null,
    @SerializedName("category")
    val category: String,
    @SerializedName("rating")
    val rating: Double = 0.0,
    @SerializedName("imageUrl")
    val imageUrl: String?,
    @SerializedName("ownerId")
    val ownerId: String,
    @SerializedName("isOpen")
    val isOpen: Boolean = true,
    @SerializedName("openingTime")
    val openingTime: String = "09:00",
    @SerializedName("closingTime")
    val closingTime: String = "21:00",
    @SerializedName("whatsappNumber")
    val whatsappNumber: String? = "+919999999999" // Default for demo purposes
) : Parcelable

data class ShopCreateRequest(
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("category")
    val category: String,
    @SerializedName("imageUrl")
    val imageUrl: String? = null,
    @SerializedName("openingTime")
    val openingTime: String = "09:00",
    @SerializedName("closingTime")
    val closingTime: String = "21:00",
    @SerializedName("whatsappNumber")
    val whatsappNumber: String? = null
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

data class ImageUploadResponse(
    @SerializedName("presigned_url")
    val presignedUrl: String,
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("field_name")
    val fieldName: String
)