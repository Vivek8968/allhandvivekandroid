package com.hyperlocal.marketplace.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.hyperlocal.marketplace.data.models.Shop
import com.hyperlocal.marketplace.presentation.screens.auth.AuthViewModel
import com.hyperlocal.marketplace.presentation.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All Categories") }
    var isDropdownExpanded by remember { mutableStateOf(false) }
    
    // Sample data - in real app this would come from ViewModels
    val categories = listOf("All Categories", "Grocery", "Electronics", "Clothing", "Food", "Pharmacy", "Books")
    val sampleShops = getSampleShops()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top Bar with Category Filter, Search, and Login
        TopAppBar(
            title = { },
            navigationIcon = {
                // Category Filter Dropdown
                Box {
                    OutlinedButton(
                        onClick = { isDropdownExpanded = true },
                        modifier = Modifier.padding(start = 8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Gray300)
                    ) {
                        Text(
                            text = selectedCategory,
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Category Filter",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    
                    DropdownMenu(
                        expanded = isDropdownExpanded,
                        onDismissRequest = { isDropdownExpanded = false }
                    ) {
                        categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category) },
                                onClick = {
                                    selectedCategory = category
                                    isDropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            },
            actions = {
                // Login Button or Profile Button
                val authViewModel: AuthViewModel = hiltViewModel()
                val authState by authViewModel.uiState.collectAsState()
                
                if (authState.isLoggedIn) {
                    // Show profile button if logged in
                    TextButton(
                        onClick = { navController.navigate("profile") },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Profile",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }
                } else {
                    // Show login button if not logged in
                    TextButton(
                        onClick = { navController.navigate("login") },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Login,
                            contentDescription = "Login",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Login",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )
        
        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            placeholder = {
                Text(
                    text = "Search shops and products",
                    color = PlaceholderColor
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = IconTint
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Primary,
                unfocusedBorderColor = Gray300,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            )
        )
        
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Categories Section
            item {
                CategoriesSection(categories = categories.drop(1)) // Remove "All Categories"
            }
            
            // Shops Near Me Section
            item {
                ShopsNearMeSection(shops = sampleShops)
            }
        }
    }
}

@Composable
fun CategoriesSection(categories: List<String>) {
    Column {
        Text(
            text = "Categories",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(categories) { category ->
                CategoryCard(category = category)
            }
        }
    }
}

@Composable
fun CategoryCard(category: String) {
    Card(
        modifier = Modifier
            .width(120.dp)
            .height(80.dp)
            .clickable { navController.navigate("categories") },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardBackground
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = category,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun ShopsNearMeSection(shops: List<Shop>) {
    Column {
        Text(
            text = "Shops Near Me",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        shops.forEach { shop ->
            ShopCard(shop = shop)
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun ShopCard(shop: Shop, onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
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
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = shop.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Text(
                        text = shop.category,
                        style = MaterialTheme.typography.bodySmall,
                        color = Gray600,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                    
                    Text(
                        text = shop.address,
                        style = MaterialTheme.typography.bodySmall,
                        color = Gray600,
                        modifier = Modifier.padding(top = 4.dp),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    
                    shop.distanceFormatted?.let { distance ->
                        Text(
                            text = distance,
                            style = MaterialTheme.typography.bodySmall,
                            color = Primary,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
                
                // Rating
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = Warning,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = String.format("%.1f", shop.rating),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

// Sample data function
fun getSampleShops(): List<Shop> {
    return listOf(
        Shop(
            id = "1",
            name = "Fresh Mart Grocery",
            description = "Your neighborhood grocery store",
            address = "123 Main Street, Downtown",
            latitude = 40.7128,
            longitude = -74.0060,
            distance = 0.5,
            distanceFormatted = "0.5 km away",
            category = "Grocery",
            rating = 4.5,
            imageUrl = null,
            ownerId = "owner1"
        ),
        Shop(
            id = "2",
            name = "Tech Hub Electronics",
            description = "Latest gadgets and electronics",
            address = "456 Tech Avenue, Silicon Valley",
            latitude = 40.7589,
            longitude = -73.9851,
            distance = 1.2,
            distanceFormatted = "1.2 km away",
            category = "Electronics",
            rating = 4.2,
            imageUrl = null,
            ownerId = "owner2"
        ),
        Shop(
            id = "3",
            name = "Bella's Fashion Boutique",
            description = "Trendy clothing and accessories",
            address = "789 Fashion Street, Uptown",
            latitude = 40.7831,
            longitude = -73.9712,
            distance = 2.1,
            distanceFormatted = "2.1 km away",
            category = "Clothing",
            rating = 4.8,
            imageUrl = null,
            ownerId = "owner3"
        )
    )
}