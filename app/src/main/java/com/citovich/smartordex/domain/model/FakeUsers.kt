package com.citovich.smartordex.domain.model

val fakeUsers = listOf(
    AppUser(
        id = "1",
        name = "Gianfranco",
        pin = "1234",
        role = UserRole.WAITER
    ),
    AppUser(
        id = "2",
        name = "Michele",
        pin = "1111",
        role = UserRole.WAITER
    ),
    AppUser(
        id = "3",
        name = "Admin",
        pin = "9999",
        role = UserRole.ADMIN
    )
)