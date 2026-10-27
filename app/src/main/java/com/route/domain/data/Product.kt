package com.route.domain.data

data class Product(
    val id: String? = null,
    val name: String? = null,
    val description: String? = null,
    val imageUrl: String? = null,
    val price: Int? = null,
    val priceAfterDiscount: Int? = null,
    val ratingsAverage: Double? = null,
)

