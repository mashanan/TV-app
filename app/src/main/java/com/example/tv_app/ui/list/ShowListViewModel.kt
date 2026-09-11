package com.example.tv_app.ui.list

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

class ShowListViewModel(
    private val repository: ShowRepository = ShowRepositoryImpl()
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Show>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Show>>> = _uiState.asStateFlow()

    init {
        loadShows()
    }

    fun loadShows() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val shows = repository.getShows()
                _uiState.value = UiState.Success(shows)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Something went wrong")
            }
        }
    }

    fun retry() = loadShows()
}
