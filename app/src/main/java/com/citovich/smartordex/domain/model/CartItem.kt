package com.citovich.smartordex.domain.model



data class CartItem(
    val product: Product,
    val quantity: Int = 1,
    val courseNumber: Int = 1,
    val selectedForSend: Boolean = false,
    val sendStatus: ItemSendStatus = ItemSendStatus.NOT_SENT,
    val sendRound: Int = 0
)
