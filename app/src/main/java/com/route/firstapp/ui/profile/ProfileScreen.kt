package com.route.firstapp.ui.profile

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
    onNavigateToSignIn: () -> Unit = {},
) {
    val context = LocalContext.current

    LifecycleEventEffect(event = Lifecycle.Event.ON_CREATE) {
        viewModel.doIntent(ProfileContract.Intent.Init)
    }

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is ProfileContract.Event.ShowMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
                ProfileContract.Event.NavigateToSignIn -> {
                    onNavigateToSignIn()
                }
            }
        }
    }

    val states = viewModel.states.collectAsStateWithLifecycle()

    when (val state = states.value) {
        ProfileContract.State.Loading -> {
            ProfileLoadingState(modifier = modifier)
        }
        is ProfileContract.State.Error -> {
            ProfileErrorState(
                modifier = modifier.padding(16.dp),
                message = state.message,
                onRetryClick = { viewModel.doIntent(ProfileContract.Intent.Init) },
            )
        }
        is ProfileContract.State.LoggedIn -> {
            ProfileSuccessState(
                modifier = modifier,
                user = state.user,
                isSigningOut = state.isSigningOut,
                onSignOutClick = { viewModel.doIntent(ProfileContract.Intent.SignOut) },
            )
        }
        is ProfileContract.State.Guest -> {
            ProfileGuestState(
                modifier = modifier,
                authMode = state.authMode,
                isLoading = state.isLoading,
                errorMessage = state.errorMessage,
                onSwitchMode = { mode -> viewModel.doIntent(ProfileContract.Intent.SwitchAuthMode(mode)) },
                onSignInSubmit = { email, password ->
                    viewModel.doIntent(ProfileContract.Intent.SubmitSignIn(email, password))
                },
                onSignUpSubmit = { name, email, password, rePassword, phone ->
                    viewModel.doIntent(
                        ProfileContract.Intent.SubmitSignUp(
                            name = name,
                            email = email,
                            password = password,
                            rePassword = rePassword,
                            phone = phone,
                        )
                    )
                },
                onClearError = { viewModel.doIntent(ProfileContract.Intent.ClearError) },
            )
        }
    }
}
