package com.rahulghag.splittrip.core.data.auth

import com.rahulghag.splittrip.core.common.repository.SessionRepository
import com.rahulghag.splittrip.core.common.result.Result
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockSessionRepository @Inject constructor() : SessionRepository {

    override suspend fun signInWithEmail(email: String, password: String): Result<Unit> =
        Result.Success(Unit)

    override suspend fun signUpWithEmail(email: String, password: String): Result<Unit> =
        Result.Success(Unit)

    override suspend fun sendPasswordResetEmail(email: String): Result<Unit> =
        Result.Success(Unit)

    override suspend fun signOut(): Result<Unit> =
        Result.Success(Unit)

    override fun isLoggedIn(): Boolean = false

    override fun getCurrentUserId(): String? = null
}
