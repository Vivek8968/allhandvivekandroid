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
    private const val DEBUG_BASE_HOST = "http://10.0.2.2:8080"  // For Android emulator (localhost equivalent)
    private const val PRODUCTION_BASE_HOST = "https://work-1-hhizqbkkwgjdnapz.prod-runtime.all-hands.dev"
    
    private val BASE_HOST = if (IS_DEBUG) DEBUG_BASE_HOST else PRODUCTION_BASE_HOST
    
    // Microservice URLs - Each service has its own port and base path
    val USER_SERVICE_URL = "$BASE_HOST/"
    val SELLER_SERVICE_URL = "$BASE_HOST:8002/"
    val CUSTOMER_SERVICE_URL = "$BASE_HOST:8003/"
    val CATALOG_SERVICE_URL = "$BASE_HOST:8004/"
    val ADMIN_SERVICE_URL = "$BASE_HOST:8005/"
    
    // API Endpoints - Updated for microservices architecture
    object Endpoints {
        // User/Auth Service endpoints
        const val REGISTER = "auth/register"
        const val LOGIN = "auth/login"
        const val VERIFY_TOKEN = "auth/verify-token"
        const val REFRESH_TOKEN = "auth/refresh-token"
        const val GET_USER_PROFILE = "users/me"
        const val UPDATE_USER_PROFILE = "users/me"
        
        // Seller/Shop Service endpoints
        const val CREATE_SHOP = "shops"
        const val GET_MY_SHOP = "shops/me"
        const val UPDATE_MY_SHOP = "shops/me"
        const val GET_SHOP_BY_ID = "shops/{id}"
        const val UPLOAD_SHOP_IMAGE = "shops/me/images/upload"
        
        // Inventory Service endpoints
        const val GET_VENDOR_PRODUCTS = "inventory"
        const val ADD_PRODUCT_FROM_CATALOG = "inventory/add-from-catalog"
        const val UPDATE_INVENTORY_ITEM = "inventory/{id}"
        const val DELETE_INVENTORY_ITEM = "inventory/{id}"
        
        // Customer Service endpoints
        const val GET_ALL_SHOPS = "shops"
        const val SEARCH_SHOPS = "shops/search"
        const val GET_SHOP_PRODUCTS = "shops/{id}/products"
        
        // Catalog Service endpoints
        const val GET_CATALOG = "items"
        const val GET_CATALOG_CATEGORIES = "categories"
        const val GET_PRODUCT_BY_ID = "items/{id}"
        const val SEARCH_CATALOG = "items/search"
        
        // Admin Service endpoints
        const val ADMIN_STATS = "stats"
        const val ADMIN_USERS = "users"
        const val ADMIN_SHOPS = "shops"
    }
    
    // Firebase Configuration
    object Firebase {
        // Firebase project configuration
        const val PROJECT_ID = "hyperlocal-marketplace"
        
        // IMPORTANT: Replace this with your actual Web Client ID from Firebase Console
        // Go to: Firebase Console > Project Settings > General > Your apps > Web app
        // Copy the "Web client ID" from OAuth 2.0 client IDs
        const val WEB_CLIENT_ID = "YOUR_ACTUAL_WEB_CLIENT_ID_HERE"
        
        // For development/testing, you can temporarily disable Google Sign-In
        const val ENABLE_GOOGLE_SIGNIN = false
        
        // Note: To enable Google Sign-In:
        // 1. Replace WEB_CLIENT_ID with actual value from Firebase Console
        // 2. Add SHA-1 certificate fingerprint to Firebase project
        // 3. Download updated google-services.json
        // 4. Set ENABLE_GOOGLE_SIGNIN = true
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
    
    // Debug Configuration
    object Debug {
        const val ENABLE_NETWORK_LOGGING = IS_DEBUG
        const val ENABLE_AUTH_LOGGING = IS_DEBUG
        const val ENABLE_API_LOGGING = IS_DEBUG
        
        // Test credentials for development
        const val TEST_EMAIL = "test@example.com"
        const val TEST_PASSWORD = "password123"
        const val TEST_PHONE = "+1234567890"
        
        // Backend connectivity test endpoints
        fun getHealthCheckUrls(): List<String> = listOf(
            "$USER_SERVICE_URL/health",
            "$SELLER_SERVICE_URL/health", 
            "$CUSTOMER_SERVICE_URL/health",
            "$CATALOG_SERVICE_URL/health",
            "$ADMIN_SERVICE_URL/health"
        )
    }
}