# Hyperlocal Marketplace Android App

A comprehensive hyperlocal marketplace Android application built with Kotlin and Jetpack Compose that connects to a backend microservices architecture. The app supports three user roles: **Customer**, **Seller**, and **Admin**.

## 🏗️ Architecture

- **MVVM Architecture** with Repository Pattern
- **Jetpack Compose** for modern UI
- **Hilt** for Dependency Injection
- **Retrofit** for API communication
- **Material 3** design system
- **StateFlow** for reactive state management

## 📱 Features

### Customer Module
- 🗺️ Explore shops by geolocation
- 📂 Browse categories & products
- 🏪 View shop details with WhatsApp contact
- 🔍 Search products
- 🛒 Place demo orders

### Seller Module
- 📝 Register as seller with shop information
- 📦 Upload inventory (manual or voice-to-text)
- 📱 Generate QR code for digital storefront
- 📋 View incoming orders
- 📊 Shop management dashboard

### Admin Module
- 👤 Admin authentication
- 👥 View sellers, shops, and customers
- ✅ Approve/Reject shop onboarding
- 📈 Analytics dashboard

## 🔧 Setup Instructions

### Prerequisites
- Android Studio Arctic Fox or later
- JDK 17
- Android SDK API 26+
- Git

### Building the Project

1. **Clone the repository:**
   ```bash
   git clone <repository-url>
   cd HyperlocalMarketplace
   ```

2. **Open in Android Studio:**
   - Open Android Studio
   - Select "Open an existing project"
   - Navigate to the project directory

3. **Configure Firebase (Optional):**
   - Replace `google-services.json` with your Firebase configuration
   - Update Firebase project settings in the file

4. **Build the project:**
   ```bash
   ./gradlew assembleDebug
   ```

5. **Install on device:**
   ```bash
   ./gradlew installDebug
   ```

## ⚙️ Configuration

### Backend Configuration

Update the backend service URLs in `Config.kt`:

```kotlin
object Config {
    // Backend Service URLs - Update these for production
    val USER_SERVICE_URL = "http://10.0.2.2:8001"
    val SELLER_SERVICE_URL = "http://10.0.2.2:8002"
    val CUSTOMER_SERVICE_URL = "http://10.0.2.2:8003"
    val CATALOG_SERVICE_URL = "http://10.0.2.2:8004"
    val ADMIN_SERVICE_URL = "http://10.0.2.2:8005"
    
    // App Configuration
    val IS_DEBUG_MODE = true  // Set to false for production
    val ENABLE_DEMO_DATA = true  // Set to false to use real backend
}
```

### Environment Switching

#### For Development (Android Emulator):
- Use `10.0.2.2` as the host (maps to localhost on development machine)
- Keep `IS_DEBUG_MODE = true`
- Keep `ENABLE_DEMO_DATA = true` for testing without backend

#### For Production:
1. Update service URLs to production endpoints:
   ```kotlin
   val USER_SERVICE_URL = "https://your-production-api.com/user"
   val SELLER_SERVICE_URL = "https://your-production-api.com/seller"
   // ... update all service URLs
   ```

2. Disable debug mode:
   ```kotlin
   val IS_DEBUG_MODE = false
   val ENABLE_DEMO_DATA = false
   ```

3. Update Firebase configuration with production keys

## 🧪 Testing Instructions

### Demo Mode Testing
The app includes comprehensive demo data for testing without a backend:

1. **Enable Demo Mode:**
   - Set `ENABLE_DEMO_DATA = true` in `Config.kt`
   - Launch the app

2. **Test Customer Flow:**
   - Select "Customer" role
   - Browse demo shops and products
   - Test search functionality
   - View shop details

3. **Test Seller Flow:**
   - Select "Seller" role
   - View demo shop dashboard
   - Browse inventory management
   - Test product addition flow

4. **Test Admin Flow:**
   - Select "Admin" role
   - View analytics dashboard
   - Browse pending shop approvals
   - Test approval/rejection flow

### Backend Integration Testing

1. **Start Backend Services:**
   ```bash
   # Clone and start the backend
   git clone https://github.com/Vivek8968/hyperlocalbymanus.git
   cd hyperlocalbymanus
   # Follow backend setup instructions
   ```

