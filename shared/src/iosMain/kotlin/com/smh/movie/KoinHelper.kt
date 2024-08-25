package com.smh.movie

import com.smh.movie.data.local.DatabaseDriverFactory
import com.smh.movie.data.local.datasource.FavouriteMovieDataSourceImpl
import com.smh.movie.data.remote.datasource.MovieRemoteDataSourceImpl
import com.smh.movie.data.remote.service.MovieService
import com.smh.movie.data.repository.MovieRepositoryImpl
import com.smh.movie.database.MoviesDB
import com.smh.movie.di.getSharedModules
import com.smh.movie.domain.datasource.FavouriteMovieDataSource
import com.smh.movie.domain.datasource.MovieRemoteDataSource
import com.smh.movie.domain.repository.MovieRepository
import com.smh.movie.local.IOSDatabaseDriverFactory
import com.smh.movie.utility.provideDispatcher
import org.koin.core.context.startKoin
import org.koin.dsl.module

private val appModule = module {
    factory<DatabaseDriverFactory> { IOSDatabaseDriverFactory() }
}

fun initKoin(){
    startKoin {
        modules(appModule + getSharedModules())
    }
}