package com.hyperlocal.marketplace.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hyperlocal.marketplace.presentation.screens.home.HomeScreen
import com.hyperlocal.marketplace.presentation.screens.categories.CategoriesScreen
import com.hyperlocal.marketplace.presentation.screens.shops.ShopsNearMeScreen
import com.hyperlocal.marketplace.presentation.screens.products.ProductsScreen
import com.hyperlocal.marketplace.presentation.screens.profile.ProfileScreen
import com.hyperlocal.marketplace.presentation.screens.help.HelpScreen

@Composable
fun HyperlocalApp() {
    val navController = rememberNavController()
    
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
        }
    }
}