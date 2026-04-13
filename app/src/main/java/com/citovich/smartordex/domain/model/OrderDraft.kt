package com.citovich.smartordex.domain.model

data class OrderDraft(
    val tableNumber: String = "",
    val coversCount: Int = 0,
    val coverPrice: Double = 0.0,
    val items: List<CartItem> = emptyList()
) {
    val itemsTotal: Double
        get() = items.sumOf { it.product.price * it.quantity }

    val coversTotal: Double
        get() = coversCount * coverPrice

    val total: Double
        get() = itemsTotal + coversTotal
}