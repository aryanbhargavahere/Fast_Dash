package com.example.minigrocerydeliveryapp.Room

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.minigrocerydeliveryapp.R
import com.example.minigrocerydeliveryapp.datamodel.Product
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class GroceryViewModel(private val dao: CartDao) : ViewModel() {

    // --- User Session Data ---
    var userName by mutableStateOf("")
    var userPhone by mutableStateOf("")
    var userAddress by mutableStateOf("Add Your Address")
    var userStatus by mutableStateOf("Gold Member")

    // --- Dynamic Stats ---
    // This will be displayed in the Account Screen
    var activeOrdersCount by mutableIntStateOf(0)
    var savedListsCount by mutableIntStateOf(0)

    // --- Shop Inventory ---
    private val allProducts = listOf(
        Product(1, "Fresh Red Apple", "Fruit Shop", 1.20, R.drawable.apples, "1kg"),
        Product(2, "Cherry Tomatoes", "Garden Fresh", 3.50, R.drawable.tomatoes, "250g"),
        Product(3, "Whole Milk 1L", "Dairy Farm", 2.80, R.drawable.milk, "1L"),
        Product(4, "Organic Eggs", "Pantry Essentials", 4.10, R.drawable.eggs, "6pcs")
    )

    // --- Live Search Logic ---
    var searchQuery by mutableStateOf("")
    val filteredProducts: List<Product> get() = if (searchQuery.isEmpty()) allProducts
    else allProducts.filter { it.name.contains(searchQuery, ignoreCase = true) }

    // --- Cart Data Management ---
    val cartItems = dao.getCartItems().stateIn(
        viewModelScope, SharingStarted.Lazily, emptyList()
    )

    val totalAmount = cartItems.map { items ->
        items.sumOf { it.price * it.quantity }
    }.stateIn(viewModelScope, SharingStarted.Lazily, 0.0)

    // --- Actions ---

    // Updated: Logic to increase order count and clear cart
    fun placeOrder() {
        viewModelScope.launch {
            if (cartItems.value.isNotEmpty()) {
                activeOrdersCount += 1 // Increment the counter
                clearCart()            // Remove items from DB
            }
        }
    }

    fun addToCart(product: Product) {
        viewModelScope.launch {
            val existing = cartItems.value.find { it.id == product.id }
            if (existing != null) {
                dao.updateItem(existing.copy(quantity = existing.quantity + 1))
            } else {
                dao.updateItem(product.toCartItem().copy(quantity = 1))
            }
        }
    }

    fun updateQuantity(item: CartItem, change: Int) {
        viewModelScope.launch {
            val newQty = item.quantity + change
            if (newQty > 0) dao.updateItem(item.copy(quantity = newQty))
            else dao.removeItem(item)
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            cartItems.value.forEach { dao.removeItem(it) }
        }
    }

    fun logout() {
        userName = ""
        userPhone = ""
        userAddress = "Add Your Address"
        activeOrdersCount = 0
    }
}