package com.hyperlocal.marketplace.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.hyperlocal.marketplace.config.Config
import com.hyperlocal.marketplace.presentation.navigation.HyperlocalApp
import com.hyperlocal.marketplace.presentation.screens.auth.GoogleSignInHelper
import com.hyperlocal.marketplace.presentation.theme.HyperlocalMarketplaceTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    @Inject
    lateinit var googleSignInHelper: GoogleSignInHelper
    
    private lateinit var googleSignInLauncher: ActivityResultLauncher<Intent>
    
    companion object {
        private const val TAG = "MainActivity"
        var googleSignInTask: Task<GoogleSignInAccount>? = null
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize Google Sign-In
        googleSignInHelper.initialize(this, Config.Firebase.WEB_CLIENT_ID)
        
        // Set up the ActivityResultLauncher for Google Sign-In
        googleSignInLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                // Store the task result for the ViewModel to process
                googleSignInTask = task
                Log.d(TAG, "Google Sign-In successful")
            } catch (e: ApiException) {
                Log.e(TAG, "Google Sign-In failed: ${e.statusCode}", e)
                googleSignInTask = null
            }
        }
        
        setContent {
            HyperlocalMarketplaceTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HyperlocalApp(
                        onGoogleSignInRequested = { launchGoogleSignIn() }
                    )
                }
            }
        }
    }
    
    private fun launchGoogleSignIn() {
        val signInIntent = googleSignInHelper.getSignInIntent()
        googleSignInLauncher.launch(signInIntent)
    }
}