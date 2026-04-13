package com.citovich.smartordex.ui.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.citovich.smartordex.domain.model.AppUser
import com.citovich.smartordex.domain.model.fakeUsers

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: (AppUser) -> Unit
) {
    var selectedUser by remember { mutableStateOf<AppUser?>(null) }
    var pin by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        topBar = {
            TopAppBar(
                title = { Text("Login Operatore") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Seleziona utente",
                style = MaterialTheme.typography.headlineSmall
            )

            UserGrid(
                users = fakeUsers,
                selectedUser = selectedUser,
                onUserClick = {
                    selectedUser = it
                    pin = ""
                    errorMessage = ""
                }
            )

            Text(
                text = "Inserisci PIN",
                style = MaterialTheme.typography.titleLarge
            )

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (pin.isEmpty()) "----" else pin.map { "*" }.joinToString(" "),
                    modifier = Modifier.padding(20.dp),
                    style = MaterialTheme.typography.headlineMedium
                )
            }

            if (selectedUser != null) {
                Text(
                    text = "Utente selezionato: ${selectedUser!!.name}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error
                )
            }

            PinPad(
                onNumberClick = { digit ->
                    if (selectedUser == null) {
                        errorMessage = "Seleziona prima un utente"
                        return@PinPad
                    }

                    if (pin.length < 4) {
                        pin += digit
                        errorMessage = ""
                    }

                    if (pin.length == 4) {
                        if (selectedUser!!.pin == pin) {
                            onLoginSuccess(selectedUser!!)
                        } else {
                            errorMessage = "PIN non valido"
                            pin = ""
                        }
                    }
                },
                onClearClick = {
                    pin = ""
                    errorMessage = ""
                },
                onDeleteClick = {
                    if (pin.isNotEmpty()) {
                        pin = pin.dropLast(1)
                    }
                }
            )
        }
    }
}

@Composable
private fun UserGrid(
    users: List<AppUser>,
    selectedUser: AppUser?,
    onUserClick: (AppUser) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(users) { user ->
            val isSelected = selectedUser?.id == user.id

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onUserClick(user) },
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant
                    }
                )
            ) {
                Text(
                    text = user.name,
                    modifier = Modifier.padding(20.dp),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
private fun PinPad(
    onNumberClick: (String) -> Unit,
    onClearClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val keys = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "C", "0", "<")

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(top = 8.dp)
    ) {
        items(keys) { key ->
            Button(
                onClick = {
                    when (key) {
                        "C" -> onClearClick()
                        "<" -> onDeleteClick()
                        else -> onNumberClick(key)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 20.dp)
            ) {
                Text(
                    text = key,
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }
    }
}