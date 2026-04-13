package com.citovich.smartordex.ui.screens.order

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
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.citovich.smartordex.domain.model.CartItem
import com.citovich.smartordex.domain.model.Category
import com.citovich.smartordex.domain.model.Product
import com.citovich.smartordex.domain.model.fakeProducts

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewOrderScreen(
    cartItems: List<CartItem>,
    onAddProduct: (Product, Int) -> Unit,
    onBackClick: () -> Unit,
    onGoToCartClick: () -> Unit
) {
    val categories = Category.entries
    var selectedCategory by remember { mutableStateOf(Category.FOOD) }
    var currentCourse by remember { mutableIntStateOf(1) }

    val filteredProducts = fakeProducts.filter { it.category == selectedCategory }
    val currentCourseItems = cartItems.filter { it.courseNumber == currentCourse }

    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        topBar = {
            TopAppBar(
                title = { Text("Nuovo Ordine") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            CourseSelector(
                currentCourse = currentCourse,
                onDecreaseCourse = {
                    if (currentCourse > 1) currentCourse--
                },
                onIncreaseCourse = {
                    currentCourse++
                }
            )

            ScrollableTabRow(
                selectedTabIndex = categories.indexOf(selectedCategory)
            ) {
                categories.forEach { category ->
                    Tab(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = category },
                        text = { Text(category.toReadableText()) }
                    )
                }
            }

            Text(
                text = "Prodotti - Portata $currentCourse",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(filteredProducts) { product ->
                    ProductCard(
                        product = product,
                        currentCourse = currentCourse,
                        onAddClick = {
                            onAddProduct(product, currentCourse)
                        }
                    )
                }
            }

            CartSummary(
                currentCourse = currentCourse,
                currentCourseItemsCount = currentCourseItems.sumOf { it.quantity },
                totalItemsCount = cartItems.sumOf { it.quantity },
                total = cartItems.sumOf { it.product.price * it.quantity },
                onBackClick = onBackClick,
                onGoToCartClick = onGoToCartClick
            )
        }
    }
}

@Composable
private fun CourseSelector(
    currentCourse: Int,
    onDecreaseCourse: () -> Unit,
    onIncreaseCourse: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Portata corrente",
                style = MaterialTheme.typography.titleMedium
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onDecreaseCourse,
                    modifier = Modifier.weight(1f),
                    enabled = currentCourse > 1
                ) {
                    Text("-")
                }

                Button(
                    onClick = {},
                    modifier = Modifier.weight(2f)
                ) {
                    Text("Portata $currentCourse")
                }

                OutlinedButton(
                    onClick = onIncreaseCourse,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("+")
                }
            }
        }
    }
}

@Composable
private fun ProductCard(
    product: Product,
    currentCourse: Int,
    onAddClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = product.name,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "€ %.2f".format(product.price),
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = "Reparto: ${product.department.toReadableText()}",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Portata: $currentCourse",
                style = MaterialTheme.typography.bodyMedium
            )

            Button(
                onClick = onAddClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Aggiungi a Portata $currentCourse")
            }
        }
    }
}

@Composable
private fun CartSummary(
    currentCourse: Int,
    currentCourseItemsCount: Int,
    totalItemsCount: Int,
    total: Double,
    onBackClick: () -> Unit,
    onGoToCartClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Riepilogo ordine",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "Portata corrente: $currentCourse",
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = "Articoli in Portata $currentCourse: $currentCourseItemsCount",
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = "Totale articoli ordine: $totalItemsCount",
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = "Totale parziale: € %.2f".format(total),
                style = MaterialTheme.typography.bodyLarge
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onBackClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Indietro")
                }

                Button(
                    onClick = onGoToCartClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Vai a Gestione Tavolo")
                }
            }
        }
    }
}

private fun Category.toReadableText(): String {
    return when (this) {
        Category.FOOD -> "Food"
        Category.DRINK -> "Drink"
        Category.COCKTAIL -> "Cocktail"
        Category.WINE -> "Vini"
        Category.SPIRITS -> "Distillati"
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