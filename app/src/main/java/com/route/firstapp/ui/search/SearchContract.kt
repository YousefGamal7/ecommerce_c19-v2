package com.route.firstapp.ui.search

import com.route.domain.data.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface SearchContract {

    abstract class ViewModel : androidx.lifecycle.ViewModel() {
        abstract val states: StateFlow<State>
        abstract val events: Flow<Event>
        abstract fun doIntent(intent: Intent)
    }

    sealed class State {
        data object Loading : State()
        data class Success(
            val products: List<Product>,
            val query: String = "",
        ) : State()
        data class Empty(
            val query: String,
        ) : State()
        data class Error(
            val message: String,
        ) : State()
    }

    sealed class Intent {
        data class Init(val initialQuery: String = "") : Intent()
        data class QueryChanged(val query: String) : Intent()
        data class ProductClicked(val productId: String) : Intent()
        data object ClearQuery : Intent()
    }

    sealed class Event {
        data class NavigateToProductDetails(val productId: String) : Event()
    }
}
