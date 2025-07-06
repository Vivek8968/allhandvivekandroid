package com.hyperlocal.marketplace.utils

import android.content.Context
import android.util.Log
import com.hyperlocal.marketplace.config.Config
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

/**
 * Utility class for testing backend connectivity
 */
class BackendConnectivityTest(private val context: Context) {
    
    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()
    
    data class ServiceStatus(
        val serviceName: String,
        val url: String,
        val isReachable: Boolean,
        val responseTime: Long,
        val errorMessage: String? = null
    )
    
    /**
     * Test connectivity to all backend services
     */
    suspend fun testAllServices(): List<ServiceStatus> = withContext(Dispatchers.IO) {
        val healthCheckUrls = Config.Debug.getHealthCheckUrls()
        val serviceNames = listOf("User Service", "Seller Service", "Customer Service", "Catalog Service", "Admin Service")
        
        healthCheckUrls.mapIndexed { index, url ->
            async {
                testService(serviceNames[index], url)
            }
        }.awaitAll()
    }
    
    /**
     * Test connectivity to a specific service
     */
    private suspend fun testService(serviceName: String, url: String): ServiceStatus = withContext(Dispatchers.IO) {
        val startTime = System.currentTimeMillis()
        
        try {
            val request = Request.Builder()
                .url(url)
                .get()
                .build()
            
            val response = client.newCall(request).execute()
            val responseTime = System.currentTimeMillis() - startTime
            
            if (response.isSuccessful) {
                Log.d("BackendTest", "✅ $serviceName is reachable (${responseTime}ms)")
                ServiceStatus(serviceName, url, true, responseTime)
            } else {
                Log.w("BackendTest", "⚠️ $serviceName returned ${response.code}")
                ServiceStatus(serviceName, url, false, responseTime, "HTTP ${response.code}")
            }
        } catch (e: Exception) {
            val responseTime = System.currentTimeMillis() - startTime
            Log.e("BackendTest", "❌ $serviceName failed: ${e.message}")
            ServiceStatus(serviceName, url, false, responseTime, e.message)
        }
    }
    
    /**
     * Test specific API endpoint
     */
    suspend fun testApiEndpoint(
        serviceName: String,
        baseUrl: String,
        endpoint: String,
        method: String = "GET",
        authToken: String? = null
    ): ServiceStatus = withContext(Dispatchers.IO) {
        val fullUrl = "$baseUrl$endpoint"
        val startTime = System.currentTimeMillis()
        
        try {
            val requestBuilder = Request.Builder().url(fullUrl)
            
            when (method.uppercase()) {
                "GET" -> requestBuilder.get()
                "POST" -> requestBuilder.post(okhttp3.RequestBody.create(null, ""))
                "PUT" -> requestBuilder.put(okhttp3.RequestBody.create(null, ""))
                "DELETE" -> requestBuilder.delete()
            }
            
            authToken?.let {
                requestBuilder.addHeader("Authorization", "Bearer $it")
            }
            
            val response = client.newCall(requestBuilder.build()).execute()
            val responseTime = System.currentTimeMillis() - startTime
            
            Log.d("BackendTest", "$method $fullUrl -> ${response.code} (${responseTime}ms)")
            
            ServiceStatus(
                serviceName = "$serviceName - $endpoint",
                url = fullUrl,
                isReachable = response.isSuccessful,
                responseTime = responseTime,
                errorMessage = if (!response.isSuccessful) "HTTP ${response.code}" else null
            )
        } catch (e: Exception) {
            val responseTime = System.currentTimeMillis() - startTime
            Log.e("BackendTest", "$method $fullUrl failed: ${e.message}")
            ServiceStatus(
                serviceName = "$serviceName - $endpoint",
                url = fullUrl,
                isReachable = false,
                responseTime = responseTime,
                errorMessage = e.message
            )
        }
    }
    
    /**
     * Generate connectivity report
     */
    fun generateReport(results: List<ServiceStatus>): String {
        val report = StringBuilder()
        report.appendLine("🔍 Backend Connectivity Report")
        report.appendLine("=" * 40)
        report.appendLine()
        
        val reachableCount = results.count { it.isReachable }
        val totalCount = results.size
        
        report.appendLine("📊 Summary: $reachableCount/$totalCount services reachable")
        report.appendLine()
        
        results.forEach { status ->
            val icon = if (status.isReachable) "✅" else "❌"
            val time = "${status.responseTime}ms"
            val error = status.errorMessage?.let { " - $it" } ?: ""
            
            report.appendLine("$icon ${status.serviceName} ($time)$error")
            report.appendLine("   URL: ${status.url}")
            report.appendLine()
        }
        
        if (reachableCount == 0) {
            report.appendLine("🚨 No services are reachable!")
            report.appendLine("💡 Suggestions:")
            report.appendLine("   • Check if backend services are running")
            report.appendLine("   • Verify IP address (192.168.1.3)")
            report.appendLine("   • Check network connectivity")
            report.appendLine("   • Ensure ports 8001-8005 are accessible")
        } else if (reachableCount < totalCount) {
            report.appendLine("⚠️ Some services are not reachable")
            report.appendLine("💡 Check the failed services and their error messages")
        } else {
            report.appendLine("🎉 All services are reachable!")
        }
        
        return report.toString()
    }
    
    private operator fun String.times(n: Int): String = this.repeat(n)
}