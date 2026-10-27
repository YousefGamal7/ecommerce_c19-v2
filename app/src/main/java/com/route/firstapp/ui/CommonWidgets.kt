package com.route.firstapp.ui

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.route.domain.data.Product
import com.route.firstapp.R
import com.route.firstapp.ui.theme.BrandColor
import com.route.firstapp.ui.theme.MyPreview

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProductCard(
    modifier: Modifier = Modifier,
    product: Product,
    onProductClick: (product: Product) -> Unit = {},
    onFavoriteClick: (product: Product) -> Unit = {},
    onAddToCartClick: (product: Product) -> Unit = {},
) {
    val currentPrice = product.priceAfterDiscount ?: product.price
    val oldPrice = if (product.priceAfterDiscount != null) product.price else null
    val rating = product.ratingsAverage ?: 4.6

    Box(
        modifier = modifier
            .widthIn(max = 190.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(color = Color.White)
            .border(
                width = 1.5.dp,
                color = BrandColor.copy(alpha = 0.3f),
                shape = RoundedCornerShape(16.dp),
            )
            .clickable { onProductClick(product) },
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            // Image section with favorite icon
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .background(Color(0xFFF2F4F7)),
            ) {
                GlideImage(
                    modifier = Modifier.fillMaxSize(),
                    model = product.imageUrl,
                    contentScale = ContentScale.Crop,
                    contentDescription = product.name ?: "Product Image",
                )
                // Favorite button (top-right)
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(32.dp)
                        .shadow(elevation = 4.dp, shape = CircleShape)
                        .background(color = Color.White, shape = CircleShape)
                        .clickable { onFavoriteClick(product) },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        modifier = Modifier.size(18.dp),
                        painter = painterResource(R.drawable.ic_fav),
                        tint = BrandColor,
                        contentDescription = "Favorite",
                    )
                }
            }

            // Product Details section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                // Title (line 1)
                Text(
                    text = product.name ?: "",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium,
                        color = BrandColor,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                // Description (line 2)
                Text(
                    text = product.description ?: product.name ?: "",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = BrandColor,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                // Price Row
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (currentPrice != null) {
                        Text(
                            text = "EGP $currentPrice",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = BrandColor,
                            ),
                        )
                    }
                    if (oldPrice != null) {
                        Text(
                            text = "$oldPrice EGP",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = BrandColor.copy(alpha = 0.6f),
                                textDecoration = TextDecoration.LineThrough,
                                fontSize = 11.sp,
                            ),
                        )
                    }
                }

                // Rating & Add to cart button Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Text(
                            text = "Review ($rating)",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = BrandColor,
                                fontSize = 12.sp,
                            ),
                        )
                        Icon(
                            modifier = Modifier.size(15.dp),
                            painter = painterResource(R.drawable.ic_star),
                            tint = Color.Unspecified,
                            contentDescription = "Rating",
                        )
                    }

                    // Add Button (+)
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .background(color = BrandColor, shape = CircleShape)
                            .clickable { onAddToCartClick(product) },
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add to cart",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp),
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview(uiMode = UI_MODE_NIGHT_NO, device = Devices.PIXEL)
private fun ProductCardDiscountPreview() {
    MyPreview {
        Box(modifier = Modifier.padding(16.dp)) {
            ProductCard(
                product = Product(
                    id = "1",
                    name = "Nike Air Jordon",
                    description = "Nike shoes flexible for women",
                    imageUrl = "",
                    price = 2000,
                    priceAfterDiscount = 1200,
                    ratingsAverage = 4.6,
                ),
            )
        }
    }
}

@Composable
@Preview(uiMode = UI_MODE_NIGHT_NO, device = Devices.PIXEL)
private fun ProductCardRegularPreview() {
    MyPreview {
        Box(modifier = Modifier.padding(16.dp)) {
            ProductCard(
                product = Product(
                    id = "2",
                    name = "Woman Shawl",
                    description = "Material Polyester Blend Multicolour",
                    imageUrl = "",
                    price = 149,
                    ratingsAverage = 4.1,
                ),
            )
        }
    }
}