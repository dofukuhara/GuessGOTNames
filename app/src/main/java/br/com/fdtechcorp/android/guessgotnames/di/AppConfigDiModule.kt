package br.com.fdtechcorp.android.guessgotnames.di

import br.com.fdtechcorp.android.guessgotnames.BuildConfig
import br.com.fdtechcorp.android.guessgotnames.lib.network.config.NetworkConfiguration
import org.koin.dsl.module

private const val BASE_URL = "https://thronesapi.com/api/v2/"

val appConfigDiModule = module {
    factory { NetworkConfiguration(
        baseUrl = BASE_URL,
        isDebuggable = BuildConfig.DEBUG
    ) }
}