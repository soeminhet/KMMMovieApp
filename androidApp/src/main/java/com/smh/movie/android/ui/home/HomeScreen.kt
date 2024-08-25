package com.smh.movie.android.ui.home

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smh.movie.domain.model.MovieUiModel
import com.smh.movie.android.ui.category.CategoryType
import com.smh.movie.android.ui.components.Loading
import com.smh.movie.android.ui.components.loadingSection
import com.smh.movie.android.ui.components.movieCarouselSection
import com.smh.movie.android.ui.components.movieHorizontalSection
import com.smh.movie.android.ui.components.moviePagingSection
import com.smh.movie.android.ui.components.movieSectionHeader
import com.smh.movie.android.ui.theme.MovieTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    goToFavourite: () -> Unit,
    goToCategory: (CategoryType) -> Unit,
    viewModel: HomeViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    if(uiState.isLoading) Loading()

    HomeContent(
        uiState = uiState,
        uiEvent = {
            when(it) {
                HomeUiEvent.FetchMoreUpcomingMovies -> viewModel.fetchMoreUpcomingMovies()
                is HomeUiEvent.OnClickFavourite -> viewModel.toggleFavourite(it.movie)
                HomeUiEvent.ToFavourite -> goToFavourite()
                is HomeUiEvent.ToCategory -> goToCategory(it.type)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeContent(
    uiState: HomeUiState,
    uiEvent: (HomeUiEvent) -> Unit,
) {
    val pagerState = rememberPagerState(pageCount = { uiState.nowPlayingMovies.size })

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Welcome Back",
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                actions = {
                    IconButton(onClick = { uiEvent(HomeUiEvent.ToFavourite) }) {
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = "Favourite"
                        )
                    }
                }
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
            contentPadding = PaddingValues(all = 16.dp)
        ) {
            movieCarouselSection(
                list = uiState.nowPlayingMovies,
                pagerState = pagerState
            )

            movieSectionHeader(
                title = "TopRated",
                modifier = Modifier.padding(bottom = 2.dp),
                show = uiState.topRatedMovies.isNotEmpty(),
                onViewMoreClick = { uiEvent(HomeUiEvent.ToCategory(CategoryType.TOP_RATED)) }
            )

            movieHorizontalSection(
                list = uiState.topRatedMovies,
                modifier = Modifier.padding(bottom = 18.dp),
                onClickFavourite = { uiModel -> uiEvent(HomeUiEvent.OnClickFavourite(uiModel)) },
            )

            movieSectionHeader(
                title = "Popular",
                modifier = Modifier.padding(bottom = 2.dp),
                show = uiState.popularMovies.isNotEmpty(),
                onViewMoreClick = { uiEvent(HomeUiEvent.ToCategory(CategoryType.POPULAR)) }
            )

            movieHorizontalSection(
                list = uiState.popularMovies,
                modifier = Modifier.padding(bottom = 28.dp),
                onClickFavourite = { uiModel -> uiEvent(HomeUiEvent.OnClickFavourite(uiModel)) },
            )

            movieSectionHeader(
                title = "Upcoming",
                show = uiState.upcomingMovies.isNotEmpty(),
                modifier = Modifier.padding(bottom = 2.dp),
            )

            moviePagingSection(
                list = uiState.upcomingMovies,
                isLoading = uiState.isMoreLoadingUpcomingMovies,
                onFetchMore = { uiEvent(HomeUiEvent.FetchMoreUpcomingMovies) },
                onClickFavourite = { uiModel -> uiEvent(HomeUiEvent.OnClickFavourite(uiModel)) }
            )

            loadingSection(
                isLoading = uiState.isMoreLoadingUpcomingMovies && uiState.upcomingMovies.isNotEmpty()
            )
        }
    }
}

@Preview(uiMode = UI_MODE_NIGHT_NO)
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun HomeScreenPreview() {
    val list = listOf(
        MovieUiModel.example,
        MovieUiModel.example,
        MovieUiModel.example
    )
    MovieTheme {
        HomeContent(
            uiState = HomeUiState(
                nowPlayingMovies = list,
                topRatedMovies = list,
                popularMovies = list,
                upcomingMovies = list,
                isMoreLoadingUpcomingMovies = false
            ),
            uiEvent = {},
        )
    }
}

private sealed interface HomeUiEvent {
    data object FetchMoreUpcomingMovies: HomeUiEvent
    data class OnClickFavourite(val movie: MovieUiModel): HomeUiEvent
    data object ToFavourite: HomeUiEvent
    data class ToCategory(val type: CategoryType): HomeUiEvent
}