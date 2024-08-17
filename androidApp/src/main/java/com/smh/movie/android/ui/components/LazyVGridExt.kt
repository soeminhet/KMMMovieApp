package com.smh.movie.android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEachIndexed
import androidx.compose.ui.util.lerp
import com.smh.movie.android.data.model.MovieUiModel
import com.smh.movie.android.ui.theme.LightGray
import com.smh.movie.android.ui.theme.Orange
import com.smh.movie.android.ui.theme.White
import kotlin.math.absoluteValue

fun LazyGridScope.movieSectionHeader(
    modifier: Modifier = Modifier,
    title: String,
    show: Boolean,
    onViewMoreClick: (() -> Unit)? = null
) {
    if (show) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            Row(
                modifier = modifier
                    .expandMaxWidth(32.dp)
                    .fillMaxWidth()
                    .padding(start = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = if (onViewMoreClick == null) 8.dp else 0.dp)
                )

                onViewMoreClick?.let {
                    IconButton(onClick = onViewMoreClick) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = "ViewMore"
                        )
                    }
                }
            }
        }
    }
}

fun LazyGridScope.movieHorizontalSection(
    modifier: Modifier = Modifier,
    list: List<MovieUiModel>,
    onClickFavourite: (MovieUiModel) -> Unit,
) {
    item(span = { GridItemSpan(maxLineSpan) }) {
        Row(
            modifier = modifier
                .expandMaxWidth(horizontal = 32.dp)
                .fillMaxWidth()
                .horizontalScroll(state = rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            list.fastForEachIndexed { index, movie ->
                MovieItem(
                    data = movie,
                    modifier = Modifier
                        .padding(start = if (index == 0) 16.dp else 0.dp)
                        .width(150.dp),
                    onClickFavourite = onClickFavourite,
                )
            }
        }
    }
}

fun LazyGridScope.movieCarouselSection(
    modifier: Modifier = Modifier,
    list: List<MovieUiModel>,
    pagerState: PagerState
) {
    if(list.isNotEmpty()) {
        item(
            span = { GridItemSpan(maxLineSpan) }
        ) {
            Column(modifier = modifier) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .expandMaxWidth(32.dp)
                        .fillMaxWidth()
                        .aspectRatio(2f / 1f),
                    contentPadding = PaddingValues(horizontal = 30.dp),
                ) { page ->
                    val movie = list[page]
                    Box {
                        ImageLoader(
                            model = movie.backdropUrl,
                            contentDescription = movie.description,
                            modifier = Modifier
                                .graphicsLayer {
                                    val pageOffset =
                                        ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).absoluteValue
                                    alpha = lerp(
                                        start = 0.5f,
                                        stop = 1f,
                                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                    )
                                    scaleY = if (pagerState.currentPage == page) 1f else 0.9f
                                    scaleX = if (pagerState.currentPage == page) 1f else 0.9f
                                }
                                .clip(RoundedCornerShape(size = 20.dp))
                                .fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )

                        Text(
                            text = movie.title,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            color = White,
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(pagerState.pageCount) { iteration ->
                        val color = if (pagerState.currentPage == iteration) Orange else LightGray
                        Box(
                            modifier = Modifier
                                .padding(2.dp)
                                .clip(CircleShape)
                                .background(color)
                                .size(6.dp)
                        )
                    }
                }
            }
        }
    }
}

fun LazyGridScope.moviePagingSection(
    modifier: Modifier = Modifier,
    list: List<MovieUiModel>,
    isLoading: Boolean,
    onFetchMore: () -> Unit,
    onClickFavourite: (MovieUiModel) -> Unit
) {
    itemsIndexed(
        items = list,
        key = { _, movie -> movie.id }
    ) { index, movie ->
        MovieItem(
            data = movie,
            modifier = modifier.padding(bottom = 20.dp),
            onClickFavourite = onClickFavourite
        )

        if (index == list.lastIndex && !isLoading) {
            LaunchedEffect(key1 = Unit) {
                onFetchMore()
            }
        }
    }
}

fun LazyGridScope.loadingSection(
    modifier: Modifier = Modifier,
    isLoading: Boolean
) {
    item(span = { GridItemSpan(maxLineSpan) }) {
        if (isLoading) {
            Box(
                modifier = modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                LinearProgressIndicator(
                    color = Orange,
                    trackColor = Orange.copy(alpha = 0.3f),
                    modifier = Modifier
                        .width(100.dp)
                        .padding(vertical = 16.dp),
                    strokeCap = StrokeCap.Round
                )
            }
        }
    }
}