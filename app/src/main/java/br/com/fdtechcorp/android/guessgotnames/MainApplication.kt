package br.com.fdtechcorp.android.guessgotnames

import android.app.Application
import br.com.fdtechcorp.android.guessgotnames.lib.network.module.networkModule
import br.com.fdtechcorp.android.guessgotnames.module.appConfigModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            if (BuildConfig.DEBUG) {
                androidLogger()
            }
            androidContext(this@MainApplication)
            modules(listOf(appConfigModule, networkModule))
        }
    }
}