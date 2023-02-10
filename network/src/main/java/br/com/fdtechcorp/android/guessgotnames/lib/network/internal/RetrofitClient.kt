package br.com.fdtechcorp.android.guessgotnames.lib.network.internal

import br.com.fdtechcorp.android.guessgotnames.lib.network.config.NetworkConfiguration
import br.com.fdtechcorp.android.guessgotnames.lib.network.public.RestClient
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal class RetrofitClient(private val networkConfiguration: NetworkConfiguration) : RestClient {
    private val retrofitClient by lazy {
        Retrofit.Builder()
            .baseUrl(networkConfiguration.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .loggingInterceptor()
            .build()
    }

    private fun Retrofit.Builder.loggingInterceptor() : Retrofit.Builder {
        return if (networkConfiguration.isDebuggable) {
            val clientConfig = OkHttpClient.Builder().addInterceptor(
                HttpLoggingInterceptor().apply {
                    setLevel(HttpLoggingInterceptor.Level.BODY)
                }
            ).build()
            this.client(clientConfig)
        } else {
            this
        }
    }

    override fun <T> getApi(service: Class<T>): T = retrofitClient.create(service)
}