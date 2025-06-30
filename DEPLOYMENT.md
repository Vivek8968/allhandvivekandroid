# Production Deployment Guide

## 🚀 Preparing for Production

### 1. Backend Configuration

Update `app/src/main/java/com/hyperlocal/marketplace/config/Config.kt`:

```kotlin
object Config {
    // 🔧 PRODUCTION BACKEND URLS - UPDATE THESE
    val USER_SERVICE_URL = "https://your-api-domain.com/user"
    val SELLER_SERVICE_URL = "https://your-api-domain.com/seller"
    val CUSTOMER_SERVICE_URL = "https://your-api-domain.com/customer"
    val CATALOG_SERVICE_URL = "https://your-api-domain.com/catalog"
    val ADMIN_SERVICE_URL = "https://your-api-domain.com/admin"
    
    // 🔧 PRODUCTION SETTINGS
    val IS_DEBUG_MODE = false
    val ENABLE_DEMO_DATA = false
    
    // 🔧 API CONFIGURATION
    val API_TIMEOUT_SECONDS = 30L
    val RETRY_ATTEMPTS = 3
    val CACHE_SIZE_MB = 10L
}
```

### 2. Firebase Configuration

#### Replace Firebase Config:
1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Create/select your production project
3. Download `google-services.json`
4. Replace `app/google-services.json` with production file

#### Update Firebase Settings:
- Enable Authentication methods (Phone, Google)
- Configure OAuth 2.0 client IDs
- Set up Cloud Messaging (for push notifications)
- Configure Firestore rules (if using)

### 3. Security Configuration

#### API Security:
```kotlin
// Add to Config.kt
object Config {
    // 🔐 API SECURITY
    val API_KEY = "your-production-api-key"
    val JWT_SECRET = "your-jwt-secret"
    
    // 🔐 ENCRYPTION
    val ENABLE_SSL_PINNING = true
    val CERTIFICATE_PINS = listOf(
        "sha256/your-certificate-pin"
    )
}
```

#### Network Security:
Update `app/src/main/res/xml/network_security_config.xml`:
```xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <domain-config cleartextTrafficPermitted="false">
        <domain includeSubdomains="true">your-api-domain.com</domain>
        <pin-set>
            <pin digest="SHA-256">your-certificate-pin</pin>
        </pin-set>
    </domain-config>
</network-security-config>
```

### 4. Build Configuration

#### Update `app/build.gradle`:

```gradle
android {
    compileSdk 34
    
    defaultConfig {
        applicationId "com.hyperlocal.marketplace"
        minSdk 26
        targetSdk 34
        versionCode 1
        versionName "1.0.0"
    }
    
    buildTypes {
        release {
            minifyEnabled true
            shrinkResources true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
            
            // 🔐 SIGNING CONFIG
            signingConfig signingConfigs.release
        }
    }
    
    // 🔐 SIGNING CONFIGURATION
    signingConfigs {
        release {
            storeFile file('path/to/your/keystore.jks')
            storePassword 'your-keystore-password'
            keyAlias 'your-key-alias'
            keyPassword 'your-key-password'
        }
    }
}
```

### 5. ProGuard Configuration

Create/update `app/proguard-rules.pro`:

```proguard
# Keep data models
-keep class com.hyperlocal.marketplace.data.models.** { *; }

# Keep API interfaces
-keep interface com.hyperlocal.marketplace.data.api.** { *; }

# Retrofit
-keepattributes Signature, InnerClasses, EnclosingMethod
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# Gson
-keepattributes Signature
-keep class com.google.gson.reflect.TypeToken { *; }
-keep class * extends com.google.gson.reflect.TypeToken

# Hilt
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.android.lifecycle.HiltViewModel { *; }
```

## 🔑 Keystore Generation

### Create Release Keystore:

```bash
keytool -genkey -v -keystore hyperlocal-release.jks \
    -keyalg RSA -keysize 2048 -validity 10000 \
    -alias hyperlocal-key
```

### Store Keystore Securely:
- Never commit keystore to version control
- Use environment variables or secure storage
- Keep backup of keystore and passwords

## 🏗️ Production Build Process

### 1. Pre-build Checklist:
- [ ] Updated all service URLs to production
- [ ] Disabled debug mode and demo data
- [ ] Replaced Firebase config with production
- [ ] Configured signing with release keystore
- [ ] Updated version code and name
- [ ] Tested with production backend

