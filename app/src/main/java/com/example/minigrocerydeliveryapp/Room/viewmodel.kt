package com.example.minigrocerydeliveryapp.Room

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.minigrocerydeliveryapp.R
import com.example.minigrocerydeliveryapp.datamodel.Order
import com.example.minigrocerydeliveryapp.datamodel.Product
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class GroceryViewModel(private val dao: CartDao, context: Context) : ViewModel() {

    private val prefs: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    var userName by mutableStateOf(prefs.getString("user_name", "") ?: "")
    var userPhone by mutableStateOf(prefs.getString("user_phone", "") ?: "")
    var isLoggedIn by mutableStateOf(prefs.getBoolean("is_logged_in", false))
    var isDarkMode by mutableStateOf(prefs.getBoolean("dark_mode", false))

    // --- NEW: Dynamic Address State (Non-Hardcoded) ---
    var userAddress by mutableStateOf("Add Your Address")
    private val _savedAddresses = MutableStateFlow<List<String>>(emptyList())
    val savedAddresses = _savedAddresses.asStateFlow()

    // --- NEW: Order History State ---
    private val _orderHistory = MutableStateFlow<List<Order>>(emptyList())
    val orderHistory = _orderHistory.asStateFlow()

    var userStatus by mutableStateOf("Gold Member")
    var activeOrdersCount by mutableIntStateOf(0)
    var savedListsCount by mutableIntStateOf(0)

    private val allProducts = listOf(
        Product(1, "Fresh Red Apple", "Fruit Shop", 120.0, R.drawable.apples, "1kg"),
        Product(2, "Cherry Tomatoes", "Garden Fresh", 80.0, R.drawable.tomatoes, "250g"),
        Product(3, "Whole Milk 1L", "Dairy Farm", 60.0, R.drawable.milk, "1L"),
        Product(4, "Organic Eggs", "Pantry Essentials", 90.0, R.drawable.eggs, "6pcs")
    )

    var searchQuery by mutableStateOf("")
    val filteredProducts: List<Product>
        get() = if (searchQuery.isEmpty()) allProducts
        else allProducts.filter { it.name.contains(searchQuery, ignoreCase = true) }

    val cartItems = dao.getCartItems().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val totalAmount = cartItems.map { items -> items.sumOf { it.price * it.quantity } }
        .stateIn(viewModelScope, SharingStarted.Lazily, 0.0)

    // --- NEW: Address Logic ---
    fun addAddress(newAddress: String) {
        if (newAddress.isNotBlank()) {
            _savedAddresses.value = _savedAddresses.value + newAddress
            userAddress = newAddress // Set newly added address as active
        }
    }

    fun selectAddress(address: String) {
        userAddress = address
    }

    // --- NEW/UPDATED: Place Order with History Recording ---
    fun placeOrder(amount: Double, orderId: String) {
        viewModelScope.launch {
            if (cartItems.value.isNotEmpty()) {
                val newOrder = Order(
                    id = orderId,
                    date = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date()),
                    amount = amount,
                    status = "Delivered"
                )
                // Update history state
                _orderHistory.value = listOf(newOrder) + _orderHistory.value
                activeOrdersCount += 1
                clearCart()
            }
        }
    }

    fun loginUser(name: String, phone: String) {
        userName = name
        userPhone = phone
        isLoggedIn = true
        prefs.edit().apply {
            putString("user_name", name)
            putString("user_phone", phone)
            putBoolean("is_logged_in", true)
            apply()
        }
    }

    fun logout() {
        userName = ""
        userPhone = ""
        isLoggedIn = false
        activeOrdersCount = 0
        prefs.edit().clear().apply()
    }

    fun toggleDarkMode() {
        isDarkMode = !isDarkMode
        prefs.edit().putBoolean("dark_mode", isDarkMode).apply()
    }

    fun addToCart(product: Product) {
        viewModelScope.launch {
            val existingItem = cartItems.value.find { it.id == product.id }
            if (existingItem != null) {
                dao.updateItem(existingItem.copy(quantity = existingItem.quantity + 1))
            } else {
                dao.updateItem(
                    CartItem(
                        id = product.id,
                        name = product.name,
                        price = product.price,
                        imageRes = product.imageRes,
                        unit = product.unit,
                        quantity = 1
                    )
                )
            }
        }
    }

    fun updateQuantity(item: CartItem, change: Int) {
        viewModelScope.launch {
            val newQuantity = item.quantity + change
            if (newQuantity > 0) {
                dao.updateItem(item.copy(quantity = newQuantity))
            } else {
                dao.removeItem(item)
            }
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            cartItems.value.forEach { dao.removeItem(it) }
        }
    }
}