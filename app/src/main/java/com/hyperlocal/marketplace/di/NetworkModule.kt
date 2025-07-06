package com.hyperlocal.marketplace.di

import com.hyperlocal.marketplace.config.Config
import com.hyperlocal.marketplace.data.api.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (Config.Debug.ENABLE_NETWORK_LOGGING) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
        
        // Custom interceptor for debugging and error handling
        val debugInterceptor = okhttp3.Interceptor { chain ->
            val request = chain.request()
            
            if (Config.Debug.ENABLE_API_LOGGING) {
                println("🌐 API Request: ${request.method} ${request.url}")
                request.body?.let { body ->
                    println("📤 Request Body: $body")
                }
            }
            
            val response = chain.proceed(request)
            
            if (Config.Debug.ENABLE_API_LOGGING) {
                println("📥 API Response: ${response.code} ${response.message}")
                if (!response.isSuccessful) {
                    println("❌ Error Response: ${response.body?.string()}")
                }
            }
            
            response
        }
        
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(debugInterceptor)
            .connectTimeout(Config.Network.CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(Config.Network.READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(Config.Network.WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }
    
    @Provides
    @Singleton
    @Named("user_retrofit")
    fun provideUserRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Config.USER_SERVICE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    @Provides
    @Singleton
    @Named("seller_retrofit")
    fun provideSellerRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Config.SELLER_SERVICE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    @Provides
    @Singleton
    @Named("customer_retrofit")
    fun provideCustomerRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Config.CUSTOMER_SERVICE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    @Provides
    @Singleton
    @Named("catalog_retrofit")
    fun provideCatalogRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Config.CATALOG_SERVICE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    @Provides
    @Singleton
    @Named("admin_retrofit")
    fun provideAdminRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Config.ADMIN_SERVICE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    @Provides
    @Singleton
    fun provideUserApiService(@Named("user_retrofit") retrofit: Retrofit): UserApiService {
        return retrofit.create(UserApiService::class.java)
    }
    
    @Provides
    @Singleton
    fun provideSellerApiService(@Named("seller_retrofit") retrofit: Retrofit): SellerApiService {
        return retrofit.create(SellerApiService::class.java)
    }
    
    @Provides
    @Singleton
    fun provideCustomerApiService(@Named("customer_retrofit") retrofit: Retrofit): CustomerApiService {
        return retrofit.create(CustomerApiService::class.java)
    }
    
    @Provides
    @Singleton
    fun provideCatalogApiService(@Named("catalog_retrofit") retrofit: Retrofit): CatalogApiService {
        return retrofit.create(CatalogApiService::class.java)
    }
    
    @Provides
    @Singleton
    fun provideAdminApiService(@Named("admin_retrofit") retrofit: Retrofit): AdminApiService {
        return retrofit.create(AdminApiService::class.java)
    }
}