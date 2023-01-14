package br.com.fdtechcorp.android.guessgotnames.lib.network.module

import br.com.fdtechcorp.android.guessgotnames.lib.network.internal.RetrofitClient
import br.com.fdtechcorp.android.guessgotnames.lib.network.public.RestClient
import org.koin.dsl.module

val networkModule = module {
    single<RestClient> { RetrofitClient(networkConfiguration = get()) }
}