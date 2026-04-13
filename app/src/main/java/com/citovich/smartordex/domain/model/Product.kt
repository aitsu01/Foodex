package com.citovich.smartordex.domain.model

data class Product(
    val id: String,
    val name: String,
    val price: Double,
    val category: Category,
    val department: Department,
    val description: String = "",
    val available: Boolean = true
)