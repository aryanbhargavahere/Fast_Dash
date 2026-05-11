package com.example.minigrocerydeliveryapp.datamodel

data class Order(
    val id: String,
    val date: String,
    val amount: Double,
    val status: String
)