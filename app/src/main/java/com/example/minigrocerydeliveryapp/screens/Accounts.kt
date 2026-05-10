package com.example.minigrocerydeliveryapp.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.minigrocerydeliveryapp.Room.GroceryViewModel

@Composable
fun AccountScreen(
    viewModel: GroceryViewModel,
    onLogout: () -> Unit,
    onOrdersClick: () -> Unit = {}, // Navigation placeholders
    onSavedClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // --- PROFILE HEADER ---
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp)
        ) {
            Box(contentAlignment = Alignment.BottomEnd) {
                Surface(
                    shape = CircleShape,
                    color = Color(0xFFE8F5E9),
                    modifier = Modifier.size(80.dp)
                ) {
                    Icon(
                        Icons.Default.AccountCircle,
                        contentDescription = null,
                        tint = Color(0xFF006D3B),
                        modifier = Modifier.padding(8.dp)
                    )
                }
                IconButton(
                    onClick = { /* Action to Edit Profile */ },
                    modifier = Modifier
                        .size(28.dp)
                        .background(Color(0xFF006D3B), CircleShape)
                ) {
                    Icon(Icons.Default.Edit, null, tint = Color.White, modifier = Modifier.size(14.dp))
                }
            }

            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(
                    text = viewModel.userName.ifEmpty { "User Name" },
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
                Text(
                    text = if (viewModel.userPhone.isEmpty()) "000 000 0000" else "+91 ${viewModel.userPhone}",
                    color = Color.Gray
                )
                Spacer(Modifier.height(8.dp))
                Surface(
                    color = Color(0xFFE8F5E9),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Verified, null, tint = Color(0xFF006D3B), modifier = Modifier.size(14.dp))
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = viewModel.userStatus.ifEmpty {"" },
                            color = Color(0xFF006D3B),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // --- STATS GRID (Real-time simplified) ---
        Row(Modifier.fillMaxWidth()) {
            // value now comes directly from viewModel.activeOrdersCount
            StatCard(
                value = viewModel.activeOrdersCount.toString(),
                label = "Active Orders",
                modifier = Modifier.weight(1f).clickable { onOrdersClick() }
            )
            // Show Saved Lists alongside Active Orders
            StatCard(
                value = viewModel.savedListsCount.toString(),
                label = "Saved Lists",
                modifier = Modifier.weight(1f).clickable { onSavedClick() }
            )
        }

        Spacer(Modifier.height(24.dp))

        // --- MENU LIST ---
        MenuListItem(
            icon = Icons.Default.ShoppingBag,
            title = "My Orders",
            onClick = onOrdersClick
        )
        MenuListItem(Icons.Default.LocationOn, "Saved Addresses")
        MenuListItem(Icons.Default.Payment, "Payment Methods")
        MenuListItem(Icons.Default.Notifications, "Notifications", hasBadge = true)
        MenuListItem(Icons.Default.Help, "Help & Support")

        // Logout Section
        MenuListItem(
            icon = Icons.Default.Logout,
            title = "Logout",
            tint = Color.Red,
            onClick = onLogout
        )

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun StatCard(value: String, label: String, modifier: Modifier, titleColor: Color = Color(0xFF006D3B)) {
    Card(
        modifier = modifier.padding(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F8FA)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            Modifier.padding(16.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = titleColor)
            Text(label, fontSize = 12.sp, color = Color.Gray)
        }
    }
}

@Composable
fun MenuListItem(icon: ImageVector, title: String, tint: Color = Color.Black, hasBadge: Boolean = false, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(shape = CircleShape, color = Color(0xFFF7F8FA), modifier = Modifier.size(40.dp)) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.padding(8.dp),
                tint = if (tint == Color.Red) Color.Red else Color(0xFF006D3B)
            )
        }
        Text(
            text = title,
            modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
            fontWeight = FontWeight.Medium,
            color = tint
        )
        if (hasBadge) {
            Box(
                Modifier
                    .size(8.dp)
                    .background(Color.Red, CircleShape)
            )
        }
        Icon(Icons.Default.ChevronRight, null, tint = Color.LightGray)
    }
}