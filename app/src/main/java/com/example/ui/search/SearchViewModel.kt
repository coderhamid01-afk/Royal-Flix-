package com.example.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.Movie
import com.example.data.repository.MovieRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class SearchUiState {
    object Idle : SearchUiState()
    object Loading : SearchUiState()
    data class Success(val results: List<Movie>) : SearchUiState()
    data class Error(val message: String) : SearchUiState()
}

class SearchViewModel(
    private val repository: MovieRepository = MovieRepository(com.example.data.remote.NetworkModule.tmdbService)
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val uiState: StateFlow<SearchUiState> = _uiState

    init {
        setupSearch()
    }

    @OptIn(FlowPreview::class)
    private fun setupSearch() {
        viewModelScope.launch {
            _query
                .debounce(300)
                .filter { it.isNotEmpty() }
                .distinctUntilChanged()
                .onEach { _uiState.value = SearchUiState.Loading }
                .flatMapLatest { repository.searchMovies(it) }
                .catch { e -> _uiState.value = SearchUiState.Error(e.message ?: "Unknown Error") }
                .collect { results ->
                    _uiState.value = SearchUiState.Success(results)
                }
        }
    }

    fun onQueryChange(newQuery: String) {
        _query.value = newQuery
        if (newQuery.isEmpty()) {
            _uiState.value = SearchUiState.Idle
        }
    }
}
