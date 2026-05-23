package com.rahulghag.splittrip.core.data.auth

import com.rahulghag.splittrip.core.common.auth.AuthError
import com.rahulghag.splittrip.core.common.auth.toUiMessage
import com.rahulghag.splittrip.core.common.repository.SessionRepository
import com.rahulghag.splittrip.core.common.result.Result
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.exception.AuthRestException
import io.github.jan.supabase.auth.providers.builtin.Email
import io.ktor.client.plugins.HttpRequestTimeoutException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SupabaseSessionRepository @Inject constructor(
    private val supabase: SupabaseClient,
) : SessionRepository {

    override suspend fun signInWithEmail(email: String, password: String): Result<Unit> =
        safeAuthCall {
            supabase.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
        }

    override suspend fun signUpWithEmail(email: String, password: String): Result<Unit> =
        safeAuthCall {
            supabase.auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }
        }

    override suspend fun sendPasswordResetEmail(email: String): Result<Unit> =
        safeAuthCall {
            supabase.auth.resetPasswordForEmail(email)
        }

    override suspend fun signOut(): Result<Unit> =
        safeAuthCall {
            supabase.auth.signOut()
        }

    override fun isLoggedIn(): Boolean =
        supabase.auth.currentSessionOrNull() != null

    override fun getCurrentUserId(): String? =
        supabase.auth.currentUserOrNull()?.id

    private suspend fun <T> safeAuthCall(block: suspend () -> T): Result<T> {
        return try {
            Result.Success(block())
        } catch (e: HttpRequestTimeoutException) {
            Result.Error(message = AuthError.NetworkError.toUiMessage())
        } catch (e: AuthRestException) {
            val error = when {
                e.message?.contains("Invalid login credentials") == true ->
                    AuthError.InvalidCredentials
                e.message?.contains("User already registered") == true ->
                    AuthError.EmailAlreadyInUse
                else -> AuthError.Unknown(e.message)
            }
            Result.Error(message = error.toUiMessage())
        } catch (e: Exception) {
            Result.Error(message = AuthError.Unknown(e.message).toUiMessage())
        }
    }
}
