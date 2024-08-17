package com.smh.movie.android

import android.app.Application
import com.smh.movie.android.di.appModule
import com.smh.movie.di.getSharedModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(appModule + getSharedModules())
        }
    }
}