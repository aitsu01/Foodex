package com.citovich.smartordex.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.citovich.smartordex.domain.model.AppUser
import com.citovich.smartordex.domain.model.UserRole
import com.citovich.smartordex.ui.screens.device.DeviceScreen
import com.citovich.smartordex.ui.screens.home.HomeScreen
import com.citovich.smartordex.ui.screens.login.LoginScreen

object Routes {
    const val DEVICE = "device"
    const val LOGIN = "login"
    const val HOME = "home"
}

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    var loggedUser by remember { mutableStateOf<AppUser?>(null) }

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
                    onNewOrderClick = { },
                    onHistoryClick = { },
                    onAdminClick = { },
                    onKitchenMonitorClick = { },
                    onLogoutClick = {
                        loggedUser = null
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(Routes.HOME) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}