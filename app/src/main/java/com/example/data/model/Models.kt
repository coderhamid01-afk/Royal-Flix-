package com.example.data.model

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    val results: List<Movie>
)

data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("vote_average") val voteAverage: Double,
    val runtime: Int? = null,
    val genres: List<Genre>? = null
) {
    val posterUrl get() = "https://image.tmdb.org/t/p/w500$posterPath"
    val backdropUrl get() = "https://image.tmdb.org/t/p/original$backdropPath"
}

data class Genre(
    val id: Int,
    val name: String
)

data class CreditsResponse(
    val cast: List<CastMember>
)

data class CastMember(
    val id: Int,
    val name: String,
    val character: String,
    @SerializedName("profile_path") val profilePath: String?
) {
    val profileUrl get() = "https://image.tmdb.org/t/p/w200$profilePath"
}
