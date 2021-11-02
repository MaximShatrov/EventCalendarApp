package com.shatrovmaxim.myapplication.utils

import android.app.Application
import android.content.Context

class SubApplication : Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: SubApplication? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        // initialize for any

        // Use ApplicationContext.
        // example: SharedPreferences etc...
        //val context: Context = applicationContext()
    }
}