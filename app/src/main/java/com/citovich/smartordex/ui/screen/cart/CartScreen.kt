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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.citovich.smartordex.domain.model.CartItem
import com.citovich.smartordex.domain.model.ItemSendStatus
import com.citovich.smartordex.domain.model.OrderDraft

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    orderDraft: OrderDraft,
    onIncreaseItem: (CartItem) -> Unit,
    onDecreaseItem: (CartItem) -> Unit,
    onRemoveItem: (CartItem) -> Unit,
    onSendCourseClick: (Int) -> Unit,
    onBackClick: () -> Unit,
    onCloseTableClick: () -> Unit
) {
    val groupedItems = orderDraft.items.groupBy { it.courseNumber }.toSortedMap()

    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        topBar = {
            TopAppBar(
                title = { Text("Gestione Tavolo ${orderDraft.tableNumber}") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            TableSummarySection(
                tableNumber = orderDraft.tableNumber,
                coversCount = orderDraft.coversCount,
                coverPrice = orderDraft.coverPrice,
                itemsTotal = orderDraft.itemsTotal,
                coversTotal = orderDraft.coversTotal,
                finalTotal = orderDraft.total
            )

            if (orderDraft.items.isEmpty()) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Nessun prodotto inserito",
                        modifier = Modifier.padding(12.dp)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 8.dp)
                ) {
                    groupedItems.forEach { (courseNumber, courseItems) ->
                        item(key = "course_$courseNumber") {
                            CourseSection(
                                courseNumber = courseNumber,
                                items = courseItems,
                                onIncreaseItem = onIncreaseItem,
                                onDecreaseItem = onDecreaseItem,
                                onRemoveItem = onRemoveItem,
                                onSendCourseClick = { onSendCourseClick(courseNumber) }
                            )
                        }
                    }
                }
            }

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
                    onClick = onCloseTableClick,
                    modifier = Modifier.weight(1f),
                    enabled = orderDraft.items.isNotEmpty()
                ) {
                    Text("Chiudi Tavolo")
                }
            }
        }
    }
}

@Composable
private fun TableSummarySection(
    tableNumber: String,
    coversCount: Int,
    coverPrice: Double,
    itemsTotal: Double,
    coversTotal: Double,
    finalTotal: Double
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "Riepilogo tavolo",
                style = MaterialTheme.typography.titleMedium
            )
            Text("Tavolo: $tableNumber")
            Text("Coperti: $coversCount")
            Text("Prezzo coperto: € %.2f".format(coverPrice))
            Text("Totale prodotti: € %.2f".format(itemsTotal))
            Text("Totale coperti: € %.2f".format(coversTotal))
            Text(
                text = "Totale finale: € %.2f".format(finalTotal),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
private fun CourseSection(
    courseNumber: Int,
    items: List<CartItem>,
    onIncreaseItem: (CartItem) -> Unit,
    onDecreaseItem: (CartItem) -> Unit,
    onRemoveItem: (CartItem) -> Unit,
    onSendCourseClick: () -> Unit
) {
    val hasPendingItems = items.any { it.sendStatus == ItemSendStatus.NOT_SENT }
    val allSent = items.all { it.sendStatus != ItemSendStatus.NOT_SENT }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Portata $courseNumber",
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = if (allSent) "Stato: già inoltrata" else "Stato: da inoltrare",
                style = MaterialTheme.typography.bodyMedium
            )

            items.forEach { item ->
                CourseItemRow(
                    item = item,
                    onIncrease = { onIncreaseItem(item) },
                    onDecrease = { onDecreaseItem(item) },
                    onRemove = { onRemoveItem(item) }
                )
            }

            Button(
                onClick = onSendCourseClick,
                modifier = Modifier.fillMaxWidth(),
                enabled = hasPendingItems
            ) {
                Text("Inoltra Portata $courseNumber")
            }
        }
    }
}

@Composable
private fun CourseItemRow(
    item: CartItem,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onRemove: () -> Unit
) {
    val editable = item.sendStatus == ItemSendStatus.NOT_SENT

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = item.product.name,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "${item.product.department.toReadableText()} • € %.2f"
                    .format(item.product.price)
            )

            Text("Qtà: ${item.quantity}")
            Text("Subtotale: € %.2f".format(item.product.price * item.quantity))
            Text("Stato: ${item.sendStatus.toReadableText()}")

            if (item.sendRound > 0) {
                Text("Invio n° ${item.sendRound}")
            }

            if (editable) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDecrease,
                        modifier = Modifier.weight(1f)
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
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("+")
                    }

                    OutlinedButton(
                        onClick = onRemove,
                        modifier = Modifier.weight(1.4f)
                    ) {
                        Text("Rimuovi")
                    }
                }
            }
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