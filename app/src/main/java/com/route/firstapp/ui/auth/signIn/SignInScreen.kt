package com.route.firstapp.ui.auth.signIn

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    viewModel: SignInViewModel = hiltViewModel(),
    onNavigateToSignUp: () -> Unit,
    onNavigateToHome: () -> Unit,
) {
    val context = LocalContext.current

    LifecycleEventEffect(event = Lifecycle.Event.ON_CREATE) {
        viewModel.doIntent(SignInContract.Intent.CheckSession)
    }

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                SignInContract.Event.NavigateToHome -> onNavigateToHome()
                SignInContract.Event.NavigateToSignUp -> onNavigateToSignUp()
                is SignInContract.Event.ShowMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    val state = viewModel.states.collectAsStateWithLifecycle().value

    val isLoading = state is SignInContract.State.Loading
    val errorMessage = (state as? SignInContract.State.Error)?.message

    SignInContent(
        modifier = modifier,
        isLoading = isLoading,
        errorMessage = errorMessage,
        onSubmit = { email, password ->
            viewModel.doIntent(SignInContract.Intent.Submit(email, password))
        },
        onNavigateToSignUp = onNavigateToSignUp,
        onClearError = { viewModel.doIntent(SignInContract.Intent.ClearError) },
    )
}
