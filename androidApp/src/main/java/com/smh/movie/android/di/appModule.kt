package com.smh.movie.android.di

import com.smh.movie.android.ui.category.CategoryViewModel
import com.smh.movie.android.ui.favourite.FavouriteViewModel
import com.smh.movie.android.ui.home.HomeViewModel
import com.smh.movie.data.local.DatabaseDriverFactory
import com.smh.movie.di.getSharedModules
import com.smh.movie.local.AndroidDatabaseDriverFactory
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val databaseModule = module {
    factory<DatabaseDriverFactory> { AndroidDatabaseDriverFactory(get()) }
}

val viewModelModule = module {
    viewModel { HomeViewModel(get(), get(), get(), get(), get(), get(), get()) }
    viewModel { FavouriteViewModel(get(), get()) }
    viewModel { CategoryViewModel(get(), get(), get(), get(), get()) }
}

val appModule = databaseModule + viewModelModule + getSharedModules()