package com.hyperlocal.marketplace.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hyperlocal.marketplace.data.models.UserRole
import com.hyperlocal.marketplace.presentation.screens.admin.AdminDashboardScreen
import com.hyperlocal.marketplace.presentation.screens.auth.AuthViewModel
import com.hyperlocal.marketplace.presentation.screens.auth.ModernLoginScreen
import com.hyperlocal.marketplace.presentation.screens.auth.RegisterScreen
import com.hyperlocal.marketplace.presentation.screens.auth.RoleSelectionScreen
import com.hyperlocal.marketplace.presentation.screens.categories.CategoriesScreen
import com.hyperlocal.marketplace.presentation.screens.help.HelpScreen
import com.hyperlocal.marketplace.presentation.screens.home.HomeScreen
import com.hyperlocal.marketplace.presentation.screens.products.ProductsScreen
import com.hyperlocal.marketplace.presentation.screens.profile.ProfileScreen
import com.hyperlocal.marketplace.presentation.screens.seller.SellerDashboardScreen
import com.hyperlocal.marketplace.presentation.screens.shops.ShopsNearMeScreen

@Composable
fun HyperlocalApp(
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val uiState by authViewModel.uiState.collectAsState()
    
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            // Auth screens
            composable("login") {
                ModernLoginScreen(navController = navController)
            }
            composable("register") {
                RegisterScreen(navController = navController)
            }
            composable("role_selection") {
                RoleSelectionScreen(navController = navController)
            }
            
            // Customer screens
            composable("home") {
                HomeScreen(navController = navController)
            }
            composable("categories") {
                CategoriesScreen(navController = navController)
            }
            composable("shops") {
                ShopsNearMeScreen(navController = navController)
            }
            composable("products") {
                ProductsScreen(navController = navController)
            }
            composable("profile") {
                ProfileScreen(navController = navController)
            }
            composable("help") {
                HelpScreen(navController = navController)
            }
            
            // Seller screens
            composable("seller_dashboard") {
                SellerDashboardScreen(navController = navController)
            }
            composable("seller_add_product") {
                // TODO: Implement AddProductScreen
                SellerDashboardScreen(navController = navController)
            }
            composable("seller_edit_product/{productId}") {
                // TODO: Implement EditProductScreen
                SellerDashboardScreen(navController = navController)
            }
            composable("seller_edit_shop") {
                // TODO: Implement EditShopScreen
                SellerDashboardScreen(navController = navController)
            }
            composable("seller_orders") {
                // TODO: Implement OrdersScreen
                SellerDashboardScreen(navController = navController)
            }
            
            // Admin screens
            composable("admin_dashboard") {
                AdminDashboardScreen(navController = navController)
            }
            composable("admin_user_details/{userId}") {
                // TODO: Implement UserDetailsScreen
                AdminDashboardScreen(navController = navController)
            }
        }
    }
}