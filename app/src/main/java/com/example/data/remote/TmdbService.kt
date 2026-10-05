package com.example.data.remote

import com.example.data.model.CreditsResponse
import com.example.data.model.Movie
import com.example.data.model.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbService {
    @GET("trending/movie/day")
    suspend fun getTrendingMovies(
        @Query("page") page: Int = 1
    ): MovieResponse

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("page") page: Int = 1
    ): MovieResponse

    @GET("discover/movie")
    suspend fun discoverMovies(
        @Query("page") page: Int = 1,
        @Query("primary_release_date.lte") releasedBefore: String? = null,
        @Query("release_date.lte") releasedBeforeSecondary: String? = null,
        @Query("with_release_type") releaseType: String? = "4|5",
        @Query("with_original_language") language: String? = null,
        @Query("with_genres") genres: String? = null,
        @Query("sort_by") sortBy: String = "popularity.desc"
    ): MovieResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int
    ): Movie

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCredits(
        @Path("movie_id") movieId: Int
    ): CreditsResponse

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String
    ): MovieResponse
}
