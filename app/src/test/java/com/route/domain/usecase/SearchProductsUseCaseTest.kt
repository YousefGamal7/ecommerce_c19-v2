package com.route.domain.usecase

import com.route.domain.data.Product
import com.route.domain.data.ProductsSort
import com.route.domain.data.Resource
import com.route.domain.repo.ProductsRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SearchProductsUseCaseTest {

    private val fakeRepo = object : ProductsRepo {
        override fun getProducts(sort: ProductsSort): Flow<Resource<List<Product>?>> {
            return flowOf(Resource.Success(emptyList()))
        }

        override fun searchProducts(query: String?): Flow<Resource<List<Product>?>> {
            val list = listOf(
                Product(id = "1", name = "Nike Shoes", price = 100),
                Product(id = "2", name = "Adidas Shirt", price = 50),
            )
            val filtered = if (query.isNullOrBlank()) {
                list
            } else {
                list.filter { it.name?.contains(query, ignoreCase = true) == true }
            }
            return flowOf(Resource.Success(filtered))
        }
    }

    private val useCase = SearchProductsUseCase(fakeRepo)

    @Test
    fun `searchProducts with blank query returns all products`() = runBlocking {
        val result = useCase.searchProducts("").first()
        assertTrue(result is Resource.Success)
        val data = (result as Resource.Success).data
        assertEquals(2, data?.size)
    }

    @Test
    fun `searchProducts with matching query returns filtered products`() = runBlocking {
        val result = useCase.searchProducts("Nike").first()
        assertTrue(result is Resource.Success)
        val data = (result as Resource.Success).data
        assertEquals(1, data?.size)
        assertEquals("Nike Shoes", data?.first()?.name)
    }

    @Test
    fun `searchProducts with non matching query returns empty list`() = runBlocking {
        val result = useCase.searchProducts("Puma").first()
        assertTrue(result is Resource.Success)
        val data = (result as Resource.Success).data
        assertTrue(data?.isEmpty() == true)
    }
}
