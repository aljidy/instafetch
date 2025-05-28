package com.adam.instafetch

import android.app.Application
import android.content.Context

class DogsApp : Application() {
    private lateinit var dogsModule: DogsModule

    companion object {
        fun from(applicationContext: Context): DogsModule {
            return (applicationContext as DogsApp).dogsModule
        }
    }

    override fun onCreate() {
        super.onCreate()
        dogsModule = DogsModule(applicationContext)
    }
}
