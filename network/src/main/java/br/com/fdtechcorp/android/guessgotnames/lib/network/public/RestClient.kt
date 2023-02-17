package br.com.fdtechcorp.android.guessgotnames.lib.network.public

interface RestClient {
    fun <T> getApi(service: Class<T>): T
}
