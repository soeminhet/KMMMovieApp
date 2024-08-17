package com.smh.movie.android.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.smh.movie.android.ui.home.HomeScreen
import kotlinx.serialization.Serializable

@Serializable
object HomeRoute

fun NavGraphBuilder.homeRoute(
    navController: NavController,
) {
    composable<HomeRoute> {
        HomeScreen(
            goToFavourite = {
                navController.navigate(FavouriteRoute)
            },
            goToCategory = {
                navController.navigate(CategoryRoute(it.name))
            },
        )
    }
}