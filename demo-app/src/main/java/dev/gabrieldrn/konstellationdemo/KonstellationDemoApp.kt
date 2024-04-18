package dev.gabrieldrn.konstellationdemo

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

/**
 * Application class for the demo app.
 */
class KonstellationDemoApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        startKoin {
            androidContext(this@KonstellationDemoApp)
            modules(appModule)
        }
    }
}
