package com.citovich.smartordex.ui.screens.kitchen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.citovich.smartordex.domain.model.Department
import com.citovich.smartordex.domain.model.SentItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KitchenMonitorScreen(
    sentItems: List<SentItem>
) {
    val departments = Department.entries
    var selectedDepartment by remember { mutableStateOf(Department.ANTIPASTI_INSALATE) }

    val filteredItems = sentItems.filter { it.department == selectedDepartment }

    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        topBar = {
            TopAppBar(
                title = { Text("Monitor Reparti") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            ScrollableTabRow(
                selectedTabIndex = departments.indexOf(selectedDepartment)
            ) {
                departments.forEach { department ->
                    Tab(
                        selected = selectedDepartment == department,
                        onClick = { selectedDepartment = department },
                        text = { Text(department.toReadableText()) }
                    )
                }
            }

            if (filteredItems.isEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Nessuna comanda per questo reparto",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(filteredItems) { item ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "Tavolo ${item.tableNumber}",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text("${item.quantity} x ${item.productName}")
                                Text("Invio n° ${item.sendRound}")
                                Text("Stato: ${item.status.toReadableText()}")
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun Department.toReadableText(): String {
    return when (this) {
        Department.ANTIPASTI_INSALATE -> "Antipasti / Insalate"
        Department.PRIMI_SECONDI -> "Primi / Secondi"
        Department.DESSERT -> "Dessert"
        Department.BAR -> "Bar"
    }
}

private fun com.citovich.smartordex.domain.model.ItemSendStatus.toReadableText(): String {
    return when (this) {
        com.citovich.smartordex.domain.model.ItemSendStatus.NOT_SENT -> "Non inviato"
        com.citovich.smartordex.domain.model.ItemSendStatus.SENT -> "Inviato"
        com.citovich.smartordex.domain.model.ItemSendStatus.PREPARING -> "In preparazione"
        com.citovich.smartordex.domain.model.ItemSendStatus.READY -> "Pronto"
        com.citovich.smartordex.domain.model.ItemSendStatus.SERVED -> "Servito"
    }
}