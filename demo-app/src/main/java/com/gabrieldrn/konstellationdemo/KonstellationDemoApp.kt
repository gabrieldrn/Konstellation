package com.gabrieldrn.konstellationdemo

import android.app.Application
import org.koin.core.context.startKoin

class KonstellationDemoApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(appModule)
        }
    }
}
