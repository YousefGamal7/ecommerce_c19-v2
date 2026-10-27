package com.route.firstapp.ui.auth.signUp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.route.firstapp.R
import com.route.firstapp.ui.theme.BrandColor
import com.route.firstapp.ui.theme.MyPreview

@Composable
fun SignUpContent(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    errorMessage: String? = null,
    onSubmit: (name: String, email: String, password: String, rePassword: String, phone: String) -> Unit = { _, _, _, _, _ -> },
    onNavigateToSignIn: () -> Unit = {},
    onClearError: () -> Unit = {},
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rePassword by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var rePasswordVisible by remember { mutableStateOf(false) }
    var validationError by remember { mutableStateOf<String?>(null) }

    val nameErrorText = stringResource(R.string.name_required)
    val emailErrorText = stringResource(R.string.email_required)
    val passwordErrorText = stringResource(R.string.password_required)
    val passwordNotMatchText = stringResource(R.string.password_not_match)
    val phoneErrorText = stringResource(R.string.phone_required)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrandColor)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        // Route Logo
        Image(
            modifier = Modifier
                .height(44.dp)
                .fillMaxWidth(),
            painter = painterResource(R.drawable.route_logo),
            contentDescription = "Route Logo",
            contentScale = ContentScale.Fit,
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Title
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.create_account_title),
            style = MaterialTheme.typography.titleSmall.copy(fontSize = 24.sp),
            color = Color.White,
            fontWeight = FontWeight.Bold,
        )

        // Subtitle
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.create_account_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.8f),
        )

        // Error message banner
        val displayedError = validationError ?: errorMessage
        if (!displayedError.isNullOrBlank()) {
            SignUpErrorBanner(
                message = displayedError,
                onDismiss = {
                    validationError = null
                    onClearError()
                },
            )
        }

        // Full Name
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.full_name),
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            color = Color.White,
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = name,
            onValueChange = {
                name = it
                validationError = null
                onClearError()
            },
            placeholder = { Text(stringResource(R.string.name_placeholder), color = Color(0xFF90A4AE)) },
            singleLine = true,
            shape = RoundedCornerShape(15.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedTextColor = Color(0xFF1E293B),
                unfocusedTextColor = Color(0xFF1E293B),
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.White,
            ),
        )

        // Mobile Number
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.phone_label),
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            color = Color.White,
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = phone,
            onValueChange = {
                phone = it
                validationError = null
                onClearError()
            },
            placeholder = { Text(stringResource(R.string.phone_placeholder), color = Color(0xFF90A4AE)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            shape = RoundedCornerShape(15.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedTextColor = Color(0xFF1E293B),
                unfocusedTextColor = Color(0xFF1E293B),
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.White,
            ),
        )

        // Email
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.email_label),
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            color = Color.White,
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = email,
            onValueChange = {
                email = it
                validationError = null
                onClearError()
            },
            placeholder = { Text(stringResource(R.string.email_placeholder), color = Color(0xFF90A4AE)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            shape = RoundedCornerShape(15.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedTextColor = Color(0xFF1E293B),
                unfocusedTextColor = Color(0xFF1E293B),
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.White,
            ),
        )

        // Password
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.password_label),
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            color = Color.White,
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = password,
            onValueChange = {
                password = it
                validationError = null
                onClearError()
            },
            placeholder = { Text(stringResource(R.string.password_placeholder), color = Color(0xFF90A4AE)) },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Text(
                        text = if (passwordVisible) "Hide" else "Show",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        color = BrandColor,
                    )
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            shape = RoundedCornerShape(15.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedTextColor = Color(0xFF1E293B),
                unfocusedTextColor = Color(0xFF1E293B),
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.White,
            ),
        )

        // Confirm Password
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.confirm_password_label),
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            color = Color.White,
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = rePassword,
            onValueChange = {
                rePassword = it
                validationError = null
                onClearError()
            },
            placeholder = { Text(stringResource(R.string.confirm_password_placeholder), color = Color(0xFF90A4AE)) },
            trailingIcon = {
                IconButton(onClick = { rePasswordVisible = !rePasswordVisible }) {
                    Text(
                        text = if (rePasswordVisible) "Hide" else "Show",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        color = BrandColor,
                    )
                }
            },
            visualTransformation = if (rePasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            shape = RoundedCornerShape(15.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedTextColor = Color(0xFF1E293B),
                unfocusedTextColor = Color(0xFF1E293B),
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.White,
            ),
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Submit Button
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            onClick = {
                when {
                    name.trim().length < 3 -> validationError = nameErrorText
                    phone.trim().length < 11 -> validationError = phoneErrorText
                    email.isBlank() || !email.contains("@") -> validationError = emailErrorText
                    password.length < 6 -> validationError = passwordErrorText
                    password != rePassword -> validationError = passwordNotMatchText
                    else -> {
                        validationError = null
                        onSubmit(name, email, password, rePassword, phone)
                    }
                }
            },
            enabled = !isLoading,
            shape = RoundedCornerShape(15.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = BrandColor,
            ),
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = BrandColor,
                    strokeWidth = 2.5.dp,
                )
            } else {
                Text(
                    text = stringResource(R.string.sign_up),
                    style = MaterialTheme.typography.titleSmall.copy(fontSize = 18.sp),
                    color = BrandColor,
                    fontWeight = FontWeight.Bold,
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Switch to Sign In
        TextButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onNavigateToSignIn,
        ) {
            Text(
                text = stringResource(R.string.already_have_account_prompt),
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            )
        }
    }
}

@Composable
fun SignUpErrorBanner(
    modifier: Modifier = Modifier,
    message: String,
    onDismiss: () -> Unit = {},
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = message,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                color = Color(0xFFC62828),
            )
            Text(
                modifier = Modifier
                    .clickable { onDismiss() }
                    .padding(start = 8.dp),
                text = "✕",
                color = Color(0xFFC62828),
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SignUpContentPreview() {
    MyPreview {
        SignUpContent()
    }
}

@Preview(showBackground = true)
@Composable
private fun SignUpErrorBannerPreview() {
    MyPreview {
        SignUpErrorBanner(message = "Account Already Exists")
    }
}
