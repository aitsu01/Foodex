# Progetto Android Kotlin — App Gestione Ordini

## Obiettivo

Realizzare una app Android in Kotlin per:

* gestione ordini
* categorie: Food, Drink, Cocktail, Vini, Distillati
* coperti con prezzo
* pagamento: cash, card, coupon, buono pasto
* pannello admin per prodotti e prezzi
* invio comande ai reparti
* invio parziale delle righe ordine
* monitor reparto: Antipasti/Insalate, Primi/Secondi, Dessert, Bar

## Stack

* Kotlin
* Jetpack Compose
* Navigation Compose
* ViewModel
* Room
* Firebase Realtime Database

## Moduli principali

1. Sala
2. Admin
3. Monitor Reparto

## Struttura progetto

```text
app/
 ├─ data/
 │   ├─ local/
 │   │   ├─ entity/
 │   │   ├─ dao/
 │   │   └─ AppDatabase.kt
 │   ├─ remote/
 │   │   └─ FirebaseDataSource.kt
 │   └─ repository/
 ├─ domain/
 │   ├─ model/
 │   └─ usecase/
 ├─ ui/
 │   ├─ navigation/
 │   ├─ screens/
 │   │   ├─ home/
 │   │   ├─ order/
 │   │   ├─ admin/
 │   │   └─ kitchen/
 │   ├─ components/
 │   └─ theme/
 └─ MainActivity.kt
```

## Enum principali

```kotlin
enum class Category {
    FOOD,
    DRINK,
    COCKTAIL,
    WINE,
    SPIRITS
}

enum class Department {
    ANTIPASTI_INSALATE,
    PRIMI_SECONDI,
    DESSERT,
    BAR
}

enum class PaymentType {
    CASH,
    CARD,
    COUPON,
    BUONO_PASTO
}

enum class OrderStatus {
    OPEN,
    PARTIALLY_SENT,
    IN_PROGRESS,
    READY,
    CLOSED
}

enum class ItemSendStatus {
    NOT_SENT,
    SENT,
    PREPARING,
    READY,
    SERVED
}
```

## Model principali

```kotlin
data class Product(
    val id: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val category: Category = Category.FOOD,
    val department: Department = Department.ANTIPASTI_INSALATE,
    val description: String = "",
    val available: Boolean = true
)

data class OrderItem(
    val id: String = "",
    val productId: String = "",
    val productName: String = "",
    val quantity: Int = 1,
    val unitPrice: Double = 0.0,
    val department: Department = Department.BAR,
    val note: String = "",
    val sendStatus: ItemSendStatus = ItemSendStatus.NOT_SENT,
    val sendRound: Int = 0
)

data class Order(
    val id: String = "",
    val tableNumber: String = "",
    val coversCount: Int = 0,
    val coverPrice: Double = 0.0,
    val items: List<OrderItem> = emptyList(),
    val paymentType: PaymentType = PaymentType.CASH,
    val status: OrderStatus = OrderStatus.OPEN,
    val createdAt: Long = System.currentTimeMillis()
) {
    val itemsTotal: Double
        get() = items.sumOf { it.unitPrice * it.quantity }

    val coversTotal: Double
        get() = coversCount * coverPrice

    val total: Double
        get() = itemsTotal + coversTotal
}
```

## Regole reparto

* DRINK → BAR
* COCKTAIL → BAR
* WINE → BAR
* SPIRITS → BAR
* FOOD → scelto dall'admin: Antipasti/Insalate, Primi/Secondi, Dessert

## Schermate iniziali

### Home

* Nuovo Ordine
* Storico Ordini
* Admin Prodotti
* Monitor Reparto

### Nuovo Ordine

* numero tavolo
* numero coperti
* prezzo coperto
* tabs categorie
* lista prodotti
* carrello in basso o schermata separata

### Carrello

* righe ordine
* quantità + / -
* note
* selezione righe da inviare
* pulsanti:

  * Invia selezionati
  * Invia tutto
  * Invia solo Bar
  * Conferma pagamento

### Admin

* aggiungi prodotto
* modifica prezzo
* categoria
* reparto
* disponibilità

### Monitor Reparto

* filtro reparto
* lista comande ricevute
* stato: SENT → PREPARING → READY

## Flusso ordine

1. Creo ordine
2. Inserisco tavolo e coperti
3. Aggiungo prodotti
4. Scelgo pagamento
5. Invio alcune righe o tutto
6. I reparti ricevono solo le righe di competenza
7. Lo stato si aggiorna in tempo reale

## Primo obiettivo sviluppo

### Fase 1

* progetto Android Studio
* navigation base
* enum e model
* schermata Home
* schermata Nuovo Ordine
* schermata Carrello

### Fase 2

* Room
* Admin prodotti
* storico ordini

### Fase 3

* Firebase Realtime Database
* monitor reparto
* invio comande live

## Prossimo file da creare

1. MainActivity.kt
2. AppNavGraph.kt
3. HomeScreen.kt
4. NewOrderScreen.kt
5. CartScreen.kt
6. model enums e data class

