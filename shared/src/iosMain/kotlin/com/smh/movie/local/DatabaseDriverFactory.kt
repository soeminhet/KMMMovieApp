package com.smh.movie.local

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.smh.movie.data.local.DatabaseDriverFactory
import com.smh.movie.database.MoviesDB

class IOSDatabaseDriverFactory : DatabaseDriverFactory {
    override fun createDriver(): SqlDriver {
        return NativeSqliteDriver(
            schema = MoviesDB.Schema,
            name = "MyAppSQLDelightDatabase.db"
        )
    }
}