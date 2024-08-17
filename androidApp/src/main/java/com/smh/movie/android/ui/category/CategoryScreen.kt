package com.smh.movie.android.ui.category

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smh.movie.android.data.model.MovieUiModel
import com.smh.movie.android.ui.components.Loading
import com.smh.movie.android.ui.components.TopBar
import com.smh.movie.android.ui.components.loadingSection
import com.smh.movie.android.ui.components.moviePagingSection
import com.smh.movie.android.ui.theme.MovieTheme
import org.koin.androidx.compose.koinViewModel

enum class CategoryType(val title: String) {
    POPULAR(title = "Popular Movies"),
    TOP_RATED(title = "TopRated Movies");

    companion object {
        fun fromName(name: String) = entries.find { it.name == name } ?: POPULAR
    }
}

@Composable
fun CategoryScreen(
    categoryType: CategoryType,
    onBack: () -> Unit,
    viewModel: CategoryViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = categoryType) {
        viewModel.fetchMovies(categoryType)
    }

    CategoryContent(
        type = categoryType,
        uiState = uiState,
        onFetchMore = viewModel::fetchMore,
        onBack = onBack,
        onFavourite = viewModel::toggleFavourite
    )
}

@Composable
private fun CategoryContent(
    type:CategoryType,
    uiState: CategoryUiState,
    onFetchMore: () -> Unit,
    onBack: () -> Unit,
    onFavourite: (MovieUiModel) -> Unit
) {
    if (uiState.loading) Loading()

    Scaffold(
        topBar = {
            TopBar(
                title = type.title,
                onBack = onBack
            )
        }
    ) {
        LazyVerticalGrid(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .animateContentSize(),
            columns = GridCells.Adaptive(150.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            moviePagingSection(
                list = uiState.movies,
                isLoading = uiState.loadingMore,
                onFetchMore = onFetchMore,
                onClickFavourite = onFavourite
            )

            loadingSection(
                isLoading = uiState.loadingMore && uiState.movies.isNotEmpty()
            )
        }
    }
}

@Preview(uiMode = UI_MODE_NIGHT_NO)
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun CategoryPreview() {
    MovieTheme {
        CategoryContent(
            type = CategoryType.POPULAR,
            uiState = CategoryUiState(
                movies = listOf(MovieUiModel.example),
            ),
            onFetchMore = {},
            onBack = {},
            onFavourite = {}
        )
    }
}