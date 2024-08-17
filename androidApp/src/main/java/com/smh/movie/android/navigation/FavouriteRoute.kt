package com.smh.movie.android.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.smh.movie.android.ui.favourite.FavouriteScreen
import kotlinx.serialization.Serializable

@Serializable
object FavouriteRoute

fun NavGraphBuilder.favouriteRoute(
    navController: NavController
) {
    composable<FavouriteRoute> {
        FavouriteScreen(
            onBack = navController::popBackStack
        )
    }
}