package com.citovich.smartordex.domain.model

data class TableSession(
    val tableNumber: String,
    val waiterName: String,
    val coversCount: Int,
    val coverPrice: Double,
    val items: List<CartItem> = emptyList(),
    val status: TableStatus = TableStatus.OPEN
) {
    val itemsTotal: Double
        get() = items.sumOf { it.product.price * it.quantity }

    val coversTotal: Double
        get() = coversCount * coverPrice

    val total: Double
        get() = itemsTotal + coversTotal
}