package com.smh.movie.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.smh.movie.android.navigation.HomeRoute
import com.smh.movie.android.navigation.categoryRoute
import com.smh.movie.android.navigation.favouriteRoute
import com.smh.movie.android.navigation.homeRoute
import com.smh.movie.android.navigation.slideNavTransition
import com.smh.movie.android.ui.theme.MovieTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalSharedTransitionApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            MovieTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SharedTransitionLayout {
                        NavHost(
                            navController = navController,
                            startDestination = HomeRoute,
                            enterTransition = { slideNavTransition.enterTransition },
                            exitTransition = { slideNavTransition.exitTransition },
                            popEnterTransition = { slideNavTransition.popEnterTransition },
                            popExitTransition = { slideNavTransition.popExitTransition },
                        ) {
                            homeRoute(navController)
                            favouriteRoute(navController)
                            categoryRoute(navController)
                        }
                    }
                }
            }
        }
    }
}
