package com.route.api.dataSource

import com.route.api.ApiServices
import com.route.api.data.toProductsSortDto
import com.route.api.helpers.safeApiCallV2
import com.route.data.dataSource.ProductsOnlineDataSource
import com.route.domain.data.Product
import com.route.domain.data.ProductsSort
import com.route.domain.data.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductsOnlineDataSourceImpl @Inject constructor(
    val apiServices: ApiServices,
): ProductsOnlineDataSource {
    override fun getProducts(sort: ProductsSort): Flow<Resource<List<Product>?>> {

        return safeApiCallV2(
            api = {
                apiServices.getProducts(sort = sort.toProductsSortDto())
            },
            converter = { dtos ->
                dtos?.map { dto -> dto.toProduct() }?:listOf()
            }
        )
    }

    override fun searchProducts(query: String?): Flow<Resource<List<Product>?>> {
        return safeApiCallV2(
            api = {
                apiServices.getAllProducts(limit = 100)
            },
            converter = { dtos ->
                val products = dtos?.map { it.toProduct() } ?: emptyList()
                if (query.isNullOrBlank()) {
                    products
                } else {
                    val cleanQuery = query.trim().lowercase()
                    products.filter { product ->
                        product.name?.lowercase()?.contains(cleanQuery) == true
                    }
                }
            }
        )
    }
}