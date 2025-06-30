package com.hyperlocal.marketplace

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.hyperlocal.marketplace.config.Config
import com.hyperlocal.marketplace.data.models.*
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.Response

// Simple API interface for testing
interface TestApiService {
    @GET("shops")
    suspend fun getShops(): Response<ApiResponse<List<Shop>>>
    
    @GET("catalog")
    suspend fun getCatalog(): Response<ApiResponse<List<CatalogItem>>>
    
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<ApiResponse<LoginResponseData>>
}

class TestBackendActivity : ComponentActivity() {
    
    private val apiService by lazy {
        Retrofit.Builder()
            .baseUrl(Config.BASE_URL + "/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TestApiService::class.java)
    }
    
    private lateinit var textView: TextView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        textView = TextView(this).apply {
            text = "Testing Backend Connection...\nAPI URL: ${Config.BASE_URL}"
            textSize = 16f
            setPadding(32, 32, 32, 32)
        }
        setContentView(textView)
        
        testBackendConnection()
    }
    
    private fun testBackendConnection() {
        val results = StringBuilder()
        results.append("🔄 Testing backend connection...\n\n")
        
        lifecycleScope.launch {
            try {
                // Test 1: Login
                results.append("📱 Testing login endpoint...\n")
                val loginResponse = apiService.login(LoginRequest("1234567890"))
                if (loginResponse.isSuccessful) {
                    val body = loginResponse.body()
                    if (body?.status == true) {
                        results.append("✅ Login successful: ${body.data?.user?.name}\n")
                        results.append("🔑 Token: ${body.data?.token?.take(20)}...\n\n")
                    } else {
                        results.append("❌ Login failed: ${body?.message}\n\n")
                    }
                } else {
                    results.append("❌ Login request failed: ${loginResponse.code()}\n\n")
                }
                
                // Test 2: Get Shops
                results.append("🏪 Testing shops endpoint...\n")
                val shopsResponse = apiService.getShops()
                if (shopsResponse.isSuccessful) {
                    val body = shopsResponse.body()
                    if (body?.status == true && body.data != null) {
                        results.append("✅ Shops loaded: ${body.data.size} shops found\n")
                        body.data.take(3).forEach { shop ->
                            results.append("   📍 ${shop.name} - ${shop.category}\n")
                        }
                        results.append("\n")
                    } else {
                        results.append("❌ Shops failed: ${body?.message}\n\n")
                    }
                } else {
                    results.append("❌ Shops request failed: ${shopsResponse.code()}\n\n")
                }
                
                // Test 3: Get Catalog
                results.append("📦 Testing catalog endpoint...\n")
                val catalogResponse = apiService.getCatalog()
                if (catalogResponse.isSuccessful) {
                    val body = catalogResponse.body()
                    if (body?.status == true && body.data != null) {
                        results.append("✅ Catalog loaded: ${body.data.size} items found\n")
                        body.data.take(3).forEach { item ->
                            results.append("   🛒 ${item.name} - $${item.suggestedPrice}\n")
                        }
                        results.append("\n")
                    } else {
                        results.append("❌ Catalog failed: ${body?.message}\n\n")
                    }
                } else {
                    results.append("❌ Catalog request failed: ${catalogResponse.code()}\n\n")
                }
                
                results.append("🎉 Backend integration test completed!")
                
            } catch (e: Exception) {
                results.append("💥 Error: ${e.message}\n")
                Log.e("TestBackend", "Error testing backend", e)
            }
            
            runOnUiThread {
                textView.text = results.toString()
            }
        }
    }
}