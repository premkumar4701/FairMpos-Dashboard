package utils

sealed class UseCaseResult<out R> {
    data class Success<out R>(val value: R) : UseCaseResult<R>()
    data class Error<out E>(val error: E) : UseCaseResult<E>()
}
