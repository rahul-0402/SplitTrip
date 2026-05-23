package com.rahulghag.splittrip.data.auth

import com.rahulghag.splittrip.core.common.result.Result
import com.rahulghag.splittrip.domain.auth.repository.SessionRepository

class MockSessionRepository : SessionRepository {

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
