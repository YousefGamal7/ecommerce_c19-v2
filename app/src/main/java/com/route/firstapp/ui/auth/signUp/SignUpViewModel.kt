package com.route.firstapp.ui.auth.signUp

import androidx.lifecycle.viewModelScope
import com.route.domain.data.Resource
import com.route.domain.data.SignUpRequest
import com.route.domain.usecase.SignUpUseCase
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
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
) : SignUpContract.ViewModel() {

    private val _events = Channel<SignUpContract.Event>(Channel.BUFFERED)
    override val events: Flow<SignUpContract.Event> = _events.receiveAsFlow()

    private val _states = MutableStateFlow<SignUpContract.State>(SignUpContract.State.Idle)
    override val states: StateFlow<SignUpContract.State> = _states.asStateFlow()

    override fun doIntent(intent: SignUpContract.Intent) {
        when (intent) {
            is SignUpContract.Intent.Submit -> signUp(
                name = intent.name,
                email = intent.email,
                password = intent.password,
                rePassword = intent.rePassword,
                phone = intent.phone,
            )
            SignUpContract.Intent.ClearError -> _states.value = SignUpContract.State.Idle
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
                        _states.emit(SignUpContract.State.Loading)
                    }
                    is Resource.Success -> {
                        val authResult = resource.data
                        if (authResult != null) {
                            _states.emit(SignUpContract.State.Success(user = authResult.user))
                            _events.send(SignUpContract.Event.ShowMessage("Account created successfully"))
                            _events.send(SignUpContract.Event.NavigateToHome)
                        } else {
                            _states.emit(SignUpContract.State.Error("Failed to create account. Please try again."))
                        }
                    }
                    is Resource.Error -> {
                        val errorMsg = resource.throwable.message ?: "Sign up failed"
                        _states.emit(SignUpContract.State.Error(errorMsg))
                    }
                }
            }
        }
    }
}
