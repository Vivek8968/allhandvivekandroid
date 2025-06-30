package com.hyperlocal.marketplace.data.api

import com.hyperlocal.marketplace.config.Config
import com.hyperlocal.marketplace.data.models.*
import retrofit2.Response
import retrofit2.http.*

interface CatalogApiService {
    
    @GET(Config.Endpoints.CATALOG)
    suspend fun getCatalog(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 20,
        @Query("category") category: String? = null
    ): Response<CatalogSearchResponse>
    
    @GET(Config.Endpoints.CATALOG_SEARCH)
    suspend fun searchCatalog(
        @Header("Authorization") token: String,
        @Query("query") query: String,
        @Query("category") category: String? = null,
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 20
    ): Response<CatalogSearchResponse>
    
    @GET(Config.Endpoints.CATALOG_ITEM)
    suspend fun getCatalogItem(
        @Header("Authorization") token: String,
        @Path("id") itemId: Int
    ): Response<CatalogItem>
}