package com.hyperlocal.marketplace.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.hyperlocal.marketplace.data.models.UserRole
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Singleton
class UserPreferencesRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.dataStore

    companion object {
        private val AUTH_TOKEN = stringPreferencesKey("auth_token")
        private val USER_ID = stringPreferencesKey("user_id")
        private val USER_ROLE = stringPreferencesKey("user_role")
        private val USER_NAME = stringPreferencesKey("user_name")
        private val USER_EMAIL = stringPreferencesKey("user_email")
        private val USER_PHONE = stringPreferencesKey("user_phone")
        private val FIREBASE_UID = stringPreferencesKey("firebase_uid")
    }

    suspend fun saveAuthToken(token: String) {
        dataStore.edit { preferences ->
            preferences[AUTH_TOKEN] = token
        }
    }

    suspend fun saveUserRole(role: UserRole) {
        dataStore.edit { preferences ->
            preferences[USER_ROLE] = role.name
        }
    }

    suspend fun saveUserInfo(
        userId: String,
        name: String,
        email: String?,
        phone: String?,
        firebaseUid: String
    ) {
        dataStore.edit { preferences ->
            preferences[USER_ID] = userId
            preferences[USER_NAME] = name
            if (email != null) preferences[USER_EMAIL] = email
            if (phone != null) preferences[USER_PHONE] = phone
            preferences[FIREBASE_UID] = firebaseUid
        }
    }

    suspend fun clearUserData() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    val authToken: Flow<String?> = dataStore.data.map { preferences ->
        preferences[AUTH_TOKEN]
    }

    val userRole: Flow<UserRole?> = dataStore.data.map { preferences ->
        preferences[USER_ROLE]?.let { roleName ->
            try {
                UserRole.valueOf(roleName)
            } catch (e: IllegalArgumentException) {
                null
            }
        }
    }

    val userId: Flow<String?> = dataStore.data.map { preferences ->
        preferences[USER_ID]
    }

    val userName: Flow<String?> = dataStore.data.map { preferences ->
        preferences[USER_NAME]
    }

    val userEmail: Flow<String?> = dataStore.data.map { preferences ->
        preferences[USER_EMAIL]
    }

    val userPhone: Flow<String?> = dataStore.data.map { preferences ->
        preferences[USER_PHONE]
    }

    val firebaseUid: Flow<String?> = dataStore.data.map { preferences ->
        preferences[FIREBASE_UID]
    }

    val isLoggedIn: Flow<Boolean> = dataStore.data.map { preferences ->
        !preferences[AUTH_TOKEN].isNullOrEmpty()
    }
}