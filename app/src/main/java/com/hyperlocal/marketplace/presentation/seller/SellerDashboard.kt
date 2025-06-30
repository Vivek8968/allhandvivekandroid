package com.hyperlocal.marketplace.presentation.seller

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
import com.hyperlocal.marketplace.presentation.common.ErrorScreen
import com.hyperlocal.marketplace.presentation.common.LoadingScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellerDashboard(
    viewModel: SellerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    LaunchedEffect(Unit) {
        viewModel.loadSellerData()
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Seller Dashboard") },
                actions = {
                    IconButton(onClick = { viewModel.refreshData() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                }
            )
        },
        floatingActionButton = {
            if (uiState.shop != null) {
                FloatingActionButton(
                    onClick = { viewModel.showAddProductDialog() }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Product")
                }
            }
        }
    ) { paddingValues ->
        when {
            uiState.isLoading -> {
                LoadingScreen("Loading seller data...")
            }
            uiState.errorMessage != null -> {
                val errorMessage = uiState.errorMessage ?: "Unknown error"
                ErrorScreen(
                    message = errorMessage,
                    onRetry = { viewModel.loadSellerData() }
                )
            }
            uiState.shop == null -> {
                ShopRegistrationScreen(
                    onRegisterShop = { viewModel.showShopRegistrationDialog() },
                    modifier = Modifier.padding(paddingValues)
                )
            }
            else -> {
                SellerContent(
                    uiState = uiState,
                    onEditShop = { viewModel.showEditShopDialog() },
                    onEditProduct = { product -> viewModel.showEditProductDialog(product) },
                    onDeleteProduct = { product -> viewModel.deleteProduct(product.id) },
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}

@Composable
fun ShopRegistrationScreen(
    onRegisterShop: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Store,
            contentDescription = "Shop",
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "Register Your Shop",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Start selling by registering your shop with us. Add your shop details, location, and start managing your inventory.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = onRegisterShop,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Register Shop")
        }
    }
}

@Composable
fun SellerContent(
    uiState: SellerUiState,
    onEditShop: () -> Unit,
    onEditProduct: (com.hyperlocal.marketplace.data.models.ShopInventory) -> Unit,
    onDeleteProduct: (com.hyperlocal.marketplace.data.models.ShopInventory) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Shop Info Card
        item {
            ShopInfoCard(
                shop = uiState.shop!!,
                onEdit = onEditShop
            )
        }
        
        // Quick Stats
        item {
            QuickStatsCard(
                totalProducts = uiState.products.size,
                pendingOrders = 0, // TODO: Implement orders
                totalViews = 0 // TODO: Implement analytics
            )
        }
        
        // Products Section
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "My Products (${uiState.products.size})",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        
        if (uiState.products.isEmpty()) {
            item {
                EmptyProductsCard()
            }
        } else {
            items(uiState.products) { product ->
                ProductCard(
                    product = product,
                    onEdit = { onEditProduct(product) },
                    onDelete = { onDeleteProduct(product) }
                )
            }
        }
    }
}

@Composable
fun ShopInfoCard(
    shop: com.hyperlocal.marketplace.data.models.Shop,
    onEdit: () -> Unit
) {
    Card(
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
                        style = MaterialTheme.typography.titleLarge,
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
                    Text(
                        text = "WhatsApp: ${shop.whatsappNumber}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit Shop")
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AssistChip(
                    onClick = { /* TODO: Generate QR */ },
                    label = { Text("QR Code") },
                    leadingIcon = {
                        Icon(Icons.Default.QrCode, contentDescription = "QR Code")
                    }
                )
                AssistChip(
                    onClick = { /* TODO: Share shop */ },
                    label = { Text("Share") },
                    leadingIcon = {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }
                )
                if (shop.isApproved) {
                    AssistChip(
                        onClick = { },
                        label = { Text("Approved") },
                        leadingIcon = {
                            Icon(Icons.Default.CheckCircle, contentDescription = "Approved")
                        }
                    )
                } else {
                    AssistChip(
                        onClick = { },
                        label = { Text("Pending") },
                        leadingIcon = {
                            Icon(Icons.Default.Schedule, contentDescription = "Pending")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun QuickStatsCard(
    totalProducts: Int,
    pendingOrders: Int,
    totalViews: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Quick Stats",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    icon = Icons.Default.Inventory,
                    label = "Products",
                    value = totalProducts.toString()
                )
                StatItem(
                    icon = Icons.Default.ShoppingCart,
                    label = "Orders",
                    value = pendingOrders.toString()
                )
                StatItem(
                    icon = Icons.Default.Visibility,
                    label = "Views",
                    value = totalViews.toString()
                )
            }
        }
    }
}

@Composable
fun StatItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun ProductCard(
    product: com.hyperlocal.marketplace.data.models.ShopInventory,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
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
                        text = product.catalogItem?.name ?: "Unknown Product",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    if (product.catalogItem?.description != null) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = product.catalogItem.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "₹${product.price}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Stock: ${product.stock}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                
                Row {
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Product")
                    }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete Product")
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyProductsCard() {
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
                imageVector = Icons.Default.Inventory,
                contentDescription = "No products",
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No products added yet",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Start adding products to your inventory",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}