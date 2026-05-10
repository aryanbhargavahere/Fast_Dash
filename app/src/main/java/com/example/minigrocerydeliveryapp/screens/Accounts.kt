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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    onNavigateCart: () -> Unit
) {
    // Dynamic Theme Colors
    val isDark = viewModel.isDarkMode
    val backgroundColor = if (isDark) Color(0xFF121212) else Color.White
    val textColor = if (isDark) Color.White else Color.Black
    val surfaceColor = if (isDark) Color(0xFF1E1E1E) else Color(0xFFF7F8FA)
    val appBarColor = if (isDark) Color(0xFF121212) else Color.White
    val bottomBarColor = if (isDark) Color(0xFF1E1E1E) else Color.White

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile", fontWeight = FontWeight.Bold, color = textColor) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Go Back",
                            tint = if (isDark) Color.White else Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = appBarColor)
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = bottomBarColor,
                contentColor = Color(0xFF006D3B)
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = false,
                    onClick = onNavigateHome,
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = Color.Gray,
                        selectedIconColor = Color(0xFF006D3B),
                        indicatorColor = if (isDark) Color(0xFF2E7D32).copy(alpha = 0.2f) else Color(0xFFE8F5E9)
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Cart") },
                    label = { Text("Cart") },
                    selected = false,
                    onClick = onNavigateCart,
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = Color.Gray,
                        selectedIconColor = Color(0xFF006D3B),
                        indicatorColor = if (isDark) Color(0xFF2E7D32).copy(alpha = 0.2f) else Color(0xFFE8F5E9)
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Account") },
                    label = { Text("Account") },
                    selected = true,
                    onClick = { /* Stay here */ },
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = Color.Gray,
                        selectedIconColor = Color(0xFF006D3B),
                        indicatorColor = if (isDark) Color(0xFF2E7D32).copy(alpha = 0.2f) else Color(0xFFE8F5E9)
                    )
                )
            }
        },
        containerColor = backgroundColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(backgroundColor)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            // --- PROFILE HEADER ---
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp)
            ) {
                Box(contentAlignment = Alignment.BottomEnd) {
                    Surface(
                        shape = CircleShape,
                        color = if (isDark) Color(0xFF2E7D32).copy(alpha = 0.2f) else Color(0xFFE8F5E9),
                        modifier = Modifier.size(80.dp)
                    ) {
                        Icon(
                            Icons.Default.AccountCircle,
                            contentDescription = null,
                            tint = if (isDark) Color(0xFF81C784) else Color(0xFF006D3B),
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
                        fontSize = 22.sp,
                        color = textColor
                    )
                    Text(
                        text = if (viewModel.userPhone.isEmpty()) "000 000 0000" else "+91 ${viewModel.userPhone}",
                        color = Color.Gray
                    )
                    Spacer(Modifier.height(8.dp))
                    Surface(
                        color = if (isDark) Color(0xFF2E7D32).copy(alpha = 0.2f) else Color(0xFFE8F5E9),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Verified, null, tint = Color(0xFF006D3B), modifier = Modifier.size(14.dp))
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = viewModel.userStatus.ifEmpty { "Gold Member" },
                                color = if (isDark) Color(0xFF81C784) else Color(0xFF006D3B),
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            // --- STATS GRID ---
            Row(Modifier.fillMaxWidth()) {
                StatCard(
                    value = viewModel.activeOrdersCount.toString(),
                    label = "Active Orders",
                    isDarkMode = isDark,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    value = viewModel.savedListsCount.toString(),
                    label = "Saved Lists",
                    isDarkMode = isDark,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(24.dp))

            // --- MENU LIST ---
            MenuListItem(Icons.Default.ShoppingBag, "My Orders", isDarkMode = isDark)

            // --- DARK MODE TOGGLE ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(shape = CircleShape, color = surfaceColor, modifier = Modifier.size(40.dp)) {
                    Icon(
                        Icons.Default.DarkMode,
                        null,
                        modifier = Modifier.padding(8.dp),
                        tint = if (isDark) Color(0xFFFFD600) else Color(0xFF006D3B)
                    )
                }
                Text(
                    "Dark Mode",
                    modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
                    fontWeight = FontWeight.Medium,
                    color = textColor
                )
                Switch(
                    checked = isDark,
                    onCheckedChange = { viewModel.toggleDarkMode() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFF006D3B)
                    )
                )
            }

            MenuListItem(Icons.Default.LocationOn, "Saved Addresses", isDarkMode = isDark)
            MenuListItem(Icons.Default.Payment, "Payment Methods", isDarkMode = isDark)
            MenuListItem(Icons.Default.Notifications, "Notifications", hasBadge = true, isDarkMode = isDark)
            MenuListItem(Icons.Default.Help, "Help & Support", isDarkMode = isDark)

            MenuListItem(
                icon = Icons.Default.Logout,
                title = "Logout",
                tint = Color.Red,
                isDarkMode = isDark,
                onClick = onLogout
            )

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun StatCard(value: String, label: String, modifier: Modifier, isDarkMode: Boolean) {
    val cardBg = if (isDarkMode) Color(0xFF1E1E1E) else Color(0xFFF7F8FA)
    val titleColor = if (isDarkMode) Color(0xFF81C784) else Color(0xFF006D3B)
    val labelColor = if (isDarkMode) Color.LightGray else Color.Gray

    Card(
        modifier = modifier.padding(4.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            Modifier.padding(16.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = titleColor)
            Text(label, fontSize = 12.sp, color = labelColor)
        }
    }
}

@Composable
fun MenuListItem(
    icon: ImageVector,
    title: String,
    isDarkMode: Boolean,
    tint: Color? = null,
    hasBadge: Boolean = false,
    onClick: () -> Unit = {}
) {
    val textColor = if (isDarkMode) Color.White else Color.Black
    val surfaceColor = if (isDarkMode) Color(0xFF1E1E1E) else Color(0xFFF7F8FA)
    val iconTint = tint ?: if (isDarkMode) Color(0xFF81C784) else Color(0xFF006D3B)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(shape = CircleShape, color = surfaceColor, modifier = Modifier.size(40.dp)) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.padding(8.dp),
                tint = iconTint
            )
        }
        Text(
            text = title,
            modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
            fontWeight = FontWeight.Medium,
            color = if (tint == Color.Red) Color.Red else textColor
        )
        if (hasBadge) {
            Box(Modifier.size(8.dp).background(Color.Red, CircleShape))
        }
        Icon(Icons.Default.ChevronRight, null, tint = Color.LightGray)
    }
}