2. **Configure App for Backend:**
   - Set `ENABLE_DEMO_DATA = false` in `Config.kt`
   - Ensure backend URLs are correct
   - Test API connectivity

3. **Test API Flows:**
   - User registration and authentication
   - Shop creation and management
   - Product catalog operations
   - Admin operations

## 📂 Project Structure

```
app/src/main/java/com/hyperlocal/marketplace/
├── config/
│   └── Config.kt                 # App configuration
├── data/
│   ├── api/                      # API service interfaces
│   ├── models/                   # Data models
│   └── repository/               # Repository implementations
├── di/
│   └── NetworkModule.kt          # Dependency injection
├── presentation/
│   ├── admin/                    # Admin UI and ViewModels
│   ├── auth/                     # Authentication UI
│   ├── customer/                 # Customer UI and ViewModels
│   ├── seller/                   # Seller UI and ViewModels
│   └── MainActivity.kt           # Main activity
├── ui/
│   └── theme/                    # Material 3 theme
└── utils/                        # Utility classes
```

## 🔑 API Integration

The app integrates with 5 microservices:

1. **User Service** (Port 8001) - Authentication and user management
2. **Seller Service** (Port 8002) - Seller operations and shop management
3. **Customer Service** (Port 8003) - Customer operations and orders
4. **Catalog Service** (Port 8004) - Product catalog and inventory
5. **Admin Service** (Port 8005) - Admin operations and analytics

### API Endpoints Used:

#### User Service
- `POST /api/users/register` - User registration
- `POST /api/users/login` - User authentication
- `GET /api/users/profile` - Get user profile

#### Seller Service
- `POST /api/sellers/register` - Seller registration
- `GET /api/sellers/shops` - Get seller shops
- `POST /api/sellers/shops` - Create new shop

#### Customer Service
- `GET /api/customers/nearby-shops` - Get nearby shops
- `POST /api/customers/orders` - Place order

#### Catalog Service
- `GET /api/catalog/products` - Get products
- `POST /api/catalog/products` - Add product
- `GET /api/catalog/categories` - Get categories

#### Admin Service
- `GET /api/admin/stats` - Get admin statistics
- `GET /api/admin/pending-shops` - Get pending shop approvals
- `PUT /api/admin/shops/{id}/approve` - Approve shop

## 🚀 Deployment

### Debug APK
The debug APK is generated at: `app/build/outputs/apk/debug/app-debug.apk`

### Release Build
1. Configure signing in `app/build.gradle`
2. Build release APK:
   ```bash
   ./gradlew assembleRelease
   ```

## 🔒 Security Notes

- Firebase authentication is configured but requires proper setup
- API calls include basic error handling and retry mechanisms
- User tokens are stored securely using Android's encrypted preferences
- Network security config is enabled for HTTPS enforcement

## 🐛 Troubleshooting

### Common Issues:

1. **Build Errors:**
   - Ensure JDK 17 is installed and configured
   - Clean and rebuild: `./gradlew clean assembleDebug`

2. **Network Issues:**
   - Check backend service URLs in `Config.kt`
   - Verify emulator can reach localhost (use 10.0.2.2)
   - Enable demo mode for offline testing

3. **Firebase Issues:**
   - Verify `google-services.json` is properly configured
   - Check Firebase project settings

## 📋 TODO / Future Enhancements

- [ ] Implement Firebase Authentication integration
- [ ] Add Google Maps integration for location services
- [ ] Implement image upload with camera/gallery
- [ ] Add real-time location tracking
- [ ] Implement push notifications
- [ ] Add offline caching with Room database
- [ ] Create comprehensive unit and integration tests
- [ ] Add performance monitoring and analytics
- [ ] Implement WhatsApp integration for shop communication

## 📄 License

This project is part of a hyperlocal marketplace solution. Please refer to the main repository for licensing information.

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## 📞 Support

For issues and questions:
- Check the troubleshooting section
- Review the backend repository: https://github.com/Vivek8968/hyperlocalbymanus.git
- Create an issue in the repository

---

**Built with ❤️ using Kotlin and Jetpack Compose**