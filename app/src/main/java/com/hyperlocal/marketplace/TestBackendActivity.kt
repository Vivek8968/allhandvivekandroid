package com.hyperlocal.marketplace

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.hyperlocal.marketplace.config.Config
import com.hyperlocal.marketplace.data.models.*
import com.hyperlocal.marketplace.utils.BackendConnectivityTest
import com.hyperlocal.marketplace.utils.NetworkErrorHandler
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.Response

// Simple API interfaces for testing each microservice
interface UserTestApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<ApiResponse<LoginResponseData>>
}

interface CustomerTestApiService {
    @GET("shops")
    suspend fun getShops(): Response<ApiResponse<List<Shop>>>
}

interface CatalogTestApiService {
    @GET("items")
    suspend fun getCatalog(): Response<ApiResponse<List<CatalogItem>>>
}

class TestBackendActivity : ComponentActivity() {
    
    private val userApiService by lazy {
        Retrofit.Builder()
            .baseUrl(Config.USER_SERVICE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserTestApiService::class.java)
    }
    
    private val customerApiService by lazy {
        Retrofit.Builder()
            .baseUrl(Config.CUSTOMER_SERVICE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CustomerTestApiService::class.java)
    }
    
    private val catalogApiService by lazy {
        Retrofit.Builder()
            .baseUrl(Config.CATALOG_SERVICE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CatalogTestApiService::class.java)
    }
    
    private lateinit var textView: TextView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        textView = TextView(this).apply {
            text = "Testing Microservices Backend Connection...\n" +
                   "User Service: ${Config.USER_SERVICE_URL}\n" +
                   "Customer Service: ${Config.CUSTOMER_SERVICE_URL}\n" +
                   "Catalog Service: ${Config.CATALOG_SERVICE_URL}"
            textSize = 16f
            setPadding(32, 32, 32, 32)
        }
        setContentView(textView)
        
        testBackendConnection()
    }
    
    private fun testBackendConnection() {
        val results = StringBuilder()
        results.append("🔄 Testing microservices backend connection...\n\n")
        
        lifecycleScope.launch {
            try {
                // First, test basic connectivity to all services
                results.append("🌐 Testing basic connectivity to all services...\n\n")
                val connectivityTest = BackendConnectivityTest(this@TestBackendActivity)
                val connectivityResults = connectivityTest.testAllServices()
                
                connectivityResults.forEach { status ->
                    val icon = if (status.isReachable) "✅" else "❌"
                    val time = "${status.responseTime}ms"
                    val error = status.errorMessage?.let { " - $it" } ?: ""
                    results.append("$icon ${status.serviceName} ($time)$error\n")
                }
                results.append("\n")
                
                val reachableServices = connectivityResults.count { it.isReachable }
                if (reachableServices == 0) {
                    results.append("🚨 No services are reachable! Check backend configuration.\n")
                    results.append("💡 Make sure backend services are running on 192.168.1.3:8001-8005\n\n")
                } else {
                    results.append("📊 $reachableServices/${connectivityResults.size} services reachable\n\n")
                    
                    // Test specific API endpoints if services are reachable
                    if (connectivityResults.any { it.serviceName.contains("User") && it.isReachable }) {
                        results.append("📱 Testing User Service API endpoints...\n")
                        try {
                            val loginResponse = userApiService.login(LoginRequest("1234567890"))
                            if (loginResponse.isSuccessful) {
                                val body = loginResponse.body()
                                if (body?.status == true) {
                                    results.append("✅ Login endpoint working\n")
                                } else {
                                    results.append("⚠️ Login endpoint reachable but returned: ${body?.message}\n")
                                }
                            } else {
                                results.append("❌ Login endpoint failed: ${NetworkErrorHandler.getErrorMessage(loginResponse)}\n")
                            }
                        } catch (e: Exception) {
                            results.append("❌ Login endpoint error: ${NetworkErrorHandler.handleNetworkException(e)}\n")
                        }
                        results.append("\n")
                    }
                    
                    if (connectivityResults.any { it.serviceName.contains("Customer") && it.isReachable }) {
                        results.append("🏪 Testing Customer Service API endpoints...\n")
                        try {
                            val shopsResponse = customerApiService.getShops()
                            if (shopsResponse.isSuccessful) {
                                val body = shopsResponse.body()
                                if (body?.status == true && body.data != null) {
                                    results.append("✅ Shops endpoint working: ${body.data.size} shops found\n")
                                    body.data.take(2).forEach { shop ->
                                        results.append("   📍 ${shop.name}\n")
                                    }
                                } else {
                                    results.append("⚠️ Shops endpoint reachable but returned: ${body?.message}\n")
                                }
                            } else {
                                results.append("❌ Shops endpoint failed: ${NetworkErrorHandler.getErrorMessage(shopsResponse)}\n")
                            }
                        } catch (e: Exception) {
                            results.append("❌ Shops endpoint error: ${NetworkErrorHandler.handleNetworkException(e)}\n")
                        }
                        results.append("\n")
                    }
                    
                    if (connectivityResults.any { it.serviceName.contains("Catalog") && it.isReachable }) {
                        results.append("📦 Testing Catalog Service API endpoints...\n")
                        try {
                            val catalogResponse = catalogApiService.getCatalog()
                            if (catalogResponse.isSuccessful) {
                                val body = catalogResponse.body()
                                if (body?.status == true && body.data != null) {
                                    results.append("✅ Catalog endpoint working: ${body.data.size} items found\n")
                                    body.data.take(2).forEach { item ->
                                        results.append("   🛒 ${item.name}\n")
                                    }
                                } else {
                                    results.append("⚠️ Catalog endpoint reachable but returned: ${body?.message}\n")
                                }
                            } else {
                                results.append("❌ Catalog endpoint failed: ${NetworkErrorHandler.getErrorMessage(catalogResponse)}\n")
                            }
                        } catch (e: Exception) {
                            results.append("❌ Catalog endpoint error: ${NetworkErrorHandler.handleNetworkException(e)}\n")
                        }
                        results.append("\n")
                    }
                }
                
                results.append("🎉 Backend connectivity test completed!\n")
                results.append("\n📋 Full Report:\n")
                results.append(connectivityTest.generateReport(connectivityResults))
                
            } catch (e: Exception) {
                results.append("💥 Test Error: ${NetworkErrorHandler.handleNetworkException(e)}\n")
                Log.e("TestBackend", "Error testing backend", e)
            }
            
            runOnUiThread {
                textView.text = results.toString()
            }
        }
    }
}