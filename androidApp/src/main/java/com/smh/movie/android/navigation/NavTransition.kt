package com.smh.movie.android.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally

const val NavAnimation = 300

data class NavTransition(
    val enterTransition: EnterTransition,
    val exitTransition: ExitTransition,
    val popEnterTransition: EnterTransition,
    val popExitTransition: ExitTransition
)

val slideNavTransition = NavTransition(
    enterTransition = slideInHorizontally(
        initialOffsetX = { 1500 },
        animationSpec = tween(NavAnimation)
    ),
    exitTransition = slideOutHorizontally(
        targetOffsetX = { -1500 },
        animationSpec = tween(NavAnimation)
    ),
    popEnterTransition = slideInHorizontally(
        initialOffsetX = { -1500 },
        animationSpec = tween(NavAnimation)
    ),
    popExitTransition = slideOutHorizontally(
        targetOffsetX = { 1500 },
        animationSpec = tween(NavAnimation)
    )
)