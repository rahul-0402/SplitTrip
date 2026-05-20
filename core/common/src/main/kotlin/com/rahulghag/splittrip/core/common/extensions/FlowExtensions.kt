package com.rahulghag.splittrip.core.common.extensions

import com.rahulghag.splittrip.core.common.result.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

// Wraps any Flow<T> into Flow<Result<T>>
// Automatically catches exceptions and emits Result.Error
fun <T> Flow<T>.asResult(): Flow<Result<T>> =
    map<T, Result<T>> { Result.Success(it) }
        .catch { emit(Result.Error(it, it.message)) }
