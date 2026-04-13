package com.citovich.smartordex.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.citovich.smartordex.domain.model.AppUser
import com.citovich.smartordex.domain.model.CartItem
import com.citovich.smartordex.domain.model.CloseTableDraft
import com.citovich.smartordex.domain.model.ItemSendStatus
import com.citovich.smartordex.domain.model.OrderDraft
import com.citovich.smartordex.domain.model.PaymentType
import com.citovich.smartordex.domain.model.Product
import com.citovich.smartordex.domain.model.SentItem
import com.citovich.smartordex.domain.model.UserRole
import com.citovich.smartordex.ui.screens.cart.CartScreen
import com.citovich.smartordex.ui.screens.close.CloseTableScreen
import com.citovich.smartordex.ui.screens.device.DeviceScreen
import com.citovich.smartordex.ui.screens.home.HomeScreen
import com.citovich.smartordex.ui.screens.kitchen.KitchenMonitorScreen
import com.citovich.smartordex.ui.screens.login.LoginScreen
import com.citovich.smartordex.ui.screens.open.OpenTableScreen
import com.citovich.smartordex.ui.screens.order.NewOrderScreen

object Routes {
    const val DEVICE = "device"
    const val LOGIN = "login"
    const val HOME = "home"
    const val OPEN_TABLE = "open_table"
    const val NEW_ORDER = "new_order"
    const val CART = "cart"
    const val CLOSE_TABLE = "close_table"
    const val KITCHEN_MONITOR = "kitchen_monitor"
}

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    var loggedUser by remember { mutableStateOf<AppUser?>(null) }

    val cartItems = remember { mutableStateListOf<CartItem>() }
    val sentItems = remember { mutableStateListOf<SentItem>() }

    var tableNumber by remember { mutableStateOf("") }
    var coversCount by remember { mutableStateOf(0) }
    var coverPrice by remember { mutableStateOf(2.5) }
    var paymentType by remember { mutableStateOf(PaymentType.CASH) }
    var sendRoundCounter by remember { mutableStateOf(1) }
    var currentCourse by remember { mutableStateOf(1) }

    NavHost(
        navController = navController,
        startDestination = Routes.DEVICE
    ) {
        composable(Routes.DEVICE) {
            DeviceScreen(
                onStartClick = {
                    navController.navigate(Routes.LOGIN)
                }
            )
        }

        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = { user ->
                    loggedUser = user
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.HOME) {
            val user = loggedUser

            if (user != null) {
                HomeScreen(
                    loggedUserName = user.name,
                    isAdmin = user.role == UserRole.ADMIN,
                    onNewOrderClick = {
                        navController.navigate(Routes.OPEN_TABLE)
                    },
                    onHistoryClick = { },
                    onAdminClick = { },
                    onKitchenMonitorClick = {
                        navController.navigate(Routes.KITCHEN_MONITOR)
                    },
                    onLogoutClick = {
                        loggedUser = null
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(Routes.HOME) { inclusive = true }
                        }
                    }
                )
            }
        }

        composable(Routes.OPEN_TABLE) {
            OpenTableScreen(
                tableNumber = tableNumber,
                coversCount = coversCount,
                coverPrice = coverPrice,
                onTableNumberChange = { tableNumber = it },
                onCoversCountChange = { coversCount = it },
                onCoverPriceChange = { coverPrice = it },
                onOpenTableClick = {
                    cartItems.clear()
                    sendRoundCounter = 1
                    currentCourse = 1
                    navController.navigate(Routes.NEW_ORDER)
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.NEW_ORDER) {
            NewOrderScreen(
                cartItems = cartItems,
                currentCourse = currentCourse,
                onDecreaseCourse = {
                    if (currentCourse > 1) currentCourse--
                },
                onIncreaseCourse = {
                    currentCourse++
                },
                onAddProduct = { product, courseNumber, quantity ->
    addToCart(cartItems, product, courseNumber, quantity)
},
                onBackClick = {
                    navController.popBackStack()
                },
                onGoToCartClick = {
                    navController.navigate(Routes.CART)
                }
            )
        }

        composable(Routes.CART) {
            CartScreen(
                orderDraft = OrderDraft(
                    tableNumber = tableNumber,
                    coversCount = coversCount,
                    coverPrice = coverPrice,
                    items = cartItems
                ),
                onIncreaseItem = { item ->
                    updateItemQuantity(cartItems, item, item.quantity + 1)
                },
                onDecreaseItem = { item ->
                    if (item.quantity > 1) {
                        updateItemQuantity(cartItems, item, item.quantity - 1)
                    } else {
                        removeItem(cartItems, item)
                    }
                },
                onRemoveItem = { item ->
                    removeItem(cartItems, item)
                },
                onSendCourseClick = { courseNumber ->
                    if (tableNumber.isBlank()) return@CartScreen

                    val courseItemsToSend = cartItems.filter {
                        it.courseNumber == courseNumber &&
                            it.sendStatus == ItemSendStatus.NOT_SENT
                    }

                    courseItemsToSend.forEach { item ->
                        sentItems.add(
                            SentItem(
                                tableNumber = tableNumber,
                                productName = item.product.name,
                                quantity = item.quantity,
                                department = item.product.department,
                                courseNumber = item.courseNumber,
                                sendRound = sendRoundCounter,
                                status = ItemSendStatus.SENT
                            )
                        )
                    }

                    markCourseAsSent(
                        cartItems = cartItems,
                        courseNumber = courseNumber,
                        sendRound = sendRoundCounter
                    )

                    sendRoundCounter++

                    if (currentCourse <= courseNumber) {
                        currentCourse = courseNumber + 1
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                },
                onCloseTableClick = {
                    navController.navigate(Routes.CLOSE_TABLE)
                }
            )
        }

        composable(Routes.CLOSE_TABLE) {
            CloseTableScreen(
                closeTableDraft = CloseTableDraft(
                    tableNumber = tableNumber,
                    coversCount = coversCount,
                    coverPrice = coverPrice,
                    items = cartItems,
                    paymentType = paymentType
                ),
                onPaymentTypeSelected = { paymentType = it },
                onBackClick = {
                    navController.popBackStack()
                },
                onConfirmCloseTableClick = {
                    cartItems.clear()
                    tableNumber = ""
                    coversCount = 0
                    coverPrice = 2.5
                    paymentType = PaymentType.CASH
                    sendRoundCounter = 1
                    currentCourse = 1

                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.KITCHEN_MONITOR) {
            KitchenMonitorScreen(
                sentItems = sentItems
            )
        }
    }
}

private fun addToCart(
    cartItems: MutableList<CartItem>,
    product: Product,
    courseNumber: Int,
    quantityToAdd: Int
) {
    val index = cartItems.indexOfFirst {
        it.product.id == product.id &&
            it.courseNumber == courseNumber &&
            it.sendStatus == ItemSendStatus.NOT_SENT
    }

    if (index >= 0) {
        val current = cartItems[index]
        cartItems[index] = current.copy(quantity = current.quantity + quantityToAdd)
    } else {
        cartItems.add(
            CartItem(
                product = product,
                quantity = quantityToAdd,
                courseNumber = courseNumber
            )
        )
    }
}

private fun updateItemQuantity(
    cartItems: MutableList<CartItem>,
    itemToUpdate: CartItem,
    newQuantity: Int
) {
    val index = cartItems.indexOfFirst {
        it.product.id == itemToUpdate.product.id &&
            it.courseNumber == itemToUpdate.courseNumber &&
            it.sendStatus == itemToUpdate.sendStatus &&
            it.sendRound == itemToUpdate.sendRound
    }

    if (index >= 0) {
        val current = cartItems[index]
        if (current.sendStatus == ItemSendStatus.NOT_SENT) {
            cartItems[index] = current.copy(quantity = newQuantity)
        }
    }
}

private fun removeItem(
    cartItems: MutableList<CartItem>,
    itemToRemove: CartItem
) {
    val index = cartItems.indexOfFirst {
        it.product.id == itemToRemove.product.id &&
            it.courseNumber == itemToRemove.courseNumber &&
            it.sendStatus == itemToRemove.sendStatus &&
            it.sendRound == itemToRemove.sendRound
    }

    if (index >= 0 && cartItems[index].sendStatus == ItemSendStatus.NOT_SENT) {
        cartItems.removeAt(index)
    }
}

private fun markCourseAsSent(
    cartItems: MutableList<CartItem>,
    courseNumber: Int,
    sendRound: Int
) {
    cartItems.indices.forEach { index ->
        val item = cartItems[index]
        if (item.courseNumber == courseNumber && item.sendStatus == ItemSendStatus.NOT_SENT) {
            cartItems[index] = item.copy(
                sendStatus = ItemSendStatus.SENT,
                sendRound = sendRound
            )
        }
    }
}