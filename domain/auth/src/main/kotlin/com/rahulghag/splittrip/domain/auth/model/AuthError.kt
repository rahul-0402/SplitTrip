package com.rahulghag.splittrip.domain.auth.model

sealed class AuthError {
    object InvalidCredentials : AuthError()
    object EmailAlreadyInUse : AuthError()
    object NetworkError : AuthError()
    data class Unknown(val message: String?) : AuthError()
}

fun AuthError.toUiMessage(): String = when (this) {
    is AuthError.InvalidCredentials -> "Invalid email or password"
    is AuthError.EmailAlreadyInUse -> "An account with this email already exists"
    is AuthError.NetworkError -> "No internet connection. Please try again."
    is AuthError.Unknown -> message ?: "Something went wrong"
}
