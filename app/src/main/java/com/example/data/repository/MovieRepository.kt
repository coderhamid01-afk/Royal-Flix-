package com.example.data.repository

import com.example.BuildConfig
import com.example.data.model.CastMember
import com.example.data.model.Movie
import com.example.data.remote.TmdbService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MovieRepository(
    private val tmdbService: TmdbService
) {
    private val apiKey = BuildConfig.TMDB_API_KEY
    private val currentDate: String
        get() = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    fun getTrendingMovies(page: Int = 1): Flow<List<Movie>> = flow {
        emit(tmdbService.discoverMovies(
            apiKey = apiKey,
            page = page,
            releasedBefore = currentDate,
            releasedBeforeSecondary = currentDate,
            releaseType = "4|5",
            sortBy = "popularity.desc"
        ).results)
    }

    fun getTopRatedMovies(page: Int = 1): Flow<List<Movie>> = flow {
        emit(tmdbService.discoverMovies(
            apiKey = apiKey,
            page = page,
            releasedBefore = currentDate,
            releasedBeforeSecondary = currentDate,
            releaseType = "4|5",
            sortBy = "vote_average.desc"
        ).results.filter { it.voteAverage > 7.0 })
    }

    fun getBollywoodMovies(page: Int = 1): Flow<List<Movie>> = flow {
        emit(tmdbService.discoverMovies(
            apiKey = apiKey,
            page = page,
            releasedBefore = currentDate,
            releasedBeforeSecondary = currentDate,
            releaseType = "4|5",
            language = "hi",
            sortBy = "primary_release_date.desc"
        ).results)
    }

    fun getHollywoodMovies(page: Int = 1): Flow<List<Movie>> = flow {
        emit(tmdbService.discoverMovies(
            apiKey = apiKey,
            page = page,
            releasedBefore = currentDate,
            releasedBeforeSecondary = currentDate,
            releaseType = "4|5",
            language = "en",
            sortBy = "popularity.desc"
        ).results)
    }

    fun getMoviesByGenre(genreId: String, page: Int = 1): Flow<List<Movie>> = flow {
        emit(tmdbService.discoverMovies(
            apiKey = apiKey,
            page = page,
            releasedBefore = currentDate,
            releasedBeforeSecondary = currentDate,
            releaseType = "4|5",
            genres = genreId,
            sortBy = "popularity.desc"
        ).results)
    }

    fun getMovieDetails(movieId: Int): Flow<Movie> = flow {
        emit(tmdbService.getMovieDetails(movieId, apiKey))
    }

    fun getMovieCredits(movieId: Int): Flow<List<CastMember>> = flow {
        emit(tmdbService.getMovieCredits(movieId, apiKey).cast)
    }

    fun searchMovies(query: String): Flow<List<Movie>> = flow {
        emit(tmdbService.searchMovies(query, apiKey).results)
    }
}
