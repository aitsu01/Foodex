package com.citovich.smartordex.ui.screens.close

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.clickable
import androidx.compose.ui.Alignment
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.citovich.smartordex.domain.model.CloseTableDraft
import com.citovich.smartordex.domain.model.PaymentType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CloseTableScreen(
    closeTableDraft: CloseTableDraft,
    onPaymentTypeSelected: (PaymentType) -> Unit,
    onBackClick: () -> Unit,
    onConfirmCloseTableClick: () -> Unit
) {
    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        topBar = {
            TopAppBar(
                title = { Text("Chiudi Tavolo") }
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
            SummarySection(closeTableDraft)
            PaymentSection(
                selectedPaymentType = closeTableDraft.paymentType,
                onPaymentTypeSelected = onPaymentTypeSelected
            )

            Button(
                onClick = onBackClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Torna alla gestione tavolo")
            }

            Button(
                onClick = onConfirmCloseTableClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Conferma chiusura tavolo")
            }
        }
    }
}

@Composable
private fun SummarySection(draft: CloseTableDraft) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Riepilogo tavolo",
                style = MaterialTheme.typography.titleMedium
            )

            Text("Tavolo: ${draft.tableNumber}")
            Text("Coperti: ${draft.coversCount}")
            Text("Prezzo coperto: € %.2f".format(draft.coverPrice))
            Text("Totale prodotti: € %.2f".format(draft.itemsTotal))
            Text("Totale coperti: € %.2f".format(draft.coversTotal))
            Text(
                text = "Totale finale: € %.2f".format(draft.total),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Prodotti ordinati",
                style = MaterialTheme.typography.titleMedium
            )

            LazyColumn {
                items(draft.items, key = { it.product.id }) { item ->
                    Text(
                        text = "${item.quantity} x ${item.product.name} - € %.2f"
                            .format(item.product.price * item.quantity)
                    )
                }
            }
        }
    }
}

@Composable
private fun PaymentSection(
    selectedPaymentType: PaymentType,
    onPaymentTypeSelected: (PaymentType) -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Metodo di pagamento",
                style = MaterialTheme.typography.titleMedium
            )

            PaymentType.entries.forEach { paymentType ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onPaymentTypeSelected(paymentType) }
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedPaymentType == paymentType,
                        onClick = { onPaymentTypeSelected(paymentType) }
                    )

                    Text(
                        text = paymentType.toReadableText(),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}

private fun PaymentType.toReadableText(): String {
    return when (this) {
        PaymentType.CASH -> "Contanti"
        PaymentType.CARD -> "Carta"
        PaymentType.COUPON -> "Coupon"
        PaymentType.BUONO_PASTO -> "Buono Pasto"
    }
}