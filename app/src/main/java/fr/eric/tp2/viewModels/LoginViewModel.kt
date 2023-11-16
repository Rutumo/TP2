package fr.eric.tp2.viewModels

import androidx.lifecycle.ViewModel
import fr.eric.tp2.ui.state.LoginState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LoginState())
    val uiState : StateFlow<LoginState> = _uiState.asStateFlow()

    fun changeUsername(username: String){
        _uiState.update {
            it.copy(
                username = username,
                password = it.password
            )
        }
    }


    fun changePassword(password: String){
        _uiState.update {
            it.copy(
                username = it.username,
                password = password,
                error = password.length < 8,
                errorMessage = if (password.length < 8) "Password is too short" else TODO()
            )
        }
    }

    fun login(){

    }
}