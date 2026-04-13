package com.citovich.smartordex.ui.screens.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.citovich.smartordex.domain.model.CartItem
import com.citovich.smartordex.domain.model.ItemSendStatus
import com.citovich.smartordex.domain.model.OrderDraft
import androidx.compose.foundation.clickable
import androidx.compose.ui.Alignment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    orderDraft: OrderDraft,
    
    onIncreaseItem: (CartItem) -> Unit,
    onDecreaseItem: (CartItem) -> Unit,
    onRemoveItem: (CartItem) -> Unit,
    onToggleItemSelection: (CartItem) -> Unit,
    onBackClick: () -> Unit,
    onSendOrderClick: () -> Unit,
    onCloseTableClick: () -> Unit
) {
    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        topBar = {
            TopAppBar(
                title = { Text("Gestione Tavolo") }
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
                text = "Prodotti tavolo",
                style = MaterialTheme.typography.titleLarge
            )

            if (orderDraft.items.isEmpty()) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Nessun prodotto inserito",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 8.dp)
                ) {
                    items(orderDraft.items, key = { it.product.id }) { item ->
                        CartItemCard(
                            item = item,
                            onIncrease = { onIncreaseItem(item) },
                            onDecrease = { onDecreaseItem(item) },
                            onRemove = { onRemoveItem(item) },
                            onToggleSelection = { onToggleItemSelection(item) }
                        )
                    }
                }
            }

            TotalsSection(
                itemsTotal = orderDraft.itemsTotal,
                coversTotal = orderDraft.coversTotal,
                finalTotal = orderDraft.total
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onBackClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Indietro")
                }

                Button(
                    onClick = onSendOrderClick,
                    modifier = Modifier.weight(1f),
                    enabled = orderDraft.items.any { it.selectedForSend && it.sendStatus == ItemSendStatus.NOT_SENT }
                ) {
                    Text("Invia Selezionati")
                }
            }

            Button(
                onClick = onCloseTableClick,
                modifier = Modifier.fillMaxWidth(),
                enabled = orderDraft.items.isNotEmpty() && orderDraft.tableNumber.isNotBlank()
            ) {
                Text("Chiudi Tavolo")
            }
        }
    }
}



@Composable
private fun CartItemCard(
    item: CartItem,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onRemove: () -> Unit,
    onToggleSelection: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = item.product.name,
                style = MaterialTheme.typography.titleMedium
            )

            Text("Reparto: ${item.product.department.toReadableText()}")
            Text("Prezzo unitario: € %.2f".format(item.product.price))
            Text("Subtotale: € %.2f".format(item.product.price * item.quantity))
            Text("Stato: ${item.sendStatus.toReadableText()}")

            if (item.sendRound > 0) {
                Text("Invio n° ${item.sendRound}")
            }

            if (item.sendStatus == ItemSendStatus.NOT_SENT) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Checkbox(
            checked = item.selectedForSend,
            onCheckedChange = { onToggleSelection() }
        )
        Text(
            text = if (item.selectedForSend) "Selezionato per invio" else "Tocca il quadratino per selezionare"
        )
    }
}

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onDecrease,
                    modifier = Modifier.weight(1f),
                    enabled = item.sendStatus == ItemSendStatus.NOT_SENT
                ) {
                    Text("-")
                }

                Button(
                    onClick = {},
                    modifier = Modifier.weight(1f)
                ) {
                    Text(item.quantity.toString())
                }

                OutlinedButton(
                    onClick = onIncrease,
                    modifier = Modifier.weight(1f),
                    enabled = item.sendStatus == ItemSendStatus.NOT_SENT
                ) {
                    Text("+")
                }
            }

            OutlinedButton(
                onClick = onRemove,
                modifier = Modifier.fillMaxWidth(),
                enabled = item.sendStatus == ItemSendStatus.NOT_SENT
            ) {
                Text("Rimuovi")
            }
        }
    }
}

@Composable
private fun TotalsSection(
    itemsTotal: Double,
    coversTotal: Double,
    finalTotal: Double
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Totali tavolo",
                style = MaterialTheme.typography.titleMedium
            )

            Text("Totale prodotti: € %.2f".format(itemsTotal))
            Text("Totale coperti: € %.2f".format(coversTotal))
            Text(
                text = "Totale finale: € %.2f".format(finalTotal),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

private fun com.citovich.smartordex.domain.model.Department.toReadableText(): String {
    return when (this) {
        com.citovich.smartordex.domain.model.Department.ANTIPASTI_INSALATE -> "Antipasti / Insalate"
        com.citovich.smartordex.domain.model.Department.PRIMI_SECONDI -> "Primi / Secondi"
        com.citovich.smartordex.domain.model.Department.DESSERT -> "Dessert"
        com.citovich.smartordex.domain.model.Department.BAR -> "Bar"
    }
}

private fun ItemSendStatus.toReadableText(): String {
    return when (this) {
        ItemSendStatus.NOT_SENT -> "Non inviato"
        ItemSendStatus.SENT -> "Inviato"
        ItemSendStatus.PREPARING -> "In preparazione"
        ItemSendStatus.READY -> "Pronto"
        ItemSendStatus.SERVED -> "Servito"
    }
}