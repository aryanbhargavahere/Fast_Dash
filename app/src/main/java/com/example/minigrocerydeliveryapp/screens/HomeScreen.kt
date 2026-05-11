package com.example.minigrocerydeliveryapp.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.minigrocerydeliveryapp.R
import com.example.minigrocerydeliveryapp.Room.GroceryViewModel
import com.example.minigrocerydeliveryapp.datamodel.CategoryItem
import com.example.minigrocerydeliveryapp.datamodel.Product

@Composable
fun HomeScreen(
    viewModel: GroceryViewModel,
    onCartClick: () -> Unit,
    onAddressClick: () -> Unit,
    onSeeAllCategories: () -> Unit,
    onAccountClick: () -> Unit
) {
    val cartItems by viewModel.cartItems.collectAsState()
    val isDark = viewModel.isDarkMode

    val bgColor = if (isDark) Color(0xFF121212) else Color(0xFFF8F9FA)
    val surfaceColor = if (isDark) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDark) Color.White else Color.Black

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(surfaceColor), // Use surface color
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    TopLocationHeader(
                        currentAddress = viewModel.userAddress,
                        isDark = isDark,
                        onChangeClick = onAddressClick
                    )
                }

                IconButton(
                    onClick = onAccountClick,
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = if (isDark) Color(0xFF2E7D32).copy(alpha = 0.2f) else Color(0xFFE8F5E9),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Account",
                            tint = if (isDark) Color(0xFF81C784) else Color(0xFF2E7D32),
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                }
            }
        },
        bottomBar = {
            BottomNavBar(
                cartCount = cartItems.size,
                isDark = isDark,
                onCartClick = onCartClick,
                onCategoriesClick = onSeeAllCategories,
                onAccountClick = onAccountClick
            )
        },
        containerColor = bgColor
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            item {
                SearchBar(
                    query = viewModel.searchQuery,
                    isDark = isDark,
                    onQueryChange = { viewModel.searchQuery = it }
                )
            }

            item { PromoBanner() }

            item { CategoryRow(isDark = isDark, onSeeAllClick = onSeeAllCategories) }

            item {
                Text(
                    "Trending Now",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
            }

            items(viewModel.filteredProducts.chunked(2)) { rowItems ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    for (product in rowItems) {
                        ProductCard(
                            product = product,
                            isDark = isDark,
                            modifier = Modifier.weight(1f),
                            onAddClick = { viewModel.addToCart(product) }
                        )
                    }
                    if (rowItems.size == 1) Spacer(modifier = Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun TopLocationHeader(currentAddress: String, isDark: Boolean, onChangeClick: () -> Unit) {
    val textColor = if (isDark) Color.White else Color.Black
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onChangeClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.LocationOn, null, tint = if (isDark) Color(0xFF81C784) else Color(0xFF2E7D32))
        Column(modifier = Modifier.padding(start = 8.dp).weight(1f)) {
            Text("Deliver to", fontSize = 10.sp, color = Color.Gray)
            Text(currentAddress, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textColor)
        }
        Icon(Icons.Default.ChevronRight, null, tint = Color.Gray)
    }
}

@Composable
fun SearchBar(query: String, isDark: Boolean, onQueryChange: (String) -> Unit) {
    val containerColor = if (isDark) Color(0xFF2C2C2C) else Color(0xFFF1F3F4)
    val textColor = if (isDark) Color.White else Color.Black

    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(56.dp),
        placeholder = { Text("Search for milk, eggs...", color = Color.Gray) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = if (isDark) Color.Gray else Color.Black) },
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = containerColor,
            unfocusedContainerColor = containerColor,
            focusedTextColor = textColor,
            unfocusedTextColor = textColor,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun PromoBanner() {
    Card(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF27C96D))
    ) {
        Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Get 50% OFF", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                Text("on your first 3 orders!", color = Color.White, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Claim Now", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
            Icon(Icons.Default.ShoppingBasket, null, modifier = Modifier.size(80.dp), tint = Color.Black.copy(alpha = 0.1f))
        }
    }
}

