package com.example.minigrocerydeliveryapp.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.minigrocerydeliveryapp.Room.GroceryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    viewModel: GroceryViewModel,
    onLogout: () -> Unit,
    onBack: () -> Unit,
    onNavigateHome: () -> Unit,
    onNavigateCart: () -> Unit,
    onOrdersClick: () -> Unit,
    onSavedAddressesClick: () -> Unit
) {
    val isDark = viewModel.isDarkMode
    val orderHistory by viewModel.orderHistory.collectAsState()

    val textColor = if (isDark) Color.White else Color.Black
    val surfaceColor = if (isDark) Color(0xFF1E1E1E) else Color.White

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Profile", fontWeight = FontWeight.Bold, color = textColor) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = textColor)
                    }
                }
            )
        },
        containerColor = if (isDark) Color(0xFF121212) else Color(0xFFF7F8FA)
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // --- USER HEADER ---
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF006D3B)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = viewModel.userName.take(1).uppercase(),
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column(Modifier.padding(start = 16.dp)) {
                    Text(viewModel.userName, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = textColor)
                    Text(viewModel.userPhone, color = Color.Gray)
                }
            }

            Spacer(Modifier.height(24.dp))

            // --- NEW: ORDER SUMMARY BOXES ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OrderStatCard(
                    modifier = Modifier.weight(1f),
                    count = viewModel.activeOrdersCount.toString(),
                    label = "Active Orders",
                    icon = Icons.Default.DirectionsRun,
                    color = Color(0xFFFFD523), // Yellow for active
                    isDark = isDark
                )
                OrderStatCard(
                    modifier = Modifier.weight(1f),
                    count = orderHistory.size.toString(),
                    label = "Delivered",
                    icon = Icons.Default.Verified,
                    color = Color(0xFF006D3B), // Green for delivered
                    isDark = isDark
                )
            }

            Spacer(Modifier.height(32.dp))

            // --- ACCOUNT OPTIONS ---
            AccountOptionItem(Icons.Default.ShoppingBag, "Order History", "View previous orders", isDark, onOrdersClick)
            AccountOptionItem(Icons.Default.LocationOn, "Saved Addresses", viewModel.userAddress, isDark, onSavedAddressesClick)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.DarkMode, null, tint = Color(0xFF006D3B))
                Text("Dark Mode", Modifier.padding(start = 16.dp).weight(1f), color = textColor)
                Switch(checked = isDark, onCheckedChange = { viewModel.toggleDarkMode() })
            }

            Spacer(Modifier.height(40.dp))

            Button(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red.copy(alpha = 0.1f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Logout", color = Color.Red, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun OrderStatCard(
    modifier: Modifier,
    count: String,
    label: String,
    icon: ImageVector,
    color: Color,
    isDark: Boolean
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) Color(0xFF1E1E1E) else Color.White
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(28.dp))
            Spacer(Modifier.height(8.dp))
            Text(count, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = if (isDark) Color.White else Color.Black)
            Text(label, fontSize = 12.sp, color = Color.Gray)
        }
    }
}

@Composable
fun AccountOptionItem(icon: ImageVector, title: String, subtitle: String, isDark: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = Color(0xFF006D3B))
        Column(Modifier.padding(start = 16.dp).weight(1f)) {
            Text(title, fontWeight = FontWeight.Bold, color = if (isDark) Color.White else Color.Black)
            Text(subtitle, fontSize = 12.sp, color = Color.Gray, maxLines = 1)
        }
        Icon(Icons.Default.ChevronRight, null, tint = Color.Gray)
    }
}