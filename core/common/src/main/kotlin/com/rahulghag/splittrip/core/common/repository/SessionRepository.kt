package com.rahulghag.splittrip.core.common.repository

import com.rahulghag.splittrip.core.common.result.Result

interface SessionRepository {
    suspend fun signInWithEmail(email: String, password: String): Result<Unit>
    suspend fun signUpWithEmail(email: String, password: String): Result<Unit>
    suspend fun sendPasswordResetEmail(email: String): Result<Unit>
    suspend fun signOut(): Result<Unit>
    fun isLoggedIn(): Boolean
    fun getCurrentUserId(): String?
}