@Composable
fun CategoryRow(isDark: Boolean, onSeeAllClick: () -> Unit) {
    val categories = listOf(
        CategoryItem("Fruits", R.drawable.ic_fruits),
        CategoryItem("Vegetables", R.drawable.veggies),
        CategoryItem("Dairy", R.drawable.dairy),
        CategoryItem("Snacks", R.drawable.snacks)
    )
    val textColor = if (isDark) Color.White else Color.Black

    Column {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Shop by Category", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = textColor)
            Text(
                "See All",
                color = if (isDark) Color(0xFF81C784) else Color(0xFF2E7D32),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.clickable { onSeeAllClick() }
            )
        }
        LazyRow(contentPadding = PaddingValues(16.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(categories) { item ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Surface(
                        modifier = Modifier.size(75.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, if (isDark) Color.DarkGray else Color(0xFFEEEEEE)),
                        color = if (isDark) Color(0xFF1E1E1E) else Color.White
                    ) {
                        Image(painter = painterResource(item.iconRes), contentDescription = null, modifier = Modifier.padding(12.dp))
                    }
                    Text(item.title, modifier = Modifier.padding(top = 8.dp), fontSize = 12.sp, color = textColor)
                }
            }
        }
    }
}

@Composable
fun ProductCard(product: Product, isDark: Boolean, modifier: Modifier = Modifier, onAddClick: () -> Unit) {
    val cardColor = if (isDark) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDark) Color.White else Color.Black

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        border = BorderStroke(1.dp, if (isDark) Color.DarkGray else Color(0xFFEEEEEE))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Image(
                painter = painterResource(product.imageRes),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().height(100.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(product.shop, fontSize = 10.sp, color = Color.Gray)
            Text(product.name, fontWeight = FontWeight.Bold, fontSize = 14.sp, maxLines = 1, color = textColor)
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("₹${product.price.toInt()}", fontWeight = FontWeight.Bold, color = if (isDark) Color(0xFF81C784) else Color(0xFF2E7D32))
                IconButton(
                    onClick = onAddClick,
                    modifier = Modifier.size(32.dp).background(Color(0xFFFFC107), RoundedCornerShape(8.dp))
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = Color.Black)
                }
            }
        }
    }
}

@Composable
fun BottomNavBar(
    cartCount: Int,
    isDark: Boolean,
    onCartClick: () -> Unit,
    onCategoriesClick: () -> Unit,
    onAccountClick: () -> Unit
) {
    val containerColor = if (isDark) Color(0xFF1E1E1E) else Color.White
    val selectedColor = if (isDark) Color(0xFF81C784) else Color(0xFF2E7D32)

    NavigationBar(containerColor = containerColor, tonalElevation = 8.dp) {
        NavigationBarItem(
            selected = true,
            onClick = {},
            icon = { Icon(Icons.Default.Home, null) },
            label = { Text("Home") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = selectedColor,
                selectedTextColor = selectedColor,
                unselectedIconColor = Color.Gray,
                indicatorColor = if (isDark) Color(0xFF2E7D32).copy(alpha = 0.2f) else Color(0xFFE8F5E9)
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = onCategoriesClick,
            icon = { Icon(Icons.Default.GridView, null) },
            label = { Text("Categories") },
            colors = NavigationBarItemDefaults.colors(unselectedIconColor = Color.Gray, unselectedTextColor = Color.Gray)
        )
        NavigationBarItem(
            selected = false,
            onClick = onCartClick,
            icon = {
                BadgedBox(badge = { if (cartCount > 0) Badge { Text(cartCount.toString()) } }) {
                    Icon(Icons.Default.ShoppingCart, null)
                }
            },
            label = { Text("Cart") },
            colors = NavigationBarItemDefaults.colors(unselectedIconColor = Color.Gray, unselectedTextColor = Color.Gray)
        )
        NavigationBarItem(
            selected = false,
            onClick = onAccountClick,
            icon = { Icon(Icons.Default.Person, null) },
            label = { Text("Account") },
            colors = NavigationBarItemDefaults.colors(unselectedIconColor = Color.Gray, unselectedTextColor = Color.Gray)
        )
    }
}