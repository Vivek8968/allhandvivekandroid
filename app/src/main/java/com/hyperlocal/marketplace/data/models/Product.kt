package com.hyperlocal.marketplace.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Product model matching API Gateway format
 */
@Parcelize
data class Product(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("price")
    val price: Double,
    @SerializedName("category")
    val category: String,
    @SerializedName("imageUrl")
    val imageUrl: String?,
    @SerializedName("shopId")
    val shopId: String,
    @SerializedName("inStock")
    val inStock: Boolean = true,
    @SerializedName("quantity")
    val quantity: Int,
    @SerializedName("unit")
    val unit: String
) : Parcelable

/**
 * Catalog item model matching API Gateway format
 */
@Parcelize
data class CatalogItem(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("category")
    val category: String,
    @SerializedName("imageUrl")
    val imageUrl: String?,
    @SerializedName("suggestedPrice")
    val suggestedPrice: Double,
    @SerializedName("unit")
    val unit: String
) : Parcelable

/**
 * Shop inventory model for seller dashboard
 */
@Parcelize
data class ShopInventory(
    @SerializedName("id")
    val id: String,
    @SerializedName("catalogItem")
    val catalogItem: CatalogItem,
    @SerializedName("price")
    val price: Double,
    @SerializedName("stock")
    val stock: Int,
    @SerializedName("isAvailable")
    val isAvailable: Boolean = true
) : Parcelable

/**
 * Category model
 */
@Parcelize
data class Category(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String?
) : Parcelable