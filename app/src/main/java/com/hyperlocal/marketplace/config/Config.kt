package com.hyperlocal.marketplace.config

/**
 * Configuration file for the Hyperlocal Marketplace app
 * 
 * IMPORTANT: Update these URLs when moving to production
 * 
 * For local development:
 * - Use your local machine IP address (not localhost/127.0.0.1)
 * - Ensure backend services are running on specified ports
 * 
 * For production:
 * - Replace with actual production server URLs
 * - Update Firebase configuration
 * - Update any API keys or secrets
 */
object Config {
    
    // Environment toggle - set to false for production
    const val IS_DEBUG = true
    
    // Base URLs for backend services - NOW CONNECTED TO REAL API GATEWAY!
    private const val DEBUG_BASE_URL = "http://192.168.1.8:12000"  // Local development IP
    private const val PRODUCTION_BASE_URL = "https://work-1-hhizqbkkwgjdnapz.prod-runtime.all-hands.dev:12000"
    
    val BASE_URL = if (IS_DEBUG) DEBUG_BASE_URL else PRODUCTION_BASE_URL
    
    // Service URLs - All routed through API Gateway
    val USER_SERVICE_URL = "$BASE_URL/"
    val SELLER_SERVICE_URL = "$BASE_URL/"
    val CUSTOMER_SERVICE_URL = "$BASE_URL/"
    val CATALOG_SERVICE_URL = "$BASE_URL/"
    val ADMIN_SERVICE_URL = "$BASE_URL/"
    
    // API Endpoints - Updated to match API Gateway routes
    object Endpoints {
        // User/Auth Service
        const val REGISTER = "auth/register"
        const val LOGIN = "auth/login"
        const val VERIFY_TOKEN = "auth/verify-token"
        const val GET_USER_PROFILE = "users/me"
        const val UPDATE_USER_PROFILE = "users/me"
        
        // Seller/Vendor Service
        const val GET_VENDOR_SHOP = "vendor/shop"
        const val CREATE_VENDOR_SHOP = "vendor/shop"
        const val UPDATE_VENDOR_SHOP = "vendor/shop"
        const val GET_VENDOR_PRODUCTS = "vendor/products"
        const val ADD_PRODUCT_FROM_CATALOG = "vendor/products/add-from-catalog"
        
        // Customer/Shop Service
        const val GET_ALL_SHOPS = "shops"
        const val GET_SHOP_BY_ID = "shops/{id}"
        const val GET_SHOP_PRODUCTS = "shops/{id}/products"
        const val CREATE_SHOP = "shops"
        const val UPDATE_SHOP = "shops/{id}"
        const val ADD_PRODUCT_TO_SHOP = "shops/{id}/products"
        const val UPDATE_SHOP_PRODUCT = "shops/{shop_id}/products/{product_id}"
        const val DELETE_SHOP_PRODUCT = "shops/{shop_id}/products/{product_id}"
        
        // Catalog Service
        const val GET_CATALOG = "catalog"
        const val GET_CATALOG_CATEGORIES = "catalog/categories"
        const val GET_PRODUCT_BY_ID = "products/{id}"
        
        // Admin Service (placeholder - not implemented in gateway yet)
        const val ADMIN_STATS = "admin/stats"
        const val ADMIN_USERS = "admin/users"
        const val ADMIN_SHOPS = "admin/shops"
    }
    
    // Firebase Configuration
    object Firebase {
        // TODO: Add your Firebase project configuration
        const val PROJECT_ID = "your-firebase-project-id"
        const val WEB_CLIENT_ID = "your-web-client-id.googleusercontent.com"
    }
    
    // App Configuration
    object App {
        const val DEFAULT_SEARCH_RADIUS_KM = 10.0
        const val MAX_SEARCH_RADIUS_KM = 50.0
        const val DEFAULT_PAGE_SIZE = 20
        const val MAX_IMAGE_SIZE_MB = 5
        const val LOCATION_UPDATE_INTERVAL_MS = 30000L // 30 seconds
        const val TOKEN_REFRESH_THRESHOLD_MINUTES = 5
    }
    
    // Demo/Test Configuration
    object Demo {
        const val DEMO_LATITUDE = 28.6139 // New Delhi
        const val DEMO_LONGITUDE = 77.2090
        const val USE_DEMO_LOCATION = IS_DEBUG
        
        // Demo user credentials for testing
        const val DEMO_CUSTOMER_PHONE = "+919999999999"
        const val DEMO_SELLER_PHONE = "+918888888888"
        const val DEMO_ADMIN_PHONE = "+917777777777"
    }
    
    // Network Configuration
    object Network {
        const val CONNECT_TIMEOUT_SECONDS = 30L
        const val READ_TIMEOUT_SECONDS = 30L
        const val WRITE_TIMEOUT_SECONDS = 30L
        const val RETRY_COUNT = 3
    }
    
    // Cache Configuration
    object Cache {
        const val SHOPS_CACHE_DURATION_MINUTES = 5
        const val CATALOG_CACHE_DURATION_HOURS = 24
        const val USER_PROFILE_CACHE_DURATION_HOURS = 1
    }
}