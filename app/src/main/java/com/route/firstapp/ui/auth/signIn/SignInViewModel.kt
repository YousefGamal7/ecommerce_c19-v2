package com.route.firstapp.ui.auth.signIn

import androidx.lifecycle.viewModelScope
import com.route.domain.data.Resource
import com.route.domain.data.SignInRequest
import com.route.domain.usecase.GetLoggedUserUseCase
import com.route.domain.usecase.SignInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val getLoggedUserUseCase: GetLoggedUserUseCase,
) : SignInContract.ViewModel() {

    private val _events = Channel<SignInContract.Event>(Channel.BUFFERED)
    override val events: Flow<SignInContract.Event> = _events.receiveAsFlow()

    private val _states = MutableStateFlow<SignInContract.State>(SignInContract.State.Idle)
    override val states: StateFlow<SignInContract.State> = _states.asStateFlow()

    override fun doIntent(intent: SignInContract.Intent) {
        when (intent) {
            SignInContract.Intent.CheckSession -> checkExistingSession()
            is SignInContract.Intent.Submit -> signIn(intent.email, intent.password)
            SignInContract.Intent.ClearError -> _states.value = SignInContract.State.Idle
        }
    }

    private fun checkExistingSession() {
        viewModelScope.launch {
            getLoggedUserUseCase.getLoggedUser().collect { resource ->
                if (resource is Resource.Success && resource.data != null) {
                    _events.send(SignInContract.Event.NavigateToHome)
                }
            }
        }
    }

    private fun signIn(email: String, password: String) {
        viewModelScope.launch {
            signInUseCase.signIn(SignInRequest(email = email.trim(), password = password))
                .collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            _states.emit(SignInContract.State.Loading)
                        }
                        is Resource.Success -> {
                            val authResult = resource.data
                            if (authResult != null) {
                                _states.emit(SignInContract.State.Success(user = authResult.user))
                                _events.send(SignInContract.Event.ShowMessage("Signed in successfully"))
                                _events.send(SignInContract.Event.NavigateToHome)
                            } else {
                                _states.emit(SignInContract.State.Error("Failed to sign in. Please try again."))
                            }
                        }
                        is Resource.Error -> {
                            val errorMsg = resource.throwable.message ?: "Incorrect email or password"
                            _states.emit(SignInContract.State.Error(errorMsg))
                        }
                    }
                }
        }
    }
}
