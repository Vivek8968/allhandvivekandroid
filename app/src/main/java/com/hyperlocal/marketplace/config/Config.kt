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
    
    // Base URLs for backend services
    // TODO: Replace with your actual server URLs
    private const val DEBUG_BASE_URL = "http://10.0.2.2" // Android emulator localhost
    private const val PRODUCTION_BASE_URL = "https://your-production-domain.com" // Production URL
    
    val BASE_URL = if (IS_DEBUG) DEBUG_BASE_URL else PRODUCTION_BASE_URL
    
    // Service URLs
    val USER_SERVICE_URL = "$BASE_URL:8001/"
    val SELLER_SERVICE_URL = "$BASE_URL:8002/"
    val CUSTOMER_SERVICE_URL = "$BASE_URL:8003/"
    val CATALOG_SERVICE_URL = "$BASE_URL:8004/"
    val ADMIN_SERVICE_URL = "$BASE_URL:8005/"
    
    // API Endpoints
    object Endpoints {
        // User Service
        const val REGISTER = "auth/register"
        const val LOGIN = "auth/login"
        const val VERIFY_TOKEN = "auth/verify-token"
        const val REFRESH_TOKEN = "auth/refresh-token"
        
        // Seller Service
        const val CREATE_SHOP = "shops"
        const val GET_MY_SHOP = "shops/me"
        const val UPDATE_SHOP = "shops/me"
        const val ADD_PRODUCT = "shops/me/products"
        const val GET_MY_PRODUCTS = "shops/me/products"
        const val UPDATE_PRODUCT = "shops/me/products/{id}"
        const val DELETE_PRODUCT = "shops/me/products/{id}"
        const val UPLOAD_IMAGE = "shops/me/images/upload"
        
        // Customer Service
        const val NEARBY_SHOPS = "shops/nearby"
        const val SHOP_DETAILS = "shops/{id}"
        const val SHOP_PRODUCTS = "shops/{id}/products"
        const val SEARCH_SHOPS = "shops/search"
        
        // Catalog Service
        const val CATALOG = "catalog"
        const val CATALOG_SEARCH = "catalog/search"
        const val CATALOG_ITEM = "catalog/{id}"
        
        // Admin Service
        const val ADMIN_USERS = "admin/users"
        const val ADMIN_SHOPS = "admin/shops"
        const val ADMIN_LOGS = "admin/logs"
        const val ADMIN_STATS = "admin/stats"
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