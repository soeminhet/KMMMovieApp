package com.smh.movie.local

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.smh.movie.data.local.DatabaseDriverFactory
import com.smh.movie.database.MoviesDB

class AndroidDatabaseDriverFactory(private val context: Context): DatabaseDriverFactory {
    override fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            schema = MoviesDB.Schema,
            context = context,
            name = "MoviesDB.db"
        )
    }
}