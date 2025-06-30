@file:OptIn(ExperimentalMaterial3Api::class)

package com.hyperlocal.marketplace.presentation.customer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hyperlocal.marketplace.data.models.Shop
import com.hyperlocal.marketplace.presentation.common.ErrorScreen
import com.hyperlocal.marketplace.presentation.common.LoadingScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerDashboard(
    viewModel: CustomerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    LaunchedEffect(Unit) {
        viewModel.loadNearbyShops()
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nearby Shops") },
                actions = {
                    IconButton(onClick = { viewModel.refreshLocation() }) {
                        Icon(Icons.Default.LocationOn, contentDescription = "Refresh Location")
                    }
                    IconButton(onClick = { /* TODO: Open search */ }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.refreshShops() }
            ) {
                Icon(Icons.Default.Refresh, contentDescription = "Refresh")
            }
        }
    ) { paddingValues ->
        when {
            uiState.isLoading -> {
                LoadingScreen("Finding nearby shops...")
            }
            uiState.errorMessage != null -> {
                val errorMessage = uiState.errorMessage ?: "Unknown error"
                ErrorScreen(
                    message = errorMessage,
                    onRetry = { viewModel.loadNearbyShops() }
                )
            }
            else -> {
                CustomerContent(
                    uiState = uiState,
                    onShopClick = { shop -> viewModel.openShop(shop) },
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}

@Composable
fun CustomerContent(
    uiState: CustomerUiState,
    onShopClick: (Shop) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Location Info
        item {
            LocationCard(
                latitude = uiState.currentLocation?.first,
                longitude = uiState.currentLocation?.second,
                searchRadius = uiState.searchRadius
            )
        }
        
        // Shops Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Nearby Shops (${uiState.shops.size})",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                TextButton(onClick = { /* TODO: View all */ }) {
                    Text("View All")
                }
            }
        }
        
        // Shops List
        if (uiState.shops.isEmpty()) {
            item {
                EmptyShopsCard()
            }
        } else {
            items(uiState.shops) { shop ->
                ShopCard(
                    shop = shop,
                    onClick = { onShopClick(shop) }
                )
            }
        }
    }
}

@Composable
fun LocationCard(
    latitude: Double?,
    longitude: Double?,
    searchRadius: Double
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location",
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Your Location",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            if (latitude != null && longitude != null) {
                Text(
                    text = "Lat: ${String.format("%.4f", latitude)}, Lng: ${String.format("%.4f", longitude)}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Search radius: ${searchRadius.toInt()} km",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Text(
                    text = "Location not available",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun ShopCard(
    shop: Shop,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = shop.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    if (shop.description != null) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = shop.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = shop.address,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                if (shop.distance != null) {
                    Text(
                        text = "${String.format("%.1f", shop.distance)} km",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AssistChip(
                    onClick = { /* TODO: Call WhatsApp */ },
                    label = { Text("WhatsApp") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = "WhatsApp"
                        )
                    }
                )
                AssistChip(
                    onClick = { /* TODO: View products */ },
                    label = { Text("Products") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Products"
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun EmptyShopsCard() {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Store,
                contentDescription = "No shops",
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No shops found nearby",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Try increasing your search radius or check back later",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}