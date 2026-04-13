package com.citovich.smartordex.domain.model

data class AppUser(
    val id: String,
    val name: String,
    val pin: String,
    val role: UserRole
)