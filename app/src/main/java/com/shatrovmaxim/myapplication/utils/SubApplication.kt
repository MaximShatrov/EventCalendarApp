package com.shatrovmaxim.myapplication.utils

import android.app.Application
import android.content.Context

/**
 * Класс, используемый для доступа к контексту приложения в репозитории.
 */
class SubApplication : Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: SubApplication = SubApplication()

        fun applicationContext(): Context {
            return instance.applicationContext
        }
    }

}