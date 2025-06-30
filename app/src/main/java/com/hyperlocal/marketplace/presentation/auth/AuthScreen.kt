@file:OptIn(ExperimentalMaterial3Api::class)

package com.hyperlocal.marketplace.presentation.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hyperlocal.marketplace.data.models.UserRole
import com.hyperlocal.marketplace.presentation.common.ErrorMessage
import com.hyperlocal.marketplace.presentation.common.LoadingButton

@Composable
fun AuthScreen(
    onLoginSuccess: (UserRole) -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    
    LaunchedEffect(uiState.isLoginSuccessful) {
        if (uiState.isLoginSuccessful && uiState.userRole != null) {
            val userRole = uiState.userRole ?: return@LaunchedEffect
            onLoginSuccess(userRole)
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // App Logo and Title
        Text(
            text = "Hyperlocal Marketplace",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Discover nearby shops and products",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        // Role Selection
        Text(
            text = "Select your role:",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        RoleSelectionCard(
            title = "Customer",
            description = "Discover nearby shops and products",
            isSelected = uiState.selectedRole == UserRole.CUSTOMER,
            onClick = { viewModel.selectRole(UserRole.CUSTOMER) }
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        RoleSelectionCard(
            title = "Seller",
            description = "Register your shop and manage inventory",
            isSelected = uiState.selectedRole == UserRole.SELLER,
            onClick = { viewModel.selectRole(UserRole.SELLER) }
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        RoleSelectionCard(
            title = "Admin",
            description = "Manage platform and approve shops",
            isSelected = uiState.selectedRole == UserRole.ADMIN,
            onClick = { viewModel.selectRole(UserRole.ADMIN) }
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Phone Authentication
        OutlinedTextField(
            value = uiState.phoneNumber,
            onValueChange = viewModel::updatePhoneNumber,
            label = { Text("Phone Number") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = "Phone"
                )
            },
            placeholder = { Text("+91 9999999999") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = uiState.phoneError != null
        )
        
        if (uiState.phoneError != null) {
            Spacer(modifier = Modifier.height(4.dp))
            val phoneError = uiState.phoneError ?: ""
            Text(
                text = phoneError,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Login Buttons
        LoadingButton(
            onClick = { viewModel.loginWithPhone(context) },
            modifier = Modifier.fillMaxWidth(),
            isLoading = uiState.isLoading,
            enabled = uiState.selectedRole != null && uiState.phoneNumber.isNotBlank(),
            text = "Login with OTP"
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedButton(
            onClick = { viewModel.loginWithGoogle(context) },
            modifier = Modifier.fillMaxWidth(),
            enabled = uiState.selectedRole != null && !uiState.isLoading
        ) {
            Text("Login with Google")
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Error Message
        if (uiState.errorMessage != null) {
            val errorMessage = uiState.errorMessage ?: ""
            ErrorMessage(
                message = errorMessage,
                modifier = Modifier.fillMaxWidth()
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Demo Mode Toggle
        if (uiState.showDemoMode) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Demo Mode",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Skip authentication and explore the app with demo data",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { viewModel.enterDemoMode() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Enter Demo Mode")
                    }
                }
            }
        }
    }
}

@Composable
fun RoleSelectionCard(
    title: String,
    description: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        border = if (isSelected) {
            CardDefaults.outlinedCardBorder().copy(
                width = 2.dp
            )
        } else {
            CardDefaults.outlinedCardBorder()
        }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = if (isSelected) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isSelected) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
        }
    }
}