package br.com.fdtechcorp.android.guessgotnames.lib.common.arch

sealed interface Either<out L, out R> {
    data class FAILURE<out L, Nothing>(val exception: L) : Either<L, Nothing>
    data class SUCCESS<Nothing, out R>(val data: R) : Either<Nothing, R>
}

fun <L> L.failure() = Either.FAILURE<L, Nothing>(this)
fun <R> R.success() = Either.SUCCESS<Nothing, R>(this)

fun <L,R> Either<L, R>.fold(failureHandler: (L) -> Unit, successHandler: (R) -> Unit) = when (this) {
    is Either.FAILURE -> failureHandler(this.exception)
    is Either.SUCCESS -> successHandler(this.data)
}