package com.smh.movie.android.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.smh.movie.android.ui.category.CategoryScreen
import com.smh.movie.android.ui.category.CategoryType
import kotlinx.serialization.Serializable

@Serializable
data class CategoryRoute(
    val type: String
)

fun NavGraphBuilder.categoryRoute(
    navController: NavController
) {
    composable<CategoryRoute> {
        val route = it.toRoute<CategoryRoute>()
        CategoryScreen(
            categoryType = CategoryType.fromName(route.type),
            onBack = navController::navigateUp
        )
    }
}