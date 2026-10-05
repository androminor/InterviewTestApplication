package com.example.userlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.di.DispatcherQualifiers
import com.example.network.ErrorState
import com.example.network.ResponseState
import com.example.user.usecase.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserListViewmodel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    @param:DispatcherQualifiers.IoDispatcher val dispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        loadUsers()
    }

    fun loadUsers() {
        viewModelScope.launch(dispatcher) {
            //initial
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = getUserUseCase()

            when (result) {
                is ResponseState.Success -> {
                    //loading
                    _uiState.value = _uiState.value.copy(
                        data = result.data ?: emptyList(),
                        isLoading = false
                    )
                }

                is ResponseState.Error -> {
                    //error
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = mapError(result.error)
                    )
                }
            }
        }
    }

}
private fun mapError(errorState: ErrorState): String {
    return when (errorState) {
        is ErrorState.ServerDownError -> "Server is currently down please try again"
        is ErrorState.NoInternetException -> "No internet connection"
        is ErrorState.NotFoundError -> "User not found"
        is ErrorState.UndefinedError -> "An unknow error occurred"
    }
}


