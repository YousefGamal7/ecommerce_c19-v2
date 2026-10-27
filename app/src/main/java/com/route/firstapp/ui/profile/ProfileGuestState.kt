package com.route.firstapp.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.route.firstapp.R
import com.route.firstapp.ui.theme.BrandColor
import com.route.firstapp.ui.theme.MyPreview

@Composable
fun ProfileGuestState(
    modifier: Modifier = Modifier,
    authMode: ProfileContract.AuthMode = ProfileContract.AuthMode.SIGN_IN,
    isLoading: Boolean = false,
    errorMessage: String? = null,
    onSwitchMode: (ProfileContract.AuthMode) -> Unit = {},
    onSignInSubmit: (String, String) -> Unit = { _, _ -> },
    onSignUpSubmit: (String, String, String, String, String) -> Unit = { _, _, _, _, _ -> },
    onClearError: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // Logo
        Image(
            modifier = Modifier
                .height(36.dp)
                .padding(top = 8.dp),
            painter = painterResource(R.drawable.route_logo),
            contentDescription = "Route Logo",
            contentScale = ContentScale.FillHeight,
        )

        // Header Title
        Text(
            text = if (authMode == ProfileContract.AuthMode.SIGN_IN) {
                stringResource(R.string.welcome_back)
            } else {
                stringResource(R.string.create_account_title)
            },
            style = MaterialTheme.typography.titleSmall.copy(fontSize = 22.sp),
            color = BrandColor,
            fontWeight = FontWeight.Bold,
        )

        Text(
            text = if (authMode == ProfileContract.AuthMode.SIGN_IN) {
                stringResource(R.string.welcome_back_subtitle)
            } else {
                stringResource(R.string.create_account_subtitle)
            },
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF607D8B),
        )

        PrimaryTabRow(
            selectedTabIndex = if (authMode == ProfileContract.AuthMode.SIGN_IN) 0 else 1,
            containerColor = Color(0xFFF0F4F8),
            contentColor = BrandColor,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
        ) {
            Tab(
                selected = authMode == ProfileContract.AuthMode.SIGN_IN,
                onClick = { onSwitchMode(ProfileContract.AuthMode.SIGN_IN) },
                text = {
                    Text(
                        text = stringResource(R.string.sign_in),
                        fontWeight = if (authMode == ProfileContract.AuthMode.SIGN_IN) FontWeight.Bold else FontWeight.Normal,
                        color = if (authMode == ProfileContract.AuthMode.SIGN_IN) BrandColor else Color(0xFF78909C),
                    )
                },
            )
            Tab(
                selected = authMode == ProfileContract.AuthMode.SIGN_UP,
                onClick = { onSwitchMode(ProfileContract.AuthMode.SIGN_UP) },
                text = {
                    Text(
                        text = stringResource(R.string.sign_up),
                        fontWeight = if (authMode == ProfileContract.AuthMode.SIGN_UP) FontWeight.Bold else FontWeight.Normal,
                        color = if (authMode == ProfileContract.AuthMode.SIGN_UP) BrandColor else Color(0xFF78909C),
                    )
                },
            )
        }

        // Error message banner
        if (!errorMessage.isNullOrBlank()) {
            AuthErrorBanner(
                message = errorMessage,
                onDismiss = onClearError,
            )
        }

        // Form
        if (authMode == ProfileContract.AuthMode.SIGN_IN) {
            SignInForm(
                isLoading = isLoading,
                onSubmit = onSignInSubmit,
                onSwitchToSignUp = { onSwitchMode(ProfileContract.AuthMode.SIGN_UP) },
            )
        } else {
            SignUpForm(
                isLoading = isLoading,
                onSubmit = onSignUpSubmit,
                onSwitchToSignIn = { onSwitchMode(ProfileContract.AuthMode.SIGN_IN) },
            )
        }
    }
}

@Composable
fun AuthErrorBanner(
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

@Composable
fun SignInForm(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    onSubmit: (email: String, password: String) -> Unit,
    onSwitchToSignUp: () -> Unit,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var validationError by remember { mutableStateOf<String?>(null) }

    val emailErrorText = stringResource(R.string.email_required)
    val passwordErrorText = stringResource(R.string.password_required)

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = email,
            onValueChange = {
                email = it
                validationError = null
            },
            label = { Text(stringResource(R.string.email_label)) },
            placeholder = { Text(stringResource(R.string.email_placeholder)) },
            leadingIcon = {
                Icon(Icons.Default.Email, contentDescription = null, tint = BrandColor)
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BrandColor,
                focusedLabelColor = BrandColor,
            ),
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = password,
            onValueChange = {
                password = it
                validationError = null
            },
            label = { Text(stringResource(R.string.password_label)) },
            placeholder = { Text(stringResource(R.string.password_placeholder)) },
            leadingIcon = {
                Icon(Icons.Default.Lock, contentDescription = null, tint = BrandColor)
            },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Text(
                        text = if (passwordVisible) "Hide" else "Show",
                        style = MaterialTheme.typography.bodySmall,
                        color = BrandColor,
                    )
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BrandColor,
                focusedLabelColor = BrandColor,
            ),
        )

        if (validationError != null) {
            Text(
                text = validationError ?: "",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFC62828),
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
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
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BrandColor),
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White,
                    strokeWidth = 2.dp,
                )
            } else {
                Text(
                    text = stringResource(R.string.sign_in),
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color.White,
                )
            }
        }

        TextButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onSwitchToSignUp,
        ) {
            Text(
                text = stringResource(R.string.dont_have_account),
                color = BrandColor,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            )
        }
    }
}

