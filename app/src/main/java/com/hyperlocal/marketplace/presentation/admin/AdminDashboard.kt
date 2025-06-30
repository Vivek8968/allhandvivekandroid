package com.hyperlocal.marketplace.presentation.admin

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
fun AdminDashboard(
    viewModel: AdminViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    LaunchedEffect(Unit) {
        viewModel.loadAdminData()
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Admin Dashboard") },
                actions = {
                    IconButton(onClick = { viewModel.refreshData() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                }
            )
        }
    ) { paddingValues ->
        when {
            uiState.isLoading -> {
                LoadingScreen("Loading admin data...")
            }
            uiState.errorMessage != null -> {
                val errorMessage = uiState.errorMessage ?: "Unknown error"
                ErrorScreen(
                    message = errorMessage,
                    onRetry = { viewModel.loadAdminData() }
                )
            }
            else -> {
                AdminContent(
                    uiState = uiState,
                    onApproveShop = { shop -> viewModel.approveShop(shop.id) },
                    onRejectShop = { shop -> viewModel.rejectShop(shop.id) },
                    onDeleteUser = { user -> viewModel.deleteUser(user.id) },
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}

@Composable
fun AdminContent(
    uiState: AdminUiState,
    onApproveShop: (com.hyperlocal.marketplace.data.models.Shop) -> Unit,
    onRejectShop: (com.hyperlocal.marketplace.data.models.Shop) -> Unit,
    onDeleteUser: (com.hyperlocal.marketplace.data.models.User) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Stats Overview
        item {
            AdminStatsCard(stats = uiState.stats)
        }
        
        // Pending Shops Section
        item {
            Text(
                text = "Pending Shop Approvals (${uiState.pendingShops.size})",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
        
        if (uiState.pendingShops.isEmpty()) {
            item {
                EmptyStateCard(
                    icon = Icons.Default.Store,
                    title = "No pending shops",
                    description = "All shops have been reviewed"
                )
            }
        } else {
            items(uiState.pendingShops) { shop ->
                PendingShopCard(
                    shop = shop,
                    onApprove = { onApproveShop(shop) },
                    onReject = { onRejectShop(shop) }
                )
            }
        }
        
        // Users Section
        item {
            Text(
                text = "Recent Users (${uiState.users.size})",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
        
        if (uiState.users.isEmpty()) {
            item {
                EmptyStateCard(
                    icon = Icons.Default.People,
                    title = "No users found",
                    description = "No users registered yet"
                )
            }
        } else {
            items(uiState.users) { user ->
                UserCard(
                    user = user,
                    onDelete = { onDeleteUser(user) }
                )
            }
        }
    }
}

@Composable
fun AdminStatsCard(
    stats: com.hyperlocal.marketplace.data.models.AdminStats?
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Platform Overview",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    icon = Icons.Default.People,
                    label = "Total Users",
                    value = stats?.totalUsers?.toString() ?: "0"
                )
                StatItem(
                    icon = Icons.Default.Store,
                    label = "Total Shops",
                    value = stats?.totalShops?.toString() ?: "0"
                )
                StatItem(
                    icon = Icons.Default.Inventory,
                    label = "Total Products",
                    value = stats?.totalProducts?.toString() ?: "0"
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    icon = Icons.Default.Schedule,
                    label = "Pending Shops",
                    value = stats?.pendingShops?.toString() ?: "0"
                )
                StatItem(
                    icon = Icons.Default.CheckCircle,
                    label = "Approved Shops",
                    value = stats?.approvedShops?.toString() ?: "0"
                )
                StatItem(
                    icon = Icons.Default.Inventory,
                    label = "Active Products",
                    value = stats?.activeProducts?.toString() ?: "0"
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
fun PendingShopCard(
    shop: com.hyperlocal.marketplace.data.models.Shop,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
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
                text = "Address: ${shop.address}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "WhatsApp: ${shop.whatsappNumber}",
                style = MaterialTheme.typography.bodySmall
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onApprove,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Check, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Approve")
                }
                
                OutlinedButton(
                    onClick = onReject,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Close, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Reject")
                }
            }
        }
    }
}

@Composable
fun UserCard(
    user: com.hyperlocal.marketplace.data.models.User,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                
                if (user.email != null) {
                    Text(
                        text = user.email,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                if (user.phone != null) {
                    Text(
                        text = user.phone,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                
                Text(
                    text = "Role: ${user.role.name}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete User",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun EmptyStateCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String
) {
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
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}