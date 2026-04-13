package com.citovich.smartordex.domain.model

data class SentItem(
    val tableNumber: String,
    val productName: String,
    val quantity: Int,
    val department: Department,
    val sendRound: Int,
    val status: ItemSendStatus = ItemSendStatus.SENT
)