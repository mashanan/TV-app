package com.example.tv_app.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tv_app.data.model.Show
import com.example.tv_app.data.repository.ShowRepository
import com.example.tv_app.data.repository.ShowRepositoryImpl
import com.example.tv_app.ui.common.UiState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ShowDetailViewModel(
    private val showId: Int,
    private val repository: ShowRepository = ShowRepositoryImpl()
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<Show>>(UiState.Loading)
    val uiState: StateFlow<UiState<Show>> = _uiState.asStateFlow()

    init {
        loadShow()
    }

    fun loadShow() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val show = repository.getShowDetail(showId)
                _uiState.value = UiState.Success(show)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Something went wrong")
            }
        }
    }

    fun retry() = loadShow()
}
