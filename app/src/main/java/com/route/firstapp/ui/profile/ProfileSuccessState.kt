package com.route.firstapp.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.route.domain.data.User
import com.route.firstapp.R
import com.route.firstapp.ui.theme.BrandColor
import com.route.firstapp.ui.theme.MyPreview

@Composable
fun ProfileSuccessState(
    modifier: Modifier = Modifier,
    user: User,
    isSigningOut: Boolean = false,
    onSignOutClick: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // User Avatar & Name Header
        ProfileHeader(user = user)

        // Account Details Card
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            text = stringResource(R.string.personal_information),
            style = MaterialTheme.typography.titleSmall,
            color = BrandColor,
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                ProfileInfoRow(
                    icon = Icons.Default.Person,
                    label = stringResource(R.string.full_name),
                    value = user.name,
                )
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = Color(0xFFECEFF1),
                )
                ProfileInfoRow(
                    icon = Icons.Default.Email,
                    label = stringResource(R.string.email_label),
                    value = user.email,
                )
                if (!user.role.isNullOrBlank()) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        color = Color(0xFFECEFF1),
                    )
                    ProfileInfoRow(
                        icon = Icons.Default.Person,
                        label = stringResource(R.string.account_role),
                        value = user.role.replaceFirstChar { it.uppercase() },
                    )
                }
            }
        }

        // Quick Actions Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                ProfileOptionRow(
                    icon = Icons.Default.ShoppingCart,
                    title = stringResource(R.string.my_orders),
                )
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = Color(0xFFECEFF1),
                )
                ProfileOptionRow(
                    icon = Icons.Default.Favorite,
                    title = stringResource(R.string.wishlist),
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Sign Out Button
        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            onClick = onSignOutClick,
            enabled = !isSigningOut,
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color(0xFFD32F2F),
            ),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFD32F2F)),
        ) {
            if (isSigningOut) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color(0xFFD32F2F),
                    strokeWidth = 2.dp,
                )
            } else {
                Text(
                    text = stringResource(R.string.sign_out),
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFFD32F2F),
                )
            }
        }
    }
}

@Composable
fun ProfileHeader(
    modifier: Modifier = Modifier,
    user: User,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        val initials = user.name
            .trim()
            .split(" ")
            .filter { it.isNotBlank() }
            .take(2)
            .map { it.first().uppercase() }
            .joinToString("")
            .ifEmpty { "U" }

        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(BrandColor)
                .border(3.dp, Color(0xFFECEFF1), CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = initials,
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        Text(
            text = user.name,
            style = MaterialTheme.typography.titleSmall.copy(fontSize = 20.sp),
            color = BrandColor,
            fontWeight = FontWeight.Bold,
        )

        Text(
            text = user.email,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF546E7A),
        )
    }
}

@Composable
fun ProfileInfoRow(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    value: String,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Color(0xFFF0F4F8)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = BrandColor,
                modifier = Modifier.size(20.dp),
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF78909C),
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                color = Color(0xFF263238),
            )
        }
    }
}

@Composable
fun ProfileOptionRow(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    onClick: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Color(0xFFF0F4F8)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = BrandColor,
                modifier = Modifier.size(20.dp),
            )
        }

        Text(
            modifier = Modifier.weight(1f),
            text = title,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            color = BrandColor,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileSuccessStatePreview() {
    MyPreview {
        ProfileSuccessState(
            user = User(
                name = "Ahmed Abd Al-Muti",
                email = "ahmed@route.com",
                role = "user",
            ),
            isSigningOut = false,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileHeaderPreview() {
    MyPreview {
        ProfileHeader(
            user = User(
                name = "Ahmed Abd Al-Muti",
                email = "ahmed@route.com",
                role = "user",
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileInfoRowPreview() {
    MyPreview {
        ProfileInfoRow(
            icon = Icons.Default.Email,
            label = "Email",
            value = "ahmed@route.com",
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileOptionRowPreview() {
    MyPreview {
        ProfileOptionRow(
            icon = Icons.Default.ShoppingCart,
            title = "My Orders",
        )
    }
}
