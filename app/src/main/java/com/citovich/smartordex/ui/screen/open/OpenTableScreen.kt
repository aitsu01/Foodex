package com.citovich.smartordex.ui.screens.open

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OpenTableScreen(
    tableNumber: String,
    coversCount: Int,
    coverPrice: Double,
    onTableNumberChange: (String) -> Unit,
    onCoversCountChange: (Int) -> Unit,
    onCoverPriceChange: (Double) -> Unit,
    onOpenTableClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        topBar = {
            TopAppBar(
                title = { Text("Apri Tavolo") }
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
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = tableNumber,
                        onValueChange = onTableNumberChange,
                        label = { Text("Numero tavolo") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text("Coperti")

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                onCoversCountChange((coversCount - 1).coerceAtLeast(0))
                            },
                            modifier = Modifier.weight(1f),
                            enabled = coversCount > 0
                        ) {
                            Text("-")
                        }

                        Button(
                            onClick = {},
                            modifier = Modifier.weight(1.4f)
                        ) {
                            Text(coversCount.toString())
                        }

                        OutlinedButton(
                            onClick = {
                                onCoversCountChange(coversCount + 1)
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("+")
                        }
                    }

                    OutlinedTextField(
                        value = if (coverPrice == 0.0) "" else coverPrice.toString(),
                        onValueChange = {
                            onCoverPriceChange(it.replace(",", ".").toDoubleOrNull() ?: 0.0)
                        },
                        label = { Text("Prezzo coperto") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                    )
                }
            }

            Button(
                onClick = onOpenTableClick,
                modifier = Modifier.fillMaxWidth(),
                enabled = tableNumber.isNotBlank() && coversCount > 0
            ) {
                Text("Apri Tavolo")
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