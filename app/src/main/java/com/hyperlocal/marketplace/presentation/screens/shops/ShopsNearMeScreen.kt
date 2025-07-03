package com.hyperlocal.marketplace.presentation.screens.shops

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.hyperlocal.marketplace.data.models.Shop
import com.hyperlocal.marketplace.presentation.screens.home.ShopCard
import com.hyperlocal.marketplace.presentation.screens.home.getSampleShops
import com.hyperlocal.marketplace.presentation.theme.Primary
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun ShopsNearMeScreen(navController: NavController) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    
    var shops by remember { mutableStateOf<List<Shop>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf("") }
    var userLocation by remember { mutableStateOf<Location?>(null) }
    
    val locationPermissionState = rememberPermissionState(
        permission = Manifest.permission.ACCESS_FINE_LOCATION,
        onPermissionResult = { isGranted ->
            if (isGranted) {
                getCurrentLocation()
            } else {
                errorMessage = "Location permission is required to show nearby shops"
                isLoading = false
                shops = getSampleShops()
            }
        }
    )
    
    // Function to get current location
    fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            isLoading = true
            errorMessage = ""
            
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        userLocation = location
                        // In a real app, we would fetch shops based on location
                        // For now, we'll use sample data
                        shops = getSampleShops().sortedBy { shop ->
                            // Calculate distance (simplified)
                            val shopLat = shop.latitude ?: 0.0
                            val shopLng = shop.longitude ?: 0.0
                            val results = FloatArray(1)
                            Location.distanceBetween(
                                location.latitude, location.longitude,
                                shopLat, shopLng,
                                results
                            )
                            results[0]
                        }
                        isLoading = false
                    } else {
                        errorMessage = "Unable to get location. Please try again."
                        isLoading = false
                        shops = getSampleShops()
                    }
                }
                .addOnFailureListener {
                    errorMessage = "Failed to get location: ${it.message}"
                    isLoading = false
                    shops = getSampleShops()
                }
        } else {
            locationPermissionState.launchPermissionRequest()
            isLoading = false
            shops = getSampleShops()
        }
    }
    
    // Request location on first launch
    LaunchedEffect(Unit) {
        getCurrentLocation()
    }
    
    // Observe permission changes
    LaunchedEffect(locationPermissionState.status) {
        if (locationPermissionState.status.isGranted) {
            getCurrentLocation()
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "Shops Near Me",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            actions = {
                // Refresh button
                IconButton(onClick = { getCurrentLocation() }) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh"
                    )
                }
            }
        )
        
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Primary)
            }
        } else if (errorMessage.isNotBlank()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = errorMessage,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { getCurrentLocation() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Primary
                        )
                    ) {
                        Text("Try Again")
                    }
                }
            }
        } else {
            if (userLocation != null) {
                Text(
                    text = "Showing shops near your location",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
            
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(shops) { shop ->
                    ShopCard(
                        shop = shop,
                        onClick = {
                            // Navigate to shop details
                        }
                    )
                }
            }
        }
    }
}