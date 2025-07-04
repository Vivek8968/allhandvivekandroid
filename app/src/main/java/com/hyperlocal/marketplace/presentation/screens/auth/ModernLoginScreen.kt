package com.hyperlocal.marketplace.presentation.screens.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.hyperlocal.marketplace.presentation.components.SocialLoginButton
import com.hyperlocal.marketplace.presentation.components.SocialLoginType
import com.hyperlocal.marketplace.presentation.theme.Gray300
import com.hyperlocal.marketplace.presentation.theme.Gray600
import com.hyperlocal.marketplace.presentation.theme.Primary

/**
 * Modern Myntra-style login screen with multiple authentication options
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModernLoginScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel(),
    onGoogleSignInRequested: () -> Unit = {}
) {
    var showPhoneVerification by remember { mutableStateOf(false) }
    var phoneNumber by remember { mutableStateOf("") }
    var verificationCode by remember { mutableStateOf("") }
    var isCodeSent by remember { mutableStateOf(false) }
    
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isLoggedIn) {
        if (uiState.isLoggedIn) {
            if (uiState.userRole != null) {
                // Navigate based on role
                when (uiState.userRole) {
                    "CUSTOMER" -> navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                    "SELLER" -> navController.navigate("seller_dashboard") {
                        popUpTo("login") { inclusive = true }
                    }
                    "ADMIN" -> navController.navigate("admin_dashboard") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            } else {
                // Navigate to role selection
                navController.navigate("role_selection") {
                    popUpTo("login") { inclusive = true }
                }
            }
        }
    }
    
    // Check for Google Sign-In results
    LaunchedEffect(Unit) {
        val task = com.hyperlocal.marketplace.presentation.MainActivity.googleSignInTask
        if (task != null) {
            viewModel.signInWithGoogle()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // App Logo Placeholder
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(Primary, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "HM",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Welcome Text
        Text(
            text = "Welcome Back!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Login to continue shopping",
            style = MaterialTheme.typography.bodyLarge,
            color = Gray600
        )
        
        Spacer(modifier = Modifier.height(32.dp))

        if (showPhoneVerification) {
            // Phone Verification Flow
            if (!isCodeSent) {
                // Phone Number Input
                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = { Text("Phone Number") },
                    placeholder = { Text("Enter your phone number") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = "Phone"
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Done
                    ),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Primary,
                        unfocusedBorderColor = Gray300,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (phoneNumber.isNotBlank()) {
                            viewModel.sendPhoneVerificationCode(phoneNumber)
                            isCodeSent = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Primary
                    )
                ) {
                    Text(
                        text = "Send Verification Code",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                TextButton(
                    onClick = { showPhoneVerification = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Back to Login Options",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Primary
                    )
                }
            } else {
                // Verification Code Input
                OutlinedTextField(
                    value = verificationCode,
                    onValueChange = { verificationCode = it },
                    label = { Text("Verification Code") },
                    placeholder = { Text("Enter the 6-digit code") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Primary,
                        unfocusedBorderColor = Gray300,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (verificationCode.isNotBlank()) {
                            viewModel.verifyPhoneCode(verificationCode)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Primary
                    )
                ) {
                    Text(
                        text = "Verify Code",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(
                    onClick = {
                        isCodeSent = false
                        verificationCode = ""
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Change Phone Number",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Primary
                    )
                }
            }
        } else {
            // Social Login Options
            SocialLoginButton(
                type = SocialLoginType.PHONE,
                onClick = { showPhoneVerification = true },
                outlined = false
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            SocialLoginButton(
                type = SocialLoginType.GOOGLE,
                onClick = { 
                    Toast.makeText(context, "Signing in with Google...", Toast.LENGTH_SHORT).show()
                    onGoogleSignInRequested()
                }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            SocialLoginButton(
                type = SocialLoginType.APPLE,
                onClick = { 
                    Toast.makeText(context, "Apple Sign-In will be available soon!", Toast.LENGTH_SHORT).show()
                }
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Divider with "or" text
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Divider(
                    modifier = Modifier.weight(1f),
                    color = Gray300
                )
                Text(
                    text = "  or  ",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Gray600
                )
                Divider(
                    modifier = Modifier.weight(1f),
                    color = Gray300
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Create Shop Button
            SocialLoginButton(
                type = SocialLoginType.SHOP,
                onClick = { 
                    // Navigate to register screen for shop creation
                    navController.navigate("register")
                }
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Login with Email Link
            TextButton(
                onClick = { navController.navigate("register") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Already have an account? Login",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Primary
                )
            }
        }

        // Error Message
        if (uiState.error.isNotBlank()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = uiState.error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Loading Indicator
        if (uiState.isLoading) {
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator(color = Primary)
        }
    }
}