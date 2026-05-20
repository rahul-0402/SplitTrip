package com.rahulghag.splittrip.core.common.result

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(
        val exception: Throwable? = null,
        val message: String? = null,
    ) : Result<Nothing>()
    data object Loading : Result<Nothing>()
}

fun <T> Result<T>.isSuccess() = this is Result.Success
fun <T> Result<T>.isError() = this is Result.Error
fun <T> Result<T>.isLoading() = this is Result.Loading

fun <T> Result<T>.getOrNull(): T? =
    if (this is Result.Success) data else null

fun <T> Result<T>.getOrDefault(default: T): T =
    if (this is Result.Success) data else default

fun <T, R> Result<T>.map(transform: (T) -> R): Result<R> = when (this) {
    is Result.Success -> Result.Success(transform(data))
    is Result.Error   -> Result.Error(exception, message)
    is Result.Loading -> Result.Loading
}

inline fun <T> Result<T>.onSuccess(action: (T) -> Unit): Result<T> {
    if (this is Result.Success) action(data)
    return this
}

inline fun <T> Result<T>.onError(
    action: (Throwable?, String?) -> Unit,
): Result<T> {
    if (this is Result.Error) action(exception, message)
    return this
}

inline fun <T> Result<T>.onLoading(action: () -> Unit): Result<T> {
    if (this is Result.Loading) action()
    return this
}