### 2. Build Release APK:

```bash
# Clean previous builds
./gradlew clean

# Build release APK
./gradlew assembleRelease

# Or build AAB for Play Store
./gradlew bundleRelease
```

### 3. Verify Release Build:

```bash
# Check APK location
ls -la app/build/outputs/apk/release/

# Verify signing
jarsigner -verify -verbose -certs app/build/outputs/apk/release/app-release.apk

# Check APK contents
aapt dump badging app/build/outputs/apk/release/app-release.apk
```

## 📱 App Store Deployment

### Google Play Store:

1. **Prepare Store Listing:**
   - App title: "Hyperlocal Marketplace"
   - Description highlighting key features
   - Screenshots from all three user roles
   - Privacy policy URL
   - App category: Shopping

2. **Upload AAB:**
   ```bash
   ./gradlew bundleRelease
   ```
   Upload `app/build/outputs/bundle/release/app-release.aab`

3. **Configure Release:**
   - Set target API level
   - Configure app signing
   - Set up staged rollout
   - Add release notes

### Alternative Distribution:

1. **Direct APK Distribution:**
   - Host APK on secure server
   - Provide download link
   - Include installation instructions

2. **Enterprise Distribution:**
   - Use MDM solutions
   - Configure enterprise certificates
   - Set up internal app store

## 🔍 Testing Production Build

### Pre-release Testing:

1. **Internal Testing:**
   - Test with production backend
   - Verify all API integrations
   - Test authentication flows
   - Validate data persistence

2. **User Acceptance Testing:**
   - Test all three user roles
   - Verify business workflows
   - Test edge cases and error scenarios
   - Performance testing

3. **Security Testing:**
   - API security validation
   - Data encryption verification
   - Authentication security
   - Network security testing

## 📊 Monitoring & Analytics

### Crash Reporting:
```gradle
// Add to app/build.gradle
implementation 'com.google.firebase:firebase-crashlytics'
implementation 'com.google.firebase:firebase-analytics'
```

### Performance Monitoring:
```gradle
implementation 'com.google.firebase:firebase-perf'
```

### Custom Analytics:
```kotlin
// Add to relevant ViewModels
FirebaseAnalytics.getInstance(context).logEvent("user_login") {
    param("user_role", userRole.name)
    param("login_method", "phone")
}
```

## 🔄 CI/CD Pipeline

### GitHub Actions Example:

```yaml
name: Build and Deploy
on:
  push:
    branches: [ main ]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v3
    
    - name: Set up JDK 17
      uses: actions/setup-java@v3
      with:
        java-version: '17'
        distribution: 'temurin'
        
    - name: Setup Android SDK
      uses: android-actions/setup-android@v2
      
    - name: Build Release APK
      run: ./gradlew assembleRelease
      
    - name: Upload APK
      uses: actions/upload-artifact@v3
      with:
        name: app-release
        path: app/build/outputs/apk/release/app-release.apk
```

## 🚨 Production Checklist

### Before Release:
- [ ] All service URLs updated to production
- [ ] Demo mode disabled
- [ ] Firebase production config added
- [ ] Release keystore configured
- [ ] ProGuard rules tested
- [ ] Version code incremented
- [ ] Privacy policy updated
- [ ] Terms of service added
- [ ] App permissions reviewed
- [ ] Security testing completed
- [ ] Performance testing passed
- [ ] Backend integration verified
- [ ] User acceptance testing completed

### After Release:
- [ ] Monitor crash reports
- [ ] Track user analytics
- [ ] Monitor API performance
- [ ] Collect user feedback
- [ ] Plan next iteration
- [ ] Update documentation
- [ ] Backup release artifacts

## 📞 Support & Maintenance

### Monitoring:
- Set up alerts for crash rates
- Monitor API response times
- Track user engagement metrics
- Monitor app store reviews

### Updates:
- Plan regular security updates
- Monitor dependency vulnerabilities
- Update Android target SDK annually
- Maintain backward compatibility

### Documentation:
- Keep deployment guide updated
- Document configuration changes
- Maintain API documentation
- Update user guides

---

**🎯 Remember: Always test thoroughly in a staging environment before production deployment!**