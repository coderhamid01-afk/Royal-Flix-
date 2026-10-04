package com.example.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.CastMember
import com.example.data.model.Movie
import com.example.data.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class DetailsUiState {
    object Loading : DetailsUiState()
    data class Success(val movie: Movie, val cast: List<CastMember>) : DetailsUiState()
    data class Error(val message: String) : DetailsUiState()
}

class DetailsViewModel(
    private val repository: MovieRepository = MovieRepository(com.example.data.remote.NetworkModule.tmdbService)
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailsUiState>(DetailsUiState.Loading)
    val uiState: StateFlow<DetailsUiState> = _uiState

    fun fetchDetails(movieId: Int) {
        viewModelScope.launch {
            _uiState.value = DetailsUiState.Loading
            try {
                repository.getMovieDetails(movieId).collect { movie ->
                    repository.getMovieCredits(movieId).collect { cast ->
                        _uiState.value = DetailsUiState.Success(movie, cast)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = DetailsUiState.Error(e.message ?: "Unknown Error")
            }
        }
    }
}
