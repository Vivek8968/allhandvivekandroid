package com.hyperlocal.marketplace.presentation.screens.help

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hyperlocal.marketplace.presentation.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "Help & Contact",
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
                HelpMenuItem(
                    icon = Icons.Default.QuestionMark,
                    title = "FAQ",
                    subtitle = "Frequently asked questions",
                    onClick = { /* Handle FAQ */ }
                )
            }
            
            item {
                HelpMenuItem(
                    icon = Icons.Default.Phone,
                    title = "Contact Support",
                    subtitle = "Get help from our support team",
                    onClick = { /* Handle contact */ }
                )
            }
            
            item {
                HelpMenuItem(
                    icon = Icons.Default.Email,
                    title = "Email Us",
                    subtitle = "Send us an email",
                    onClick = { /* Handle email */ }
                )
            }
            
            item {
                HelpMenuItem(
                    icon = Icons.Default.Chat,
                    title = "Live Chat",
                    subtitle = "Chat with our support team",
                    onClick = { /* Handle chat */ }
                )
            }
            
            item {
                HelpMenuItem(
                    icon = Icons.Default.Feedback,
                    title = "Send Feedback",
                    subtitle = "Help us improve the app",
                    onClick = { /* Handle feedback */ }
                )
            }
            
            item {
                HelpMenuItem(
                    icon = Icons.Default.Policy,
                    title = "Privacy Policy",
                    subtitle = "Read our privacy policy",
                    onClick = { /* Handle privacy */ }
                )
            }
            
            item {
                HelpMenuItem(
                    icon = Icons.Default.Gavel,
                    title = "Terms of Service",
                    subtitle = "Read our terms of service",
                    onClick = { /* Handle terms */ }
                )
            }
        }
    }
}

@Composable
fun HelpMenuItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
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
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = Gray600
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Navigate",
                tint = IconTint,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}