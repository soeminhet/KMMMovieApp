package com.smh.movie.data.local.datasource

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.smh.movie.data.mapper.toModel
import com.smh.movie.database.MoviesDB
import com.smh.movie.domain.datasource.FavouriteMovieDataSource
import com.smh.movie.domain.model.FavouriteMovieModel
import comsmhmoviedatabase.FavouriteMovieEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class FavouriteMovieDataSourceImpl(db: MoviesDB) : FavouriteMovieDataSource {

    private val queries = db.favouriteMoviesQueries

    override suspend fun insertFavouriteMovie(movie: FavouriteMovieModel) {
        queries.insertFavourite(
            id = movie.id,
            title = movie.title,
            description = movie.description,
            imageUrl = movie.imageUrl,
            releaseDate = movie.releaseDate,
            backdropUrl = movie.backdropUrl,
            votePercentage = movie.votePercentage.toLong()
        )
    }

    override fun getAllFavourites(): Flow<List<FavouriteMovieModel>> {
        return queries.getAllFavourites().asFlow()
            .mapToList(Dispatchers.IO)
            .map { it.map(FavouriteMovieEntity::toModel) }
    }

    override suspend fun deleteFavouriteMovie(id: Long) {
        queries.deleteFavourite(id)
    }
}