package com.smh.movie.di

import com.smh.movie.data.local.datasource.FavouriteMovieDataSourceImpl
import com.smh.movie.data.local.DatabaseDriverFactory
import com.smh.movie.data.remote.datasource.MovieRemoteDataSourceImpl
import com.smh.movie.data.remote.service.MovieService
import com.smh.movie.data.repository.MovieRepositoryImpl
import com.smh.movie.database.MoviesDB
import com.smh.movie.domain.usecase.GetUpcomingMoviesUseCase
import com.smh.movie.domain.datasource.FavouriteMovieDataSource
import com.smh.movie.domain.datasource.MovieRemoteDataSource
import com.smh.movie.domain.repository.MovieRepository
import com.smh.movie.domain.usecase.DeleteFavouriteMovieUseCase
import com.smh.movie.domain.usecase.GetAllFavouriteMoviesUseCase
import com.smh.movie.domain.usecase.GetNowPlayingMoviesUseCase
import com.smh.movie.domain.usecase.GetPopularMoviesUseCase
import com.smh.movie.domain.usecase.GetPopularMoviesUseCaseIOS
import com.smh.movie.domain.usecase.GetTopRatedMoviesUseCase
import com.smh.movie.domain.usecase.InsertOrUpdateFavouriteMovieUseCase
import com.smh.movie.utility.provideDispatcher
import org.koin.dsl.module

private val dataModule = module {
    factory { MovieService() }
    factory<MovieRemoteDataSource> { MovieRemoteDataSourceImpl(get()) }
    factory<FavouriteMovieDataSource> { FavouriteMovieDataSourceImpl(get()) }
    factory { MoviesDB(get<DatabaseDriverFactory>().createDriver()) }
}

private val dispatcherModule = module {
    factory { provideDispatcher() }
}

private val domainModule = module {
    single<MovieRepository> { MovieRepositoryImpl(get(), get()) }
    factory { GetNowPlayingMoviesUseCase() }
    factory { GetPopularMoviesUseCase() }
    factory { GetTopRatedMoviesUseCase() }
    factory { GetUpcomingMoviesUseCase() }
    factory { InsertOrUpdateFavouriteMovieUseCase() }
    factory { GetAllFavouriteMoviesUseCase() }
    factory { DeleteFavouriteMovieUseCase() }

    factory { GetPopularMoviesUseCaseIOS() }
}

private val sharedModule = listOf(dataModule, dispatcherModule, domainModule)

fun getSharedModules() = sharedModule