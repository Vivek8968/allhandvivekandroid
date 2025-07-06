package com.hyperlocal.marketplace

import android.os.Bundle
import android.widget.Button
import android.widget.ScrollView
import android.widget.TextView
import android.widget.LinearLayout
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.hyperlocal.marketplace.config.Config
import com.hyperlocal.marketplace.utils.BackendConnectivityTest
import com.hyperlocal.marketplace.utils.NetworkErrorHandler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * Debug activity for testing all API configurations and connectivity
 */
@AndroidEntryPoint
class DebugActivity : ComponentActivity() {
    
    private lateinit var outputTextView: TextView
    private lateinit var scrollView: ScrollView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 32, 32, 32)
        }
        
        // Title
        val titleText = TextView(this).apply {
            text = "🔧 HyperLocal Marketplace Debug Console"
            textSize = 20f
            setPadding(0, 0, 0, 32)
        }
        layout.addView(titleText)
        
        // Test buttons
        val testConnectivityButton = Button(this).apply {
            text = "🌐 Test Backend Connectivity"
            setOnClickListener { testBackendConnectivity() }
        }
        layout.addView(testConnectivityButton)
        
        val testConfigButton = Button(this).apply {
            text = "⚙️ Test Configuration"
            setOnClickListener { testConfiguration() }
        }
        layout.addView(testConfigButton)
        
        val testApiEndpointsButton = Button(this).apply {
            text = "🔗 Test API Endpoints"
            setOnClickListener { testApiEndpoints() }
        }
        layout.addView(testApiEndpointsButton)
        
        val clearButton = Button(this).apply {
            text = "🗑️ Clear Output"
            setOnClickListener { clearOutput() }
        }
        layout.addView(clearButton)
        
        // Output text view
        outputTextView = TextView(this).apply {
            text = "Debug console ready. Click buttons above to run tests.\n\n"
            textSize = 12f
            setPadding(16, 16, 16, 16)
            setBackgroundColor(0xFF1E1E1E.toInt())
            setTextColor(0xFF00FF00.toInt())
        }
        
        scrollView = ScrollView(this).apply {
            addView(outputTextView)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
        }
        layout.addView(scrollView)
        
        setContentView(layout)
        
        // Auto-run connectivity test on start
        testBackendConnectivity()
    }
    
    private fun appendOutput(text: String) {
        runOnUiThread {
            outputTextView.text = "${outputTextView.text}$text\n"
            scrollView.post {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN)
            }
        }
    }
    
    private fun clearOutput() {
        outputTextView.text = "Debug console cleared.\n\n"
    }
    
    private fun testBackendConnectivity() {
        appendOutput("🔄 Starting backend connectivity test...")
        
        lifecycleScope.launch {
            try {
                val connectivityTest = BackendConnectivityTest(this@DebugActivity)
                val results = connectivityTest.testAllServices()
                
                appendOutput("\n📊 Connectivity Results:")
                results.forEach { status ->
                    val icon = if (status.isReachable) "✅" else "❌"
                    val time = "${status.responseTime}ms"
                    val error = status.errorMessage?.let { " - $it" } ?: ""
                    appendOutput("$icon ${status.serviceName} ($time)$error")
                }
                
                val reachableCount = results.count { it.isReachable }
                appendOutput("\n📈 Summary: $reachableCount/${results.size} services reachable")
                
                if (reachableCount == 0) {
                    appendOutput("🚨 No services reachable!")
                    appendOutput("💡 Check if backend is running on 192.168.1.3:8001-8005")
                } else if (reachableCount < results.size) {
                    appendOutput("⚠️ Some services are down")
                } else {
                    appendOutput("🎉 All services are reachable!")
                }
                
            } catch (e: Exception) {
                appendOutput("❌ Connectivity test failed: ${e.message}")
            }
        }
    }
    
    private fun testConfiguration() {
        appendOutput("🔄 Testing configuration...")
        
        appendOutput("\n📋 Current Configuration:")
        appendOutput("• Debug Mode: ${Config.Debug.ENABLE_API_LOGGING}")
        appendOutput("• Network Logging: ${Config.Debug.ENABLE_NETWORK_LOGGING}")
        appendOutput("• Base Host: 192.168.1.3")
        
        appendOutput("\n🌐 Service URLs:")
        appendOutput("• User Service: ${Config.USER_SERVICE_URL}")
        appendOutput("• Seller Service: ${Config.SELLER_SERVICE_URL}")
        appendOutput("• Customer Service: ${Config.CUSTOMER_SERVICE_URL}")
        appendOutput("• Catalog Service: ${Config.CATALOG_SERVICE_URL}")
        appendOutput("• Admin Service: ${Config.ADMIN_SERVICE_URL}")
        
        appendOutput("\n⏱️ Network Timeouts:")
        appendOutput("• Connect: ${Config.Network.CONNECT_TIMEOUT_SECONDS}s")
        appendOutput("• Read: ${Config.Network.READ_TIMEOUT_SECONDS}s")
        appendOutput("• Write: ${Config.Network.WRITE_TIMEOUT_SECONDS}s")
        
        appendOutput("\n🔧 Health Check URLs:")
        Config.Debug.getHealthCheckUrls().forEachIndexed { index, url ->
            val serviceName = listOf("User", "Seller", "Customer", "Catalog", "Admin")[index]
            appendOutput("• $serviceName: $url")
        }
        
        appendOutput("\n✅ Configuration test completed")
    }
    
    private fun testApiEndpoints() {
        appendOutput("🔄 Testing API endpoint mappings...")
        
        lifecycleScope.launch {
            try {
                val connectivityTest = BackendConnectivityTest(this@DebugActivity)
                
                appendOutput("\n🔗 Testing specific endpoints:")
                
                // Test User Service endpoints
                appendOutput("\n📱 User Service Endpoints:")
                val userEndpoints = listOf(
                    "/auth/firebase-login",
                    "/auth/register",
                    "/auth/verify-token",
                    "/profile"
                )
                
                userEndpoints.forEach { endpoint ->
                    val result = connectivityTest.testApiEndpoint(
                        "User Service",
                        Config.USER_SERVICE_URL,
                        endpoint,
                        "POST"
                    )
                    val icon = if (result.isReachable) "✅" else "❌"
                    val error = result.errorMessage?.let { " - $it" } ?: ""
                    appendOutput("  $icon $endpoint (${result.responseTime}ms)$error")
                }
                
                // Test Seller Service endpoints
                appendOutput("\n🏪 Seller Service Endpoints:")
                val sellerEndpoints = listOf(
                    "/shops",
                    "/shops/my",
                    "/inventory",
                    "/upload/shop-image"
                )
                
                sellerEndpoints.forEach { endpoint ->
                    val result = connectivityTest.testApiEndpoint(
                        "Seller Service",
                        Config.SELLER_SERVICE_URL,
                        endpoint,
                        "GET"
                    )
                    val icon = if (result.isReachable) "✅" else "❌"
                    val error = result.errorMessage?.let { " - $it" } ?: ""
                    appendOutput("  $icon $endpoint (${result.responseTime}ms)$error")
                }
                
                // Test Customer Service endpoints
                appendOutput("\n🛒 Customer Service Endpoints:")
                val customerEndpoints = listOf(
                    "/shops",
                    "/shops/nearby",
                    "/orders",
                    "/orders/history"
                )
                
                customerEndpoints.forEach { endpoint ->
                    val result = connectivityTest.testApiEndpoint(
                        "Customer Service",
                        Config.CUSTOMER_SERVICE_URL,
                        endpoint,
                        "GET"
                    )
                    val icon = if (result.isReachable) "✅" else "❌"
                    val error = result.errorMessage?.let { " - $it" } ?: ""
                    appendOutput("  $icon $endpoint (${result.responseTime}ms)$error")
                }
                
                // Test Catalog Service endpoints
                appendOutput("\n📦 Catalog Service Endpoints:")
                val catalogEndpoints = listOf(
                    "/items",
                    "/categories",
                    "/search"
                )
                
                catalogEndpoints.forEach { endpoint ->
                    val result = connectivityTest.testApiEndpoint(
                        "Catalog Service",
                        Config.CATALOG_SERVICE_URL,
                        endpoint,
                        "GET"
                    )
                    val icon = if (result.isReachable) "✅" else "❌"
                    val error = result.errorMessage?.let { " - $it" } ?: ""
                    appendOutput("  $icon $endpoint (${result.responseTime}ms)$error")
                }
                
                appendOutput("\n✅ API endpoint testing completed")
                
            } catch (e: Exception) {
                appendOutput("❌ API endpoint test failed: ${NetworkErrorHandler.handleNetworkException(e)}")
            }
        }
    }
}