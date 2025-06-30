package com.hyperlocal.marketplace.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AdminStats(
    @SerializedName("total_users")
    val totalUsers: Int,
    @SerializedName("total_customers")
    val totalCustomers: Int,
    @SerializedName("total_sellers")
    val totalSellers: Int,
    @SerializedName("total_shops")
    val totalShops: Int,
    @SerializedName("approved_shops")
    val approvedShops: Int,
    @SerializedName("pending_shops")
    val pendingShops: Int,
    @SerializedName("total_products")
    val totalProducts: Int,
    @SerializedName("active_products")
    val activeProducts: Int
) : Parcelable

@Parcelize
data class AdminLog(
    @SerializedName("id")
    val id: Int,
    @SerializedName("admin_id")
    val adminId: Int,
    @SerializedName("action")
    val action: String,
    @SerializedName("entity_type")
    val entityType: String,
    @SerializedName("entity_id")
    val entityId: Int,
    @SerializedName("details")
    val details: String?,
    @SerializedName("created_at")
    val createdAt: String
) : Parcelable

data class AdminUsersResponse(
    @SerializedName("users")
    val users: List<User>,
    @SerializedName("total")
    val total: Int,
    @SerializedName("page")
    val page: Int,
    @SerializedName("page_size")
    val pageSize: Int,
    @SerializedName("total_pages")
    val totalPages: Int
)

data class AdminShopsResponse(
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

data class AdminLogsResponse(
    @SerializedName("logs")
    val logs: List<AdminLog>,
    @SerializedName("total")
    val total: Int,
    @SerializedName("page")
    val page: Int,
    @SerializedName("page_size")
    val pageSize: Int,
    @SerializedName("total_pages")
    val totalPages: Int
)

data class ShopApprovalRequest(
    @SerializedName("is_approved")
    val isApproved: Boolean,
    @SerializedName("rejection_reason")
    val rejectionReason: String? = null
)