package com.route.firstapp.ui.profile

import com.route.domain.data.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface ProfileContract {

    abstract class ViewModel : androidx.lifecycle.ViewModel() {
        abstract val states: StateFlow<State>
        abstract val events: Flow<Event>
        abstract fun doIntent(intent: Intent)
    }

    enum class AuthMode {
        SIGN_IN,
        SIGN_UP,
    }

    sealed class State {
        data object Loading : State()

        data class LoggedIn(
            val user: User,
            val isSigningOut: Boolean = false,
        ) : State()

        data class Guest(
            val authMode: AuthMode = AuthMode.SIGN_IN,
            val isLoading: Boolean = false,
            val errorMessage: String? = null,
        ) : State()

        data class Error(
            val message: String,
        ) : State()
    }

    sealed class Intent {
        data object Init : Intent()
        data class SwitchAuthMode(val mode: AuthMode) : Intent()
        data class SubmitSignIn(val email: String, val password: String) : Intent()
        data class SubmitSignUp(
            val name: String,
            val email: String,
            val password: String,
            val rePassword: String,
            val phone: String,
        ) : Intent()
        data object SignOut : Intent()
        data object ClearError : Intent()
    }

    sealed class Event {
        data class ShowMessage(val message: String) : Event()
    }
}
