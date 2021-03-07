package com.example.vesputiapp

import android.app.Application
import com.example.vesputiapp.di.apiModule
import com.example.vesputiapp.di.itemModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App:Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(listOf(apiModule, itemModule))
        }
    }

}