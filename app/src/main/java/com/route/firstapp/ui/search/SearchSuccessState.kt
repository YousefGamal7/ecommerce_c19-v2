package com.route.firstapp.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.route.domain.data.Product
import com.route.firstapp.R
import com.route.firstapp.ui.ProductCard
import com.route.firstapp.ui.theme.BrandColor
import com.route.firstapp.ui.theme.MyPreview

@Composable
fun SearchSuccessState(
    modifier: Modifier = Modifier,
    products: List<Product>,
    query: String = "",
    onQueryChange: (String) -> Unit = {},
    onClearQuery: () -> Unit = {},
    onProductClick: (String) -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        SearchInputField(
            query = query,
            onQueryChange = onQueryChange,
            onClear = onClearQuery,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.search_results_found, products.size),
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                color = Color(0xFF64748B),
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(
                items = products,
                key = { it.id ?: "${it.name}_${it.hashCode()}" },
            ) { product ->
                ProductCard(
                    modifier = Modifier.fillMaxWidth(),
                    product = product,
                    onProductClick = { clickedProduct ->
                        clickedProduct.id?.let(onProductClick)
                    },
                )
            }
        }
    }
}

@Composable
fun SearchInputField(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange: (String) -> Unit,
    onClear: () -> Unit,
) {
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(50.dp)),
        value = query,
        onValueChange = onQueryChange,
        placeholder = {
            Text(
                text = stringResource(R.string.search_placeholder),
                color = Color(0xFF90A4AE),
                style = MaterialTheme.typography.bodyMedium,
            )
        },
        prefix = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = BrandColor,
                modifier = Modifier.size(22.dp),
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Clear",
                    tint = BrandColor,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { onClear() },
                )
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(50.dp),
        textStyle = MaterialTheme.typography.bodyMedium.copy(color = BrandColor),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedBorderColor = BrandColor,
            unfocusedBorderColor = BrandColor.copy(alpha = 0.5f),
        ),
    )
}

@Preview(showBackground = true)
@Composable
private fun SearchSuccessStatePreview() {
    MyPreview {
        SearchSuccessState(
            products = listOf(
                Product(id = "1", name = "Nike Air Jordan 1", imageUrl = "", price = 2500),
                Product(id = "2", name = "Samsung Galaxy Buds 2", imageUrl = "", price = 3999),
                Product(id = "3", name = "Sony WH-CH510", imageUrl = "", price = 1949),
                Product(id = "4", name = "Woman Shawl", imageUrl = "", price = 149),
            ),
            query = "shoes",
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchInputFieldPreview() {
    MyPreview {
        Box(modifier = Modifier.padding(16.dp)) {
            SearchInputField(
                query = "Nike",
                onQueryChange = {},
                onClear = {},
            )
        }
    }
}
