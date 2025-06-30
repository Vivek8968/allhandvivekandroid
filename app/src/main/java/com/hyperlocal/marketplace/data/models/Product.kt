package com.hyperlocal.marketplace.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CatalogItem(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("category")
    val category: String,
    @SerializedName("brand")
    val brand: String?,
    @SerializedName("model")
    val model: String?,
    @SerializedName("image_url")
    val imageUrl: String?,
    @SerializedName("specifications")
    val specifications: Map<String, String>?,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String
) : Parcelable

@Parcelize
data class ShopInventory(
    @SerializedName("id")
    val id: Int,
    @SerializedName("shop_id")
    val shopId: Int,
    @SerializedName("catalog_item_id")
    val catalogItemId: Int,
    @SerializedName("catalog_item")
    val catalogItem: CatalogItem?,
    @SerializedName("price")
    val price: Double,
    @SerializedName("stock")
    val stock: Int,
    @SerializedName("is_available")
    val isAvailable: Boolean = true,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String
) : Parcelable

data class ShopInventoryCreateRequest(
    @SerializedName("catalog_item_id")
    val catalogItemId: Int,
    @SerializedName("price")
    val price: Double,
    @SerializedName("stock")
    val stock: Int,
    @SerializedName("is_available")
    val isAvailable: Boolean = true
)

data class ShopInventoryUpdateRequest(
    @SerializedName("price")
    val price: Double?,
    @SerializedName("stock")
    val stock: Int?,
    @SerializedName("is_available")
    val isAvailable: Boolean?
)

data class CatalogSearchResponse(
    @SerializedName("items")
    val items: List<CatalogItem>,
    @SerializedName("total")
    val total: Int,
    @SerializedName("page")
    val page: Int,
    @SerializedName("page_size")
    val pageSize: Int,
    @SerializedName("total_pages")
    val totalPages: Int
)

data class ShopProductsResponse(
    @SerializedName("products")
    val products: List<ShopInventory>,
    @SerializedName("shop")
    val shop: Shop,
    @SerializedName("total")
    val total: Int
)

@Parcelize
data class Category(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String
) : Parcelable