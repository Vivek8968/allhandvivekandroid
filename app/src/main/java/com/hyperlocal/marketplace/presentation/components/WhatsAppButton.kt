package com.hyperlocal.marketplace.presentation.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hyperlocal.marketplace.presentation.theme.WhatsAppGreen

/**
 * WhatsApp button component that opens WhatsApp chat with the provided phone number
 * 
 * @param phoneNumber The phone number to chat with (should include country code)
 * @param message Optional pre-filled message
 * @param modifier Modifier for the button
 * @param outlined Whether to use an outlined button style (default: true)
 */
@Composable
fun WhatsAppButton(
    phoneNumber: String,
    message: String = "Hello, I'm interested in your products",
    modifier: Modifier = Modifier,
    outlined: Boolean = true
) {
    val context = LocalContext.current
    
    val openWhatsApp = {
        try {
            // Format phone number (remove spaces, +, etc.)
            val formattedPhone = phoneNumber.replace(Regex("[^0-9]"), "")
            
            // Encode message
            val encodedMessage = Uri.encode(message)
            
            // Create WhatsApp intent
            val uri = Uri.parse("https://wa.me/$formattedPhone?text=$encodedMessage")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            
            // Try to open WhatsApp app
            context.startActivity(intent)
        } catch (e: Exception) {
            // Fallback to browser if WhatsApp is not installed
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://web.whatsapp.com/send?phone=$phoneNumber&text=$message")
            )
            context.startActivity(browserIntent)
        }
    }
    
    if (outlined) {
        OutlinedButton(
            onClick = openWhatsApp,
            modifier = modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, WhatsAppGreen),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = WhatsAppGreen
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Chat,
                    contentDescription = "WhatsApp",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "WhatsApp • Chat Now",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    } else {
        Button(
            onClick = openWhatsApp,
            modifier = modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = WhatsAppGreen,
                contentColor = Color.White
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Chat,
                    contentDescription = "WhatsApp",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "WhatsApp • Chat Now",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}