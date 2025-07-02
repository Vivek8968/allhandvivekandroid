package com.hyperlocal.marketplace.presentation.screens.products

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hyperlocal.marketplace.data.models.Product
import com.hyperlocal.marketplace.presentation.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsScreen(navController: NavController) {
    val products = getSampleProducts()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "Products",
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(products) { product ->
                ProductCard(product = product)
            }
        }
    }
}

@Composable
fun ProductCard(product: Product) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Handle product click */ },
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
                        text = product.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Text(
                        text = product.category,
                        style = MaterialTheme.typography.bodySmall,
                        color = Gray600,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                    
                    product.description?.let { description ->
                        Text(
                            text = description,
                            style = MaterialTheme.typography.bodySmall,
                            color = Gray600,
                            modifier = Modifier.padding(top = 4.dp),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    
                    Text(
                        text = "${product.quantity} ${product.unit}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Gray600,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "$${String.format("%.2f", product.price)}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Primary
                    )
                    
                    Text(
                        text = if (product.inStock) "In Stock" else "Out of Stock",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (product.inStock) Success else Error,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}

fun getSampleProducts(): List<Product> {
    return listOf(
        Product(
            id = "1",
            name = "Fresh Apples",
            description = "Crisp and sweet red apples",
            price = 3.99,
            category = "Fruits",
            imageUrl = null,
            shopId = "1",
            inStock = true,
            quantity = 1,
            unit = "kg"
        ),
        Product(
            id = "2",
            name = "Wireless Headphones",
            description = "High-quality bluetooth headphones",
            price = 89.99,
            category = "Electronics",
            imageUrl = null,
            shopId = "2",
            inStock = true,
            quantity = 1,
            unit = "piece"
        ),
        Product(
            id = "3",
            name = "Cotton T-Shirt",
            description = "Comfortable cotton t-shirt",
            price = 19.99,
            category = "Clothing",
            imageUrl = null,
            shopId = "3",
            inStock = false,
            quantity = 1,
            unit = "piece"
        )
    )
}