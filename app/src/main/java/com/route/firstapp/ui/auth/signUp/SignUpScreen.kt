package com.route.firstapp.ui.auth.signUp

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = hiltViewModel(),
    onNavigateToSignIn: () -> Unit,
    onNavigateToHome: () -> Unit,
) {
    val context = LocalContext.current

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                SignUpContract.Event.NavigateToHome -> onNavigateToHome()
                SignUpContract.Event.NavigateToSignIn -> onNavigateToSignIn()
                is SignUpContract.Event.ShowMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    val state = viewModel.states.collectAsStateWithLifecycle().value

    val isLoading = state is SignUpContract.State.Loading
    val errorMessage = (state as? SignUpContract.State.Error)?.message

    SignUpContent(
        modifier = modifier,
        isLoading = isLoading,
        errorMessage = errorMessage,
        onSubmit = { name, email, password, rePassword, phone ->
            viewModel.doIntent(
                SignUpContract.Intent.Submit(
                    name = name,
                    email = email,
                    password = password,
                    rePassword = rePassword,
                    phone = phone,
                )
            )
        },
        onNavigateToSignIn = onNavigateToSignIn,
        onClearError = { viewModel.doIntent(SignUpContract.Intent.ClearError) },
    )
}
