package br.com.fdtechcorp.android.guessgotnames

import android.app.Application
import br.com.fdtechcorp.android.guessgotnames.di.appConfigDiModule
import br.com.fdtechcorp.android.guessgotnames.lib.common.di.commonDiModule
import br.com.fdtechcorp.android.guessgotnames.lib.network.di.networkDiModule
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
            modules(listOf(appConfigDiModule, networkDiModule, commonDiModule))
        }
    }
}
