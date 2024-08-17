package com.smh.movie.android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smh.movie.android.data.model.MovieUiModel
import com.smh.movie.android.ui.theme.Green
import com.smh.movie.android.ui.theme.MovieTheme
import com.smh.movie.android.ui.theme.NavyBlue
import com.smh.movie.android.ui.theme.Orange
import com.smh.movie.android.ui.theme.Red
import com.smh.movie.android.ui.theme.White
import com.smh.movie.domain.model.VoteAverage

@Composable
fun MovieItem(
    modifier: Modifier = Modifier,
    data: MovieUiModel,
    onClickFavourite: (MovieUiModel) -> Unit
) {
    Column(modifier = modifier) {
        Box {
            ImageLoader(
                model = data.imageUrl,
                contentDescription = data.title,
                modifier = Modifier
                    .clip(RoundedCornerShape(size = 20.dp))
                    .fillMaxWidth()
                    .aspectRatio(2f / 3f),
                contentScale = ContentScale.Crop,
            )

            IconButton(
                onClick = { onClickFavourite(data) },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 2.dp, end = 2.dp)
            ) {
                Icon(
                    imageVector = if (data.isFavourite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favourite",
                    tint = Red
                )
            }
        }

        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
        ) {
            MovieVoting(
                data = data,
                modifier = Modifier
                    .offset(y = (-16).dp)
            )

            Text(
                text = data.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .offset(y = (-8).dp)
            )

            Text(
                text = data.releaseDate,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.offset(y = (-8).dp)
            )
        }
    }
}

@Composable
private fun MovieVoting(
    modifier: Modifier = Modifier,
    data: MovieUiModel
) {
    val color = remember {
        when (data.voteAverage) {
            VoteAverage.High -> Green
            VoteAverage.Medium -> Orange
            VoteAverage.Low -> Red
        }
    }

    Box(
        modifier = modifier
    ) {
        CircularProgressIndicator(
            progress = data.votePercentageFloat,
            trackColor = color.copy(alpha = 0.3f),
            color = color,
            strokeWidth = 4.dp,
            modifier = Modifier.size(50.dp)
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .padding(4.dp)
                .background(NavyBlue, shape = CircleShape)
        )

        Text(
            text = data.votePercentageString,
            color = White,
            modifier = Modifier.align(Alignment.Center),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MovieItemPreview() {
    MovieTheme {
        Surface {
            MovieItem(
                data = MovieUiModel.example,
                onClickFavourite = {}
            )
        }
    }
}