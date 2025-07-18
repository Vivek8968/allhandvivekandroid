package com.hyperlocal.marketplace.presentation.screens.auth

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Helper class for Google Sign-In functionality
 */
@Singleton
class GoogleSignInHelper @Inject constructor() {
    
    private lateinit var googleSignInClient: GoogleSignInClient
    
    /**
     * Initialize Google Sign-In client
     * 
     * @param context Application context
     * @param webClientId Google Web Client ID from Firebase console
     * @return true if initialized successfully, false if Web Client ID is invalid
     */
    fun initialize(context: Context, webClientId: String): Boolean {
        // Check if we have a valid Web Client ID
        if (webClientId == "YOUR_ACTUAL_WEB_CLIENT_ID_HERE" || 
            webClientId.contains("placeholder") || 
            webClientId.isBlank()) {
            return false
        }
        
        try {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(webClientId)
                .requestEmail()
                .requestProfile()
                .build()
            
            googleSignInClient = GoogleSignIn.getClient(context, gso)
            return true
        } catch (e: Exception) {
            return false
        }
    }
    
    /**
     * Check if Google Sign-In is properly configured
     */
    fun isConfigured(): Boolean {
        return ::googleSignInClient.isInitialized
    }
    
    /**
     * Get sign-in intent to launch with ActivityResultLauncher
     */
    fun getSignInIntent(): Intent {
        return googleSignInClient.signInIntent
    }
    
    /**
     * Handle the result from Google Sign-In
     * 
     * @param task Task from onActivityResult
     * @return GoogleSignInAccount if successful, null otherwise
     */
    suspend fun handleSignInResult(task: Task<GoogleSignInAccount>): GoogleSignInAccount? {
        return try {
            task.await()
        } catch (e: ApiException) {
            null
        }
    }
    
    /**
     * Get Firebase credential from Google Sign-In account
     * 
     * @param account Google Sign-In account
     * @return Firebase AuthResult
     */
    suspend fun firebaseAuthWithGoogle(account: GoogleSignInAccount): AuthResult? {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        return try {
            FirebaseAuth.getInstance().signInWithCredential(credential).await()
        } catch (e: Exception) {
            null
        }
    }
    
    /**
     * Sign out from Google
     */
    fun signOut() {
        googleSignInClient.signOut()
    }
}