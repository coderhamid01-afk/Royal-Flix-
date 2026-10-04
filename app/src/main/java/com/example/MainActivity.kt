package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ui.details.DetailsScreen
import com.example.ui.main.MainScaffold
import com.example.ui.navigation.Details
import com.example.ui.navigation.Main
import com.example.ui.navigation.Player
import com.example.ui.navigation.Splash
import com.example.ui.player.VideoPlayerScreen
import com.example.ui.splash.SplashScreen
import com.example.ui.theme.RoyalFlixTheme
import androidx.navigation.toRoute

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RoyalFlixTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Splash
                ) {
                    composable<Splash>(
                        exitTransition = { fadeOut(animationSpec = tween(500)) }
                    ) {
                        SplashScreen(
                            onNavigateToMain = {
                                navController.navigate(Main) {
                                    popUpTo(Splash) { inclusive = true }
                                }
                            }
                        )
                    }
                    composable<Main>(
                        enterTransition = { fadeIn(animationSpec = tween(500)) }
                    ) {
                        MainScaffold(
                            onNavigateToDetails = { movieId ->
                                navController.navigate(Details(movieId))
                            }
                        )
                    }
                    composable<Details>(
                        enterTransition = {
                            slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.Up,
                                animationSpec = tween(500)
                            )
                        },
                        exitTransition = {
                            slideOutOfContainer(
                                AnimatedContentTransitionScope.SlideDirection.Down,
                                animationSpec = tween(500)
                            )
                        }
                    ) { backStackEntry ->
                        val details: Details = backStackEntry.toRoute()
                        DetailsScreen(
                            movieId = details.movieId,
                            onBack = { navController.popBackStack() },
                            onWatchNow = { movieId ->
                                navController.navigate(Player(movieId))
                            }
                        )
                    }
                    composable<Player> { backStackEntry ->
                        val player: Player = backStackEntry.toRoute()
                        VideoPlayerScreen(movieId = player.movieId)
                    }
                }
            }
        }
    }
}
