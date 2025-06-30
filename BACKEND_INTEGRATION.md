# Backend Integration Documentation

## 🎯 Overview

This document describes the successful integration between the Android Hyperlocal Marketplace app and the backend API gateway. The integration demonstrates end-to-end connectivity between the mobile app and the backend services.

## 🏗️ Architecture

### Backend Components
- **API Gateway**: Running on port 12000, provides unified access to all services
- **User Service**: Handles authentication and user management
- **Customer Service**: Manages customer-specific operations
- **Seller Service**: Handles seller and shop management
- **Admin Service**: Provides administrative functions

### Android Components
- **API Services**: Retrofit interfaces for each backend service
- **Data Models**: Kotlin data classes matching API response format
- **Repositories**: Data access layer with proper error handling
- **Configuration**: Centralized config for API endpoints

## 🔗 API Integration

### Base Configuration
```kotlin
// Config.kt
object Config {
    const val BASE_URL = "http://10.0.2.2:12000"  // For Android emulator
    // For physical device: "http://192.168.1.100:12000"
}
```

### API Response Format
All API endpoints return responses in this standardized format:
```json
{
  "status": true,
  "message": "Success",
  "data": { ... }
}
```

### Integrated Endpoints

#### 1. Authentication
- **POST** `/auth/login` - Phone-based login
- **POST** `/auth/register` - User registration
- **POST** `/auth/verify-otp` - OTP verification

#### 2. Shops Management
- **GET** `/shops` - Get all shops with location filtering
- **GET** `/shops/{id}` - Get shop details
- **POST** `/shops` - Create new shop (seller)
- **GET** `/shops/{id}/products` - Get shop products

#### 3. Catalog Management
- **GET** `/catalog` - Get product catalog
- **GET** `/catalog/categories` - Get product categories
- **GET** `/products/{id}` - Get product details

#### 4. Vendor Operations
- **GET** `/vendor/shop` - Get vendor's shop
- **GET** `/vendor/products` - Get vendor's products
- **POST** `/vendor/products/from-catalog` - Add product from catalog
- **POST** `/shops/{id}/products` - Add custom product

## 📱 Android Implementation

### API Services
Each service is implemented as a Retrofit interface:

```kotlin
@GET("shops")
suspend fun getAllShops(
    @Query("latitude") latitude: Double? = null,
    @Query("longitude") longitude: Double? = null
): Response<ApiResponse<List<Shop>>>
```

### Data Models
Models are designed to match the API response structure:

```kotlin
data class Shop(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("address") val address: String,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("category") val category: String,
    @SerializedName("rating") val rating: Double,
    @SerializedName("imageUrl") val imageUrl: String?,
    @SerializedName("ownerId") val ownerId: String,
    @SerializedName("isOpen") val isOpen: Boolean = true,
    @SerializedName("openingTime") val openingTime: String = "09:00",
    @SerializedName("closingTime") val closingTime: String = "21:00"
) : Parcelable
```

### Repository Pattern
Repositories handle API calls with proper error handling:

```kotlin
suspend fun getAllShops(
    latitude: Double? = null,
    longitude: Double? = null
): Flow<Resource<List<Shop>>> = flow {
    try {
        emit(Resource.Loading())
        val response = customerApiService.getAllShops(latitude, longitude)
        if (response.isSuccessful) {
            response.body()?.let { apiResponse ->
                if (apiResponse.status && apiResponse.data != null) {
                    emit(Resource.Success(apiResponse.data))
                } else {
                    emit(Resource.Error(apiResponse.message))
                }
            } ?: emit(Resource.Error("Empty response body"))
        } else {
            emit(Resource.Error("Failed to get shops: ${response.message()}"))
        }
    } catch (e: Exception) {
        emit(Resource.Error("Network error: ${e.localizedMessage}"))
    }
}
```

## 🧪 Testing

### Backend Integration Test
The app includes a `TestBackendActivity` that verifies:

1. **Login Endpoint**: Tests phone-based authentication
2. **Shops Endpoint**: Retrieves and displays shop data
3. **Catalog Endpoint**: Fetches product catalog

### Test Results
When the app runs, it automatically tests all endpoints and displays:
- ✅ Login successful: User Name
- ✅ Shops loaded: X shops found
- ✅ Catalog loaded: Y items found

## 🚀 Deployment

### Backend Deployment
1. Start the API gateway:
   ```bash
   cd /workspace/backend
   python api_gateway.py
   ```
   Server runs on `http://localhost:12000`

### Android Deployment
1. Build the APK:
   ```bash
   cd /workspace/HyperlocalMarketplace
   ./gradlew assembleDebug
   ```
   APK location: `app/build/outputs/apk/debug/app-debug.apk`

2. Install on device/emulator:
   ```bash
   adb install app/build/outputs/apk/debug/app-debug.apk
   ```

## 🔧 Configuration

### Network Configuration
- **Emulator**: Use `10.0.2.2:12000` (maps to host localhost)
- **Physical Device**: Use actual IP address (e.g., `192.168.1.100:12000`)
- **Production**: Update `Config.BASE_URL` to production server

### Security
- Uses `android:usesCleartextTraffic="true"` for development
- For production, implement HTTPS and remove cleartext traffic

## 📊 Current Status

### ✅ Completed
- [x] Backend API gateway running and tested
- [x] Android API services implemented
- [x] Data models matching API structure
- [x] Repository pattern with error handling
- [x] Network configuration for emulator
- [x] APK build successful (19MB)
- [x] End-to-end connectivity verified

### 🔄 In Progress
- [ ] Complete UI integration with new API structure
- [ ] Firebase authentication integration
- [ ] Location-based shop filtering
- [ ] Image upload functionality

### 📋 Next Steps
1. Update ViewModels to use new repository methods
2. Integrate Firebase Auth with backend authentication
3. Implement location services for nearby shops
4. Add image upload for shop and product photos
5. Complete seller dashboard functionality
6. Implement admin approval workflow

## 🐛 Known Issues

1. **UI Components**: Temporarily disabled due to API structure changes
2. **Authentication**: Firebase integration pending
3. **Location Services**: GPS integration not yet implemented

## 📞 Support

For technical issues or questions about the integration:
- Check API gateway logs for backend issues
- Use Android Studio debugger for app issues
- Verify network connectivity between app and backend

## 🔗 Related Files

- `Config.kt` - API configuration
- `TestBackendActivity.kt` - Integration testing
- `data/api/` - API service interfaces
- `data/models/` - Data models
- `data/repository/` - Repository implementations