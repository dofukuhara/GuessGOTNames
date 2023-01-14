package br.com.fdtechcorp.android.guessgotnames.module

import br.com.fdtechcorp.android.guessgotnames.BuildConfig
import br.com.fdtechcorp.android.guessgotnames.lib.network.config.NetworkConfiguration
import org.koin.dsl.module

private const val BASE_URL = "https://thronesapi.com/api/v2/"

val appConfigModule = module {
    factory { NetworkConfiguration(
        baseUrl = BASE_URL,
        isDebuggable = BuildConfig.DEBUG
    ) }
}