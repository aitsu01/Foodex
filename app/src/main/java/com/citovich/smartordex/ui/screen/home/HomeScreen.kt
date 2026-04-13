package com.citovich.smartordex.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    loggedUserName: String,
    isAdmin: Boolean,
    onNewOrderClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onAdminClick: () -> Unit,
    onKitchenMonitorClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "SmartOrdex")
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Gestione ordini ristorante",
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = "Utente: $loggedUserName",
                style = MaterialTheme.typography.titleMedium
            )

            MainMenuButton(
                text = "Nuovo Ordine",
                onClick = onNewOrderClick
            )

            MainMenuButton(
                text = "Storico Ordini",
                onClick = onHistoryClick
            )

            MainMenuButton(
                text = "Monitor Cucina / Bar",
                onClick = onKitchenMonitorClick
            )

            if (isAdmin) {
                MainMenuButton(
                    text = "Admin Prodotti",
                    onClick = onAdminClick
                )
            }

            OutlinedButton(
                onClick = onLogoutClick,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                Text("Logout")
            }
        }
    }
}

@Composable
private fun MainMenuButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        Text(text = text)
    }
}