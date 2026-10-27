package com.route.firstapp.ui.profile

import androidx.lifecycle.viewModelScope
import com.route.domain.data.Resource
import com.route.domain.data.SignInRequest
import com.route.domain.data.SignUpRequest
import com.route.domain.usecase.GetLoggedUserUseCase
import com.route.domain.usecase.SignInUseCase
import com.route.domain.usecase.SignOutUseCase
import com.route.domain.usecase.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getLoggedUserUseCase: GetLoggedUserUseCase,
    private val signInUseCase: SignInUseCase,
    private val signUpUseCase: SignUpUseCase,
    private val signOutUseCase: SignOutUseCase,
) : ProfileContract.ViewModel() {

    private val _events = Channel<ProfileContract.Event>(Channel.BUFFERED)
    override val events: Flow<ProfileContract.Event> = _events.receiveAsFlow()

    private val _states = MutableStateFlow<ProfileContract.State>(ProfileContract.State.Loading)
    override val states: StateFlow<ProfileContract.State> = _states.asStateFlow()

    override fun doIntent(intent: ProfileContract.Intent) {
        when (intent) {
            ProfileContract.Intent.Init -> checkUserSession()
            is ProfileContract.Intent.SwitchAuthMode -> switchAuthMode(intent.mode)
            is ProfileContract.Intent.SubmitSignIn -> signIn(intent.email, intent.password)
            is ProfileContract.Intent.SubmitSignUp -> signUp(
                name = intent.name,
                email = intent.email,
                password = intent.password,
                rePassword = intent.rePassword,
                phone = intent.phone,
            )
            ProfileContract.Intent.SignOut -> signOut()
            ProfileContract.Intent.ClearError -> clearError()
        }
    }

    private fun checkUserSession() {
        viewModelScope.launch {
            getLoggedUserUseCase.getLoggedUser().collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _states.emit(ProfileContract.State.Loading)
                    }
                    is Resource.Success -> {
                        val user = resource.data
                        if (user != null) {
                            _states.emit(ProfileContract.State.LoggedIn(user = user))
                        } else {
                            _states.emit(ProfileContract.State.Guest(authMode = ProfileContract.AuthMode.SIGN_IN))
                        }
                    }
                    is Resource.Error -> {
                        _states.emit(
                            ProfileContract.State.Guest(
                                authMode = ProfileContract.AuthMode.SIGN_IN,
                                errorMessage = resource.throwable.message,
                            )
                        )
                    }
                }
            }
        }
    }

    private fun switchAuthMode(mode: ProfileContract.AuthMode) {
        _states.update { current ->
            if (current is ProfileContract.State.Guest) {
                current.copy(authMode = mode, errorMessage = null)
            } else {
                ProfileContract.State.Guest(authMode = mode)
            }
        }
    }

    private fun clearError() {
        _states.update { current ->
            if (current is ProfileContract.State.Guest) {
                current.copy(errorMessage = null)
            } else {
                current
            }
        }
    }

    private fun signIn(email: String, password: String) {
        viewModelScope.launch {
            signInUseCase.signIn(SignInRequest(email = email.trim(), password = password))
                .collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            _states.update { current ->
                                if (current is ProfileContract.State.Guest) {
                                    current.copy(isLoading = true, errorMessage = null)
                                } else {
                                    ProfileContract.State.Guest(
                                        authMode = ProfileContract.AuthMode.SIGN_IN,
                                        isLoading = true,
                                    )
                                }
                            }
                        }
                        is Resource.Success -> {
                            val authResult = resource.data
                            if (authResult != null) {
                                _states.emit(ProfileContract.State.LoggedIn(user = authResult.user))
                                _events.send(ProfileContract.Event.ShowMessage("Signed in successfully"))
                            } else {
                                _states.emit(
                                    ProfileContract.State.Guest(
                                        authMode = ProfileContract.AuthMode.SIGN_IN,
                                        errorMessage = "Failed to sign in. Please try again.",
                                    )
                                )
                            }
                        }
                        is Resource.Error -> {
                            val errorMsg = resource.throwable.message ?: "Incorrect email or password"
                            _states.emit(
                                ProfileContract.State.Guest(
                                    authMode = ProfileContract.AuthMode.SIGN_IN,
                                    isLoading = false,
                                    errorMessage = errorMsg,
                                )
                            )
                        }
                    }
                }
        }
    }

    private fun signUp(
        name: String,
        email: String,
        password: String,
        rePassword: String,
        phone: String,
    ) {
        viewModelScope.launch {
            signUpUseCase.signUp(
                SignUpRequest(
                    name = name.trim(),
                    email = email.trim(),
                    password = password,
                    rePassword = rePassword,
                    phone = phone.trim(),
                )
            ).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _states.update { current ->
                            if (current is ProfileContract.State.Guest) {
                                current.copy(isLoading = true, errorMessage = null)
                            } else {
                                ProfileContract.State.Guest(
                                    authMode = ProfileContract.AuthMode.SIGN_UP,
                                    isLoading = true,
                                )
                            }
                        }
                    }
                    is Resource.Success -> {
                        val authResult = resource.data
                        if (authResult != null) {
                            _states.emit(ProfileContract.State.LoggedIn(user = authResult.user))
                            _events.send(ProfileContract.Event.ShowMessage("Account created successfully"))
                        } else {
                            _states.emit(
                                ProfileContract.State.Guest(
                                    authMode = ProfileContract.AuthMode.SIGN_UP,
                                    errorMessage = "Failed to create account. Please try again.",
                                )
                            )
                        }
                    }
                    is Resource.Error -> {
                        val errorMsg = resource.throwable.message ?: "Sign up failed"
                        _states.emit(
                            ProfileContract.State.Guest(
                                authMode = ProfileContract.AuthMode.SIGN_UP,
                                isLoading = false,
                                errorMessage = errorMsg,
                            )
                        )
                    }
                }
            }
        }
    }

    private fun signOut() {
        viewModelScope.launch {
            signOutUseCase.signOut().collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _states.update { current ->
                            if (current is ProfileContract.State.LoggedIn) {
                                current.copy(isSigningOut = true)
                            } else {
                                current
                            }
                        }
                    }
                    is Resource.Success -> {
                        _states.emit(ProfileContract.State.Guest(authMode = ProfileContract.AuthMode.SIGN_IN))
                        _events.send(ProfileContract.Event.ShowMessage("Signed out successfully"))
                        _events.send(ProfileContract.Event.NavigateToSignIn)
                    }
                    is Resource.Error -> {
                        _states.update { current ->
                            if (current is ProfileContract.State.LoggedIn) {
                                current.copy(isSigningOut = false)
                            } else {
                                current
                            }
                        }
                        _events.send(
                            ProfileContract.Event.ShowMessage(
                                resource.throwable.message ?: "Sign out failed"
                            )
                        )
                    }
                }
            }
        }
    }
}
