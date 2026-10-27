package com.route.firstapp.ui.search

import androidx.lifecycle.viewModelScope
import com.route.domain.data.Product
import com.route.domain.data.Resource
import com.route.domain.usecase.SearchProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchProductsUseCase: SearchProductsUseCase,
) : SearchContract.ViewModel() {

    private val _events = Channel<SearchContract.Event>(Channel.BUFFERED)
    override val events: Flow<SearchContract.Event> = _events.receiveAsFlow()

    private val _states = MutableStateFlow<SearchContract.State>(SearchContract.State.Loading)
    override val states: StateFlow<SearchContract.State> = _states.asStateFlow()

    private var allProducts: List<Product> = emptyList()
    private var currentQuery: String = ""
    private var loadJob: Job? = null

    override fun doIntent(intent: SearchContract.Intent) {
        when (intent) {
            is SearchContract.Intent.Init -> {
                if (allProducts.isEmpty()) {
                    loadProducts(intent.initialQuery)
                } else if (intent.initialQuery.isNotBlank()) {
                    filterAndEmit(intent.initialQuery)
                }
            }
            is SearchContract.Intent.QueryChanged -> {
                currentQuery = intent.query
                if (allProducts.isEmpty()) {
                    loadProducts(intent.query)
                } else {
                    filterAndEmit(intent.query)
                }
            }
            SearchContract.Intent.ClearQuery -> {
                currentQuery = ""
                filterAndEmit("")
            }
            is SearchContract.Intent.ProductClicked -> {
                viewModelScope.launch {
                    _events.send(SearchContract.Event.NavigateToProductDetails(intent.productId))
                }
            }
        }
    }

    private fun loadProducts(query: String) {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            searchProductsUseCase.searchProducts(query = "")
                .collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            _states.emit(SearchContract.State.Loading)
                        }
                        is Resource.Success -> {
                            allProducts = resource.data ?: emptyList()
                            filterAndEmit(query)
                        }
                        is Resource.Error -> {
                            _states.emit(
                                SearchContract.State.Error(
                                    message = resource.throwable.message ?: "Failed to load products"
                                )
                            )
                        }
                    }
                }
        }
    }

    private fun filterAndEmit(query: String) {
        currentQuery = query
        val filtered = if (query.isBlank()) {
            allProducts
        } else {
            val cleanQuery = query.trim().lowercase()
            allProducts.filter { product ->
                product.name?.lowercase()?.contains(cleanQuery) == true
            }
        }

        val newState = if (filtered.isEmpty() && query.isNotBlank()) {
            SearchContract.State.Empty(query = query)
        } else {
            SearchContract.State.Success(
                products = filtered,
                query = query,
            )
        }
        viewModelScope.launch {
            _states.emit(newState)
        }
    }
}

