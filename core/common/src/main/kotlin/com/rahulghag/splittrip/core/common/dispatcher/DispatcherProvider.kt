package com.rahulghag.splittrip.core.common.dispatcher

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface DispatcherProvider {
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
    val unconfined: CoroutineDispatcher
}

class DefaultDispatcherProvider : DispatcherProvider {
    override val main: CoroutineDispatcher      = Dispatchers.Main
    override val io: CoroutineDispatcher        = Dispatchers.IO
    override val default: CoroutineDispatcher   = Dispatchers.Default
    override val unconfined: CoroutineDispatcher = Dispatchers.Unconfined
}

// Use this in unit tests instead of DefaultDispatcherProvider
class TestDispatcherProvider(
    private val testDispatcher: CoroutineDispatcher,
) : DispatcherProvider {
    override val main        = testDispatcher
    override val io          = testDispatcher
    override val default     = testDispatcher
    override val unconfined  = testDispatcher
}
