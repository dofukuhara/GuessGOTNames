package br.com.fdtechcorp.android.guessgotnames.lib.common.network

interface ModelMapper<IN, OUT> {
    fun transform(voData: IN) : OUT
}