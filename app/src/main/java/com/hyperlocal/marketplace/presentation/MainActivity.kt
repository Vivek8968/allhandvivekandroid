package com.hyperlocal.marketplace.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hyperlocal.marketplace.data.models.UserRole
import com.hyperlocal.marketplace.presentation.auth.AuthScreen
import com.hyperlocal.marketplace.presentation.admin.AdminDashboard
import com.hyperlocal.marketplace.presentation.customer.CustomerDashboard
import com.hyperlocal.marketplace.presentation.seller.SellerDashboard
import com.hyperlocal.marketplace.presentation.common.LoadingScreen
import com.hyperlocal.marketplace.presentation.theme.HyperlocalMarketplaceTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    private val mainViewModel: MainViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        
        splashScreen.setKeepOnScreenCondition {
            mainViewModel.isLoading.value
        }
        
        setContent {
            HyperlocalMarketplaceTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainContent(mainViewModel)
                }
            }
        }
    }
}

@Composable
fun MainContent(viewModel: MainViewModel) {
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val isLoggedIn by viewModel.isLoggedIn.collectAsStateWithLifecycle()
    val userRole by viewModel.userRole.collectAsStateWithLifecycle()
    
    when {
        isLoading -> {
            LoadingScreen()
        }
        !isLoggedIn -> {
            AuthScreen(
                onLoginSuccess = { role ->
                    viewModel.onLoginSuccess(role)
                }
            )
        }
        else -> {
            when (userRole) {
                UserRole.CUSTOMER -> CustomerDashboard()
                UserRole.SELLER -> SellerDashboard()
                UserRole.ADMIN -> AdminDashboard()
                null -> AuthScreen(
                    onLoginSuccess = { role ->
                        viewModel.onLoginSuccess(role)
                    }
                )
            }
        }
    }
}