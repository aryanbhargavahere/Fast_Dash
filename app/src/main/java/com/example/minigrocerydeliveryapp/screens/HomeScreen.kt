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
import androidx.compose.ui.draw.clip
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
    onAccountClick: () -> Unit // Edit: Ensure this is used
) {
    val cartItems by viewModel.cartItems.collectAsState()

    Scaffold(
        topBar = {
            // Edit: Added Row to include the Profile Icon next to the Location
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    TopLocationHeader(
                        currentAddress = viewModel.userAddress,
                        onChangeClick = onAddressClick
                    )
                }

                // Edit: Profile Icon in the Top Bar (matches Account Screen UI)
                IconButton(
                    onClick = onAccountClick,
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFFE8F5E9),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Account",
                            tint = Color(0xFF2E7D32),
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                }
            }
        },
        bottomBar = {
            BottomNavBar(
                cartCount = cartItems.size,
                onCartClick = onCartClick,
                onCategoriesClick = onSeeAllCategories,
                onAccountClick = onAccountClick // Edit: Pass the click to Bottom Bar
            )
        },
        containerColor = Color(0xFFF8F9FA)
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            item {
                SearchBar(
                    query = viewModel.searchQuery,
                    onQueryChange = { viewModel.searchQuery = it }
                )
            }

            item { PromoBanner() }

            item { CategoryRow(onSeeAllClick = onSeeAllCategories) }

            item {
                Text(
                    "Trending Now",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
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
fun TopLocationHeader(currentAddress: String, onChangeClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onChangeClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.LocationOn, null, tint = Color(0xFF2E7D32))
        Column(modifier = Modifier.padding(start = 8.dp).weight(1f)) {
            Text("Deliver to", fontSize = 10.sp, color = Color.Gray)
            Text(currentAddress, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
        Icon(Icons.Default.ChevronRight, null, tint = Color.Gray)
    }
}

@Composable
fun SearchBar(query: String, onQueryChange: (String) -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(56.dp),
        placeholder = { Text("Search for milk, eggs...", color = Color.Gray) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFF1F3F4),
            unfocusedContainerColor = Color(0xFFF1F3F4),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun PromoBanner() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
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
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    Text("Claim Now", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
            Icon(Icons.Default.ShoppingBasket, null, modifier = Modifier.size(80.dp), tint = Color.Black.copy(alpha = 0.1f))
        }
    }
}

@Composable
fun CategoryRow(onSeeAllClick: () -> Unit) {
    val categories = listOf(
        CategoryItem("Fruits", R.drawable.ic_fruits),
        CategoryItem("Vegetables", R.drawable.veggies),
        CategoryItem("Dairy", R.drawable.dairy),
        CategoryItem("Snacks", R.drawable.snacks)
    )

    Column {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Shop by Category", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(
                "See All",
                color = Color(0xFF2E7D32),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.clickable { onSeeAllClick() }
            )
        }
        LazyRow(
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(categories) { item ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Surface(
                        modifier = Modifier.size(75.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color(0xFFEEEEEE)),
                        color = Color.White
                    ) {
                        Image(painter = painterResource(item.iconRes), contentDescription = null, modifier = Modifier.padding(12.dp))
                    }
                    Text(item.title, modifier = Modifier.padding(top = 8.dp), fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun ProductCard(product: Product, modifier: Modifier = Modifier, onAddClick: () -> Unit) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFEEEEEE))
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
            Text(product.name, fontWeight = FontWeight.Bold, fontSize = 14.sp, maxLines = 1)
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("$${product.price}", fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
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
    onCartClick: () -> Unit,
    onCategoriesClick: () -> Unit,
    onAccountClick: () -> Unit // Edit: Parameter added
) {
    NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
        NavigationBarItem(
            selected = true,
            onClick = {},
            icon = { Icon(Icons.Default.Home, null) },
            label = { Text("Home") },
            colors = NavigationBarItemDefaults.colors(selectedIconColor = Color(0xFF2E7D32), indicatorColor = Color(0xFFE8F5E9))
        )
        NavigationBarItem(
            selected = false,
            onClick = onCategoriesClick,
            icon = { Icon(Icons.Default.GridView, null) },
            label = { Text("Categories") }
        )
        NavigationBarItem(
            selected = false,
            onClick = onCartClick,
            icon = {
                BadgedBox(badge = { if (cartCount > 0) Badge { Text(cartCount.toString()) } }) {
                    Icon(Icons.Default.ShoppingCart, null)
                }
            },
            label = { Text("Cart") }
        )
        NavigationBarItem(
            selected = false,
            onClick = onAccountClick, // Edit: Logic linked
            icon = { Icon(Icons.Default.Person, null) },
            label = { Text("Account") }
        )
    }
}