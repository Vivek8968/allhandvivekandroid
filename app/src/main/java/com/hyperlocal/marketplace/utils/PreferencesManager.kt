package com.hyperlocal.marketplace.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.hyperlocal.marketplace.data.models.UserRole
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    
    private object PreferencesKeys {
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val USER_ID = intPreferencesKey("user_id")
        val USER_ROLE = stringPreferencesKey("user_role")
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_EMAIL = stringPreferencesKey("user_email")
        val USER_PHONE = stringPreferencesKey("user_phone")
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val LAST_LATITUDE = doublePreferencesKey("last_latitude")
        val LAST_LONGITUDE = doublePreferencesKey("last_longitude")
        val SEARCH_RADIUS = doublePreferencesKey("search_radius")
        val IS_FIRST_LAUNCH = booleanPreferencesKey("is_first_launch")
    }
    
    suspend fun saveAuthToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.ACCESS_TOKEN] = token
        }
    }
    
    suspend fun saveUserData(
        userId: Int,
        role: UserRole,
        name: String,
        email: String?,
        phone: String?
    ) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_ID] = userId
            preferences[PreferencesKeys.USER_ROLE] = role.name
            preferences[PreferencesKeys.USER_NAME] = name
            email?.let { preferences[PreferencesKeys.USER_EMAIL] = it }
            phone?.let { preferences[PreferencesKeys.USER_PHONE] = it }
            preferences[PreferencesKeys.IS_LOGGED_IN] = true
        }
    }
    
    suspend fun saveLocation(latitude: Double, longitude: Double) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.LAST_LATITUDE] = latitude
            preferences[PreferencesKeys.LAST_LONGITUDE] = longitude
        }
    }
    
    suspend fun saveSearchRadius(radius: Double) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.SEARCH_RADIUS] = radius
        }
    }
    
    suspend fun setFirstLaunchCompleted() {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_FIRST_LAUNCH] = false
        }
    }
    
    suspend fun clearUserData() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
    
    val authToken: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.ACCESS_TOKEN]
    }
    
    val userId: Flow<Int?> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.USER_ID]
    }
    
    val userRole: Flow<UserRole?> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.USER_ROLE]?.let { 
            try {
                UserRole.valueOf(it)
            } catch (e: IllegalArgumentException) {
                null
            }
        }
    }
    
    val userName: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.USER_NAME]
    }
    
    val userEmail: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.USER_EMAIL]
    }
    
    val userPhone: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.USER_PHONE]
    }
    
    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.IS_LOGGED_IN] ?: false
    }
    
    val lastLocation: Flow<Pair<Double, Double>?> = context.dataStore.data.map { preferences ->
        val lat = preferences[PreferencesKeys.LAST_LATITUDE]
        val lon = preferences[PreferencesKeys.LAST_LONGITUDE]
        if (lat != null && lon != null) Pair(lat, lon) else null
    }
    
    val searchRadius: Flow<Double> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.SEARCH_RADIUS] ?: 10.0
    }
    
    val isFirstLaunch: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.IS_FIRST_LAUNCH] ?: true
    }
}