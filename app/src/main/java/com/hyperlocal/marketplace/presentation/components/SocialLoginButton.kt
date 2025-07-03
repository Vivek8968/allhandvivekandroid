package com.hyperlocal.marketplace.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hyperlocal.marketplace.presentation.theme.AppleBlack
import com.hyperlocal.marketplace.presentation.theme.FacebookBlue
import com.hyperlocal.marketplace.presentation.theme.GoogleRed
import com.hyperlocal.marketplace.presentation.theme.Gray300
import com.hyperlocal.marketplace.presentation.theme.Gray600
import com.hyperlocal.marketplace.presentation.theme.Primary

enum class SocialLoginType {
    PHONE, GOOGLE, APPLE, SHOP
}

/**
 * Social login button component for authentication screen
 */
@Composable
fun SocialLoginButton(
    type: SocialLoginType,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    outlined: Boolean = true
) {
    val (icon, text, color) = when (type) {
        SocialLoginType.PHONE -> Triple(
            Icons.Default.Phone,
            "Continue with Phone",
            Primary
        )
        SocialLoginType.GOOGLE -> Triple(
            Icons.Default.Email,
            "Continue with Google",
            GoogleRed
        )
        SocialLoginType.APPLE -> Triple(
            Icons.Default.Email,
            "Continue with Apple",
            AppleBlack
        )
        SocialLoginType.SHOP -> Triple(
            Icons.Default.Store,
            "Create Your Own Shop",
            Primary
        )
    }
    
    if (outlined) {
        OutlinedButton(
            onClick = onClick,
            modifier = modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, if (type == SocialLoginType.SHOP) Primary else Gray300),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = if (type == SocialLoginType.SHOP) Primary else Gray600
            )
        ) {
            SocialButtonContent(icon, text)
        }
    } else {
        Button(
            onClick = onClick,
            modifier = modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = color,
                contentColor = if (type == SocialLoginType.APPLE) Color.White else Color.White
            )
        ) {
            SocialButtonContent(icon, text)
        }
    }
}

@Composable
private fun SocialButtonContent(
    icon: ImageVector,
    text: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
    }
}