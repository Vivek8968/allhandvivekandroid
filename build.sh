#!/bin/bash

# Hyperlocal Marketplace Android Build Script
# This script builds the Android APK with proper environment setup

echo "🏗️  Building Hyperlocal Marketplace Android App..."
echo "=================================================="

# Set environment variables
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export ANDROID_HOME=/workspace/android-sdk

# Check if Java is available
if [ ! -d "$JAVA_HOME" ]; then
    echo "❌ Java 17 not found at $JAVA_HOME"
    echo "Please install Java 17 JDK"
    exit 1
fi

# Check if Android SDK is available
if [ ! -d "$ANDROID_HOME" ]; then
    echo "❌ Android SDK not found at $ANDROID_HOME"
    echo "Please install Android SDK"
    exit 1
fi

echo "✅ Java Home: $JAVA_HOME"
echo "✅ Android Home: $ANDROID_HOME"
echo ""

# Clean previous builds
echo "🧹 Cleaning previous builds..."
./gradlew clean

# Build debug APK
echo "🔨 Building debug APK..."
./gradlew assembleDebug

# Check if build was successful
if [ $? -eq 0 ]; then
    echo ""
    echo "🎉 Build successful!"
    echo "📱 APK location: app/build/outputs/apk/debug/app-debug.apk"
    
    # Show APK details
    APK_PATH="app/build/outputs/apk/debug/app-debug.apk"
    if [ -f "$APK_PATH" ]; then
        APK_SIZE=$(du -h "$APK_PATH" | cut -f1)
        echo "📊 APK size: $APK_SIZE"
        echo ""
        echo "🚀 To install on device/emulator:"
        echo "   adb install $APK_PATH"
        echo ""
        echo "📋 To test the app:"
        echo "   1. Install APK on Android device (API 26+)"
        echo "   2. Launch the app"
        echo "   3. Follow test_app.md for testing guide"
        echo "   4. Demo mode is enabled by default"
    fi
else
    echo ""
    echo "❌ Build failed!"
    echo "Check the error messages above for details"
    exit 1
fi

echo ""
echo "📚 Next steps:"
echo "   - Review README.md for configuration"
echo "   - Follow test_app.md for testing"
echo "   - Update Config.kt for production deployment"
echo ""
echo "✨ Happy testing!"