@Composable
fun SignUpForm(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    onSubmit: (name: String, email: String, password: String, rePassword: String, phone: String) -> Unit,
    onSwitchToSignIn: () -> Unit,
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
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = name,
            onValueChange = {
                name = it
                validationError = null
            },
            label = { Text(stringResource(R.string.full_name)) },
            placeholder = { Text(stringResource(R.string.name_placeholder)) },
            leadingIcon = {
                Icon(Icons.Default.Person, contentDescription = null, tint = BrandColor)
            },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BrandColor,
                focusedLabelColor = BrandColor,
            ),
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = email,
            onValueChange = {
                email = it
                validationError = null
            },
            label = { Text(stringResource(R.string.email_label)) },
            placeholder = { Text(stringResource(R.string.email_placeholder)) },
            leadingIcon = {
                Icon(Icons.Default.Email, contentDescription = null, tint = BrandColor)
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BrandColor,
                focusedLabelColor = BrandColor,
            ),
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = password,
            onValueChange = {
                password = it
                validationError = null
            },
            label = { Text(stringResource(R.string.password_label)) },
            placeholder = { Text(stringResource(R.string.password_placeholder)) },
            leadingIcon = {
                Icon(Icons.Default.Lock, contentDescription = null, tint = BrandColor)
            },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Text(
                        text = if (passwordVisible) "Hide" else "Show",
                        style = MaterialTheme.typography.bodySmall,
                        color = BrandColor,
                    )
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BrandColor,
                focusedLabelColor = BrandColor,
            ),
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = rePassword,
            onValueChange = {
                rePassword = it
                validationError = null
            },
            label = { Text(stringResource(R.string.confirm_password_label)) },
            placeholder = { Text(stringResource(R.string.confirm_password_placeholder)) },
            leadingIcon = {
                Icon(Icons.Default.Lock, contentDescription = null, tint = BrandColor)
            },
            trailingIcon = {
                IconButton(onClick = { rePasswordVisible = !rePasswordVisible }) {
                    Text(
                        text = if (rePasswordVisible) "Hide" else "Show",
                        style = MaterialTheme.typography.bodySmall,
                        color = BrandColor,
                    )
                }
            },
            visualTransformation = if (rePasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BrandColor,
                focusedLabelColor = BrandColor,
            ),
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = phone,
            onValueChange = {
                phone = it
                validationError = null
            },
            label = { Text(stringResource(R.string.phone_label)) },
            placeholder = { Text(stringResource(R.string.phone_placeholder)) },
            leadingIcon = {
                Icon(Icons.Default.Phone, contentDescription = null, tint = BrandColor)
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BrandColor,
                focusedLabelColor = BrandColor,
            ),
        )

        if (validationError != null) {
            Text(
                text = validationError ?: "",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFC62828),
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            onClick = {
                when {
                    name.trim().length < 3 -> validationError = nameErrorText
                    email.isBlank() || !email.contains("@") -> validationError = emailErrorText
                    password.length < 6 -> validationError = passwordErrorText
                    password != rePassword -> validationError = passwordNotMatchText
                    phone.trim().length < 11 -> validationError = phoneErrorText
                    else -> {
                        validationError = null
                        onSubmit(name, email, password, rePassword, phone)
                    }
                }
            },
            enabled = !isLoading,
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BrandColor),
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White,
                    strokeWidth = 2.dp,
                )
            } else {
                Text(
                    text = stringResource(R.string.sign_up),
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color.White,
                )
            }
        }

        TextButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onSwitchToSignIn,
        ) {
            Text(
                text = stringResource(R.string.already_have_account),
                color = BrandColor,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileGuestStateSignInPreview() {
    MyPreview {
        ProfileGuestState(
            authMode = ProfileContract.AuthMode.SIGN_IN,
            isLoading = false,
            errorMessage = null,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileGuestStateSignUpPreview() {
    MyPreview {
        ProfileGuestState(
            authMode = ProfileContract.AuthMode.SIGN_UP,
            isLoading = false,
            errorMessage = null,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AuthErrorBannerPreview() {
    MyPreview {
        AuthErrorBanner(
            message = "Incorrect email or password",
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SignInFormPreview() {
    MyPreview {
        SignInForm(
            isLoading = false,
            onSubmit = { _, _ -> },
            onSwitchToSignUp = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SignUpFormPreview() {
    MyPreview {
        SignUpForm(
            isLoading = false,
            onSubmit = { _, _, _, _, _ -> },
            onSwitchToSignIn = {},
        )
    }
}
