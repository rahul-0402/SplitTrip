package com.rahulghag.splittrip.core.di.mock

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AssetReader @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    fun read(fileName: String): String =
        context.assets
            .open("mock/$fileName")
            .bufferedReader()
            .use { it.readText() }
}
