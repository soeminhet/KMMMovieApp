package com.smh.movie.android.ui.favourite

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smh.movie.domain.model.MovieUiModel
import com.smh.movie.android.ui.components.MovieItem
import com.smh.movie.android.ui.components.TopBar
import com.smh.movie.android.ui.theme.MovieTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavouriteScreen(
    onBack: () -> Unit,
    viewModel: FavouriteViewModel = koinViewModel()
) {
    val favouriteMovies by viewModel.favouriteMovies.collectAsState()

    FavouriteContent(
        favouriteMovies = favouriteMovies,
        uiEvent = {
            when(it) {
                FavouriteUiEvent.OnBack -> onBack()
                is FavouriteUiEvent.OnClickFavourite -> viewModel.removeFavourite(it.movie)
            }
        }
    )
}

@Composable
private fun FavouriteContent(
    favouriteMovies: List<MovieUiModel>,
    uiEvent: (FavouriteUiEvent) -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(
                title = "Favourite",
                onBack = { uiEvent(FavouriteUiEvent.OnBack) }
            )
        }
    ) {
        LazyVerticalGrid(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            columns = GridCells.Adaptive(150.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
        ) {
            items(
                favouriteMovies,
                key = { movie -> movie.id }
            ) { movie ->
                MovieItem(
                    data = movie,
                    onClickFavourite = { data ->
                        uiEvent(FavouriteUiEvent.OnClickFavourite(data))
                    },
                    modifier = Modifier.animateItem()
                )
            }
        }
    }
}

sealed interface FavouriteUiEvent {
    data object OnBack: FavouriteUiEvent
    data class OnClickFavourite(val movie: MovieUiModel): FavouriteUiEvent
}

@Preview(uiMode = UI_MODE_NIGHT_NO)
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun FavouriteScreenPreview() {
    MovieTheme {
        FavouriteContent(
            favouriteMovies = listOf(MovieUiModel.example),
            uiEvent = {}
        )
    }
}