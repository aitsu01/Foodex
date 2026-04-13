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
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
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
    currentCourse: Int,
    onDecreaseCourse: () -> Unit,
    onIncreaseCourse: () -> Unit,
    onAddProduct: (Product, Int, Int) -> Unit,
    onBackClick: () -> Unit,
    onGoToCartClick: () -> Unit
) {
    val categories = Category.entries
    val selectedCategoryState = remember { androidx.compose.runtime.mutableStateOf(Category.DRINK) }
    val selectedCategory = selectedCategoryState.value

    val productCourseMap = remember { mutableStateMapOf<String, Int>() }
    val productQuantityMap = remember { mutableStateMapOf<String, Int>() }

    val filteredProducts = fakeProducts.filter { it.category == selectedCategory }

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
            GlobalCourseHeader(
                currentCourse = currentCourse,
                onDecreaseCourse = onDecreaseCourse,
                onIncreaseCourse = onIncreaseCourse
            )

            ScrollableTabRow(
                selectedTabIndex = categories.indexOf(selectedCategory)
            ) {
                categories.forEach { category ->
                    Tab(
                        selected = selectedCategory == category,
                        onClick = { selectedCategoryState.value = category },
                        text = { Text(category.toReadableText()) }
                    )
                }
            }

            Text(
                text = "Categoria: ${selectedCategory.toReadableText()}",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 12.dp)
            ) {
                items(filteredProducts, key = { it.id }) { product ->
                    val selectedCourse = productCourseMap[product.id] ?: currentCourse
                    val selectedQuantity = productQuantityMap[product.id] ?: 1

                    ProductCard(
                        product = product,
                        selectedCourse = selectedCourse,
                        selectedQuantity = selectedQuantity,
                        onDecreaseCourse = {
                            val newValue = (selectedCourse - 1).coerceAtLeast(1)
                            productCourseMap[product.id] = newValue
                        },
                        onIncreaseCourse = {
                            productCourseMap[product.id] = selectedCourse + 1
                        },
                        onDecreaseQuantity = {
                            val newValue = (selectedQuantity - 1).coerceAtLeast(1)
                            productQuantityMap[product.id] = newValue
                        },
                        onIncreaseQuantity = {
                            productQuantityMap[product.id] = selectedQuantity + 1
                        },
                        onAddClick = {
                            onAddProduct(product, selectedCourse, selectedQuantity)
                            productQuantityMap[product.id] = 1
                        }
                    )
                }
            }

            CartSummary(
                totalItemsCount = cartItems.sumOf { it.quantity },
                total = cartItems.sumOf { it.product.price * it.quantity },
                onBackClick = onBackClick,
                onGoToCartClick = onGoToCartClick
            )
        }
    }
}

@Composable
private fun GlobalCourseHeader(
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
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Portata predefinita",
                style = MaterialTheme.typography.titleMedium
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
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
                    modifier = Modifier.weight(1.4f)
                ) {
                    Text("Portata $currentCourse")
                }

                OutlinedButton(
                    onClick = onIncreaseCourse,
                    modifier = Modifier.weight(1.4f)
                ) {
                    Text("Avvia ${currentCourse + 1}")
                }
            }
        }
    }
}

@Composable
private fun ProductCard(
    product: Product,
    selectedCourse: Int,
    selectedQuantity: Int,
    onDecreaseCourse: () -> Unit,
    onIncreaseCourse: () -> Unit,
    onDecreaseQuantity: () -> Unit,
    onIncreaseQuantity: () -> Unit,
    onAddClick: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = product.name,
                style = MaterialTheme.typography.titleMedium
            )

            Text("€ %.2f".format(product.price))
            Text("Reparto: ${product.department.toReadableText()}")

            Text(
                text = "Quantità",
                style = MaterialTheme.typography.bodyMedium
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onDecreaseQuantity,
                    modifier = Modifier.weight(1f),
                    enabled = selectedQuantity > 1
                ) {
                    Text("-")
                }

                Button(
                    onClick = {},
                    modifier = Modifier.weight(1.2f)
                ) {
                    Text(selectedQuantity.toString())
                }

                OutlinedButton(
                    onClick = onIncreaseQuantity,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("+")
                }
            }

            Text(
                text = "Portata",
                style = MaterialTheme.typography.bodyMedium
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onDecreaseCourse,
                    modifier = Modifier.weight(1f),
                    enabled = selectedCourse > 1
                ) {
                    Text("-")
                }

                Button(
                    onClick = {},
                    modifier = Modifier.weight(1.3f)
                ) {
                    Text("Portata $selectedCourse")
                }

                OutlinedButton(
                    onClick = onIncreaseCourse,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("+")
                }
            }

            Button(
                onClick = onAddClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Aggiungi")
            }
        }
    }
}

@Composable
private fun CartSummary(
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
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "Riepilogo ordine",
                style = MaterialTheme.typography.titleMedium
            )

            Text("Articoli totali: $totalItemsCount")
            Text("Totale: € %.2f".format(total))

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
                    onClick = onGoToCartClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Gestione Tavolo")
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