package br.com.fdtechcorp.android.guessgotnames.lib.network.di

import br.com.fdtechcorp.android.guessgotnames.lib.network.internal.RetrofitClient
import br.com.fdtechcorp.android.guessgotnames.lib.network.public.RestClient
import org.koin.dsl.module

val networkDiModule = module {
    single<RestClient> { RetrofitClient(networkConfiguration = get()) }
}
