package com.route.firstapp.ui.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.route.firstapp.ui.AppRoutes

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel(),
    onNavigate: (AppRoutes) -> Unit = {},
) {
    LifecycleEventEffect(event = Lifecycle.Event.ON_CREATE) {
        viewModel.doIntent(SearchContract.Intent.Init())
    }

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is SearchContract.Event.NavigateToProductDetails -> {
                    onNavigate(AppRoutes.ProductDetails(productId = event.productId))
                }
            }
        }
    }

    val states = viewModel.states.collectAsStateWithLifecycle()

    when (val state = states.value) {
        SearchContract.State.Loading -> {
            SearchLoadingState(modifier = modifier)
        }
        is SearchContract.State.Error -> {
            SearchErrorState(
                modifier = modifier,
                message = state.message,
                onRetryClick = { viewModel.doIntent(SearchContract.Intent.Init()) },
            )
        }
        is SearchContract.State.Empty -> {
            SearchEmptyState(
                modifier = modifier,
                query = state.query,
                onQueryChange = { query -> viewModel.doIntent(SearchContract.Intent.QueryChanged(query)) },
                onClearQuery = { viewModel.doIntent(SearchContract.Intent.ClearQuery) },
            )
        }
        is SearchContract.State.Success -> {
            SearchSuccessState(
                modifier = modifier,
                products = state.products,
                query = state.query,
                onQueryChange = { query -> viewModel.doIntent(SearchContract.Intent.QueryChanged(query)) },
                onClearQuery = { viewModel.doIntent(SearchContract.Intent.ClearQuery) },
                onProductClick = { productId ->
                    viewModel.doIntent(SearchContract.Intent.ProductClicked(productId))
                },
            )
        }
    }
}
