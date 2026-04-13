package com.citovich.smartordex.ui.screens.tablemap

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.citovich.smartordex.domain.model.TableSession
import com.citovich.smartordex.domain.model.TableStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TableMapScreen(
    loggedUserName: String,
    tableSessions: List<TableSession>,
    onTableClick: (TableSession) -> Unit,
    onBackClick: () -> Unit
) {
    val myTables = tableSessions.filter { it.waiterName == loggedUserName }

    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        topBar = {
            TopAppBar(
                title = { Text("Mappa Tavoli") }
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
                text = "Tavoli di $loggedUserName",
                style = MaterialTheme.typography.titleLarge
            )

            if (myTables.isEmpty()) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Nessun tavolo assegnato",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(myTables, key = { it.tableNumber }) { table ->
                        TableCard(
                            table = table,
                            onClick = { onTableClick(table) }
                        )
                    }
                }
            }

            OutlinedButton(
                onClick = onBackClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Indietro")
            }
        }
    }
}

@Composable
private fun TableCard(
    table: TableSession,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = table.status.toTableColor()
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "Tavolo ${table.tableNumber}",
                style = MaterialTheme.typography.titleLarge
            )

            Text("Cameriere: ${table.waiterName}")
            Text("Coperti: ${table.coversCount}")
            Text("Totale: € %.2f".format(table.total))
            Text("Stato: ${table.status.toReadableText()}")
        }
    }
}

@Composable
private fun TableStatus.toTableColor() = when (this) {
    TableStatus.OPEN -> MaterialTheme.colorScheme.tertiaryContainer
    TableStatus.CLOSED_NOT_PAID -> MaterialTheme.colorScheme.errorContainer
    TableStatus.PAID -> MaterialTheme.colorScheme.primaryContainer
}

private fun TableStatus.toReadableText(): String {
    return when (this) {
        TableStatus.OPEN -> "Aperto"
        TableStatus.CLOSED_NOT_PAID -> "Chiuso non pagato"
        TableStatus.PAID -> "Pagato"
    }
}