package com.route.firstapp.ui.auth.signIn

import com.route.domain.data.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface SignInContract {

    abstract class ViewModel : androidx.lifecycle.ViewModel() {
        abstract val states: StateFlow<State>
        abstract val events: Flow<Event>
        abstract fun doIntent(intent: Intent)
    }

    sealed class State {
        data object Idle : State()
        data object Loading : State()
        data class Error(val message: String) : State()
        data class Success(val user: User) : State()
    }

    sealed class Intent {
        data object CheckSession : Intent()
        data class Submit(val email: String, val password: String) : Intent()
        data object ClearError : Intent()
    }

    sealed class Event {
        data object NavigateToHome : Event()
        data object NavigateToSignUp : Event()
        data class ShowMessage(val message: String) : Event()
    }
}
