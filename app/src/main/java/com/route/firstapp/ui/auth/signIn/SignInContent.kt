package com.route.firstapp.ui.auth.signIn

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
fun SignInContent(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    errorMessage: String? = null,
    onSubmit: (email: String, password: String) -> Unit = { _, _ -> },
    onNavigateToSignUp: () -> Unit = {},
    onClearError: () -> Unit = {},
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var validationError by remember { mutableStateOf<String?>(null) }

    val emailErrorText = stringResource(R.string.email_required)
    val passwordErrorText = stringResource(R.string.password_required)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrandColor)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Route Logo
        Image(
            modifier = Modifier
                .height(48.dp)
                .fillMaxWidth(),
            painter = painterResource(R.drawable.route_logo),
            contentDescription = "Route Logo",
            contentScale = ContentScale.Fit,
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Title
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.welcome_back_route),
            style = MaterialTheme.typography.titleSmall.copy(fontSize = 24.sp),
            color = Color.White,
            fontWeight = FontWeight.Bold,
        )

        // Subtitle
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.sign_in_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.8f),
        )

        // Error message banner
        val displayedError = validationError ?: errorMessage
        if (!displayedError.isNullOrBlank()) {
            SignInErrorBanner(
                message = displayedError,
                onDismiss = {
                    validationError = null
                    onClearError()
                },
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Email Label & Input Field
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.email_label),
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
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
            placeholder = {
                Text(
                    text = stringResource(R.string.email_placeholder),
                    color = Color(0xFF90A4AE),
                )
            },
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

        // Password Label & Input Field
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.password_label),
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
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
            placeholder = {
                Text(
                    text = stringResource(R.string.password_placeholder),
                    color = Color(0xFF90A4AE),
                )
            },
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

        // Forgot password
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            text = stringResource(R.string.forgot_password),
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal),
            color = Color.White,
            textAlign = androidx.compose.ui.text.style.TextAlign.End,
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Login Button
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            onClick = {
                if (email.isBlank() || !email.contains("@")) {
                    validationError = emailErrorText
                } else if (password.length < 6) {
                    validationError = passwordErrorText
                } else {
                    validationError = null
                    onSubmit(email, password)
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
                    text = stringResource(R.string.login_button),
                    style = MaterialTheme.typography.titleSmall.copy(fontSize = 18.sp),
                    color = BrandColor,
                    fontWeight = FontWeight.Bold,
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Switch to Sign Up
        TextButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onNavigateToSignUp,
        ) {
            Text(
                text = stringResource(R.string.create_account_prompt),
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            )
        }
    }
}

@Composable
fun SignInErrorBanner(
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
private fun SignInContentPreview() {
    MyPreview {
        SignInContent()
    }
}

@Preview(showBackground = true)
@Composable
private fun SignInErrorBannerPreview() {
    MyPreview {
        SignInErrorBanner(message = "Incorrect email or password")
    }
}
