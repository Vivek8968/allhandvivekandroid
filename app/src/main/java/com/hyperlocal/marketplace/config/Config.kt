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
    
    // Base host for microservices
    private const val DEBUG_BASE_HOST = "http://192.168.1.8"  // Local development IP
    private const val PRODUCTION_BASE_HOST = "https://work-1-hhizqbkkwgjdnapz.prod-runtime.all-hands.dev"
    
    private val BASE_HOST = if (IS_DEBUG) DEBUG_BASE_HOST else PRODUCTION_BASE_HOST
    
    // Microservice URLs - Each service has its own port and base path
    val USER_SERVICE_URL = "$BASE_HOST:8001/api/users/"
    val SELLER_SERVICE_URL = "$BASE_HOST:8002/api/sellers/"
    val CUSTOMER_SERVICE_URL = "$BASE_HOST:8003/api/customers/"
    val CATALOG_SERVICE_URL = "$BASE_HOST:8004/api/catalog/"
    val ADMIN_SERVICE_URL = "$BASE_HOST:8005/api/admin/"
    
    // API Endpoints - Updated for microservices architecture
    object Endpoints {
        // User/Auth Service - /api/users/ prefix is now in the base URL
        const val REGISTER = "auth/register"
        const val LOGIN = "auth/login"
        const val VERIFY_TOKEN = "auth/verify-token"
        const val GET_USER_PROFILE = "me"
        const val UPDATE_USER_PROFILE = "me"
        
        // Seller/Vendor Service - /api/sellers/ prefix is now in the base URL
        const val GET_VENDOR_SHOP = "shop"
        const val CREATE_VENDOR_SHOP = "shop"
        const val UPDATE_VENDOR_SHOP = "shop"
        const val GET_VENDOR_PRODUCTS = "products"
        const val ADD_PRODUCT_FROM_CATALOG = "products/add-from-catalog"
        
        // Customer/Shop Service - /api/customers/ prefix is now in the base URL
        const val GET_ALL_SHOPS = "shops"
        const val GET_SHOP_BY_ID = "shops/{id}"
        const val GET_SHOP_PRODUCTS = "shops/{id}/products"
        const val CREATE_SHOP = "shops"
        const val UPDATE_SHOP = "shops/{id}"
        const val ADD_PRODUCT_TO_SHOP = "shops/{id}/products"
        const val UPDATE_SHOP_PRODUCT = "shops/{shop_id}/products/{product_id}"
        const val DELETE_SHOP_PRODUCT = "shops/{shop_id}/products/{product_id}"
        
        // Catalog Service - /api/catalog/ prefix is now in the base URL
        const val GET_CATALOG = "items"
        const val GET_CATALOG_CATEGORIES = "categories"
        const val GET_PRODUCT_BY_ID = "items/{id}"
        
        // Admin Service - /api/admin/ prefix is now in the base URL
        const val ADMIN_STATS = "stats"
        const val ADMIN_USERS = "users"
        const val ADMIN_SHOPS = "shops"
    }
    
    // Firebase Configuration
    object Firebase {
        // Firebase project configuration
        const val PROJECT_ID = "hyperlocal-marketplace"
        const val WEB_CLIENT_ID = "1234567890-abcdefghijklmnopqrstuvwxyz.apps.googleusercontent.com"
        
        // Note: This is a placeholder client ID. You need to replace it with your actual
        // Web Client ID from the Firebase Console > Authentication > Sign-in method > Google
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