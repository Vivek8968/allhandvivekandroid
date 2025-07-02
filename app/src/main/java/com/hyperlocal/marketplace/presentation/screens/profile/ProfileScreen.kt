package com.hyperlocal.marketplace.presentation.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.hyperlocal.marketplace.data.models.UserRole
import com.hyperlocal.marketplace.presentation.screens.auth.AuthViewModel
import com.hyperlocal.marketplace.presentation.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by authViewModel.uiState.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "Profile",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )
        
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                // User Info Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = CardBackground
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Profile",
                            modifier = Modifier.size(80.dp),
                            tint = Gray400
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        if (uiState.isLoggedIn) {
                            Text(
                                text = uiState.userName ?: "User",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Role: ${uiState.userRole ?: "Customer"}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Gray600
                            )
                        } else {
                            Text(
                                text = "Guest User",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Please login to access your profile",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Gray600
                            )
                        }
                    }
                }
            }
            
            if (!uiState.isLoggedIn) {
                item {
                    ProfileMenuItem(
                        icon = Icons.Default.Login,
                        title = "Login / Register",
                        onClick = { navController.navigate("login") }
                    )
                }
            } else {
                // Show role-specific options
                when (uiState.userRole) {
                    "CUSTOMER" -> {
                        item {
                            ProfileMenuItem(
                                icon = Icons.Default.ShoppingCart,
                                title = "My Orders",
                                onClick = { /* Handle orders */ }
                            )
                        }
                        
                        item {
                            ProfileMenuItem(
                                icon = Icons.Default.Favorite,
                                title = "Favorites",
                                onClick = { /* Handle favorites */ }
                            )
                        }
                    }
                    "SELLER" -> {
                        item {
                            ProfileMenuItem(
                                icon = Icons.Default.Store,
                                title = "My Shop",
                                onClick = { navController.navigate("seller_dashboard") }
                            )
                        }
                        
                        item {
                            ProfileMenuItem(
                                icon = Icons.Default.ShoppingCart,
                                title = "Orders",
                                onClick = { navController.navigate("seller_orders") }
                            )
                        }
                    }
                    "ADMIN" -> {
                        item {
                            ProfileMenuItem(
                                icon = Icons.Default.Dashboard,
                                title = "Admin Dashboard",
                                onClick = { navController.navigate("admin_dashboard") }
                            )
                        }
                    }
                }
                
                // Common options for all logged-in users
                item {
                    ProfileMenuItem(
                        icon = Icons.Default.Settings,
                        title = "Settings",
                        onClick = { /* Handle settings */ }
                    )
                }
                
                item {
                    ProfileMenuItem(
                        icon = Icons.Default.ExitToApp,
                        title = "Logout",
                        onClick = { 
                            authViewModel.signOut()
                            navController.navigate("login") {
                                popUpTo("home") { inclusive = true }
                            }
                        }
                    )
                }
            }
            
            // Common options for all users
            item {
                ProfileMenuItem(
                    icon = Icons.Default.Info,
                    title = "About",
                    onClick = { /* Handle about */ }
                )
            }
            
            item {
                ProfileMenuItem(
                    icon = Icons.Default.Help,
                    title = "Help & Support",
                    onClick = { navController.navigate("help") }
                )
            }
        }
    }
}

@Composable
fun ProfileMenuItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardBackground
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = IconTint,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Navigate",
                tint = IconTint,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}