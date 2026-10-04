package com.example.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
object Splash

@Serializable
object Main

@Serializable
object Home

@Serializable
object Search

@Serializable
object MyList

@Serializable
object Upcoming

@Serializable
object Settings

@Serializable
data class Details(val movieId: Int)

@Serializable
data class Player(val movieId: Int)
