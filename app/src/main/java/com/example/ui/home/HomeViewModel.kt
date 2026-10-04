package com.example.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.Movie
import com.example.data.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

data class HomeState(
    val trending: List<Movie> = emptyList(),
    val topRated: List<Movie> = emptyList(),
    val bollywood: List<Movie> = emptyList(),
    val hollywood: List<Movie> = emptyList(),
    val action: List<Movie> = emptyList(),
    val romance: List<Movie> = emptyList(),
    val sciFi: List<Movie> = emptyList(),
    val horror: List<Movie> = emptyList(),
    val comedy: List<Movie> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

class HomeViewModel(
    private val repository: MovieRepository = MovieRepository(com.example.data.remote.NetworkModule.tmdbService)
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state

    private var currentPage = 1

    init {
        fetchInitialData()
    }

    private fun fetchInitialData() {
        viewModelScope.launch {
            try {
                val flows = listOf(
                    repository.getTrendingMovies(1),
                    repository.getTopRatedMovies(1),
                    repository.getBollywoodMovies(1),
                    repository.getHollywoodMovies(1),
                    repository.getMoviesByGenre("28", 1), // Action
                    repository.getMoviesByGenre("10749", 1), // Romance
                    repository.getMoviesByGenre("878", 1), // Sci-Fi
                    repository.getMoviesByGenre("27", 1), // Horror
                    repository.getMoviesByGenre("35", 1) // Comedy
                )
                combine(flows) { results ->
                    HomeState(
                        trending = results[0],
                        topRated = results[1],
                        bollywood = results[2],
                        hollywood = results[3],
                        action = results[4],
                        romance = results[5],
                        sciFi = results[6],
                        horror = results[7],
                        comedy = results[8],
                        isLoading = false
                    )
                }.collect {
                    _state.value = it
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    fun loadMore() {
        if (_state.value.isLoading) return
        currentPage++
        viewModelScope.launch {
            try {
                repository.getTrendingMovies(currentPage).collect { newMovies ->
                    _state.value = _state.value.copy(
                        trending = _state.value.trending + newMovies
                    )
                }
            } catch (e: Exception) {
                // Handle pagination error silently or show a toast
            }
        }
    }
}
