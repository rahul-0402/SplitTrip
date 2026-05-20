package com.rahulghag.splittrip.core.common.extensions

// No android.util.Patterns — pure Kotlin regex (KMP safe)
fun String.isValidEmail(): Boolean {
    val emailRegex = Regex(
        "^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$"
    )
    return emailRegex.matches(this.trim())
}

// "Rahul Ghag" → "RG"
// "Komal"        → "K"
fun String.initials(): String =
    trim()
        .split(" ")
        .filter { it.isNotEmpty() }
        .take(2)
        .joinToString("") { it.first().uppercase() }

// Generates UPI deep link
// Opens Google Pay, PhonePe, Paytm etc directly
fun String.toUpiDeepLink(
    name: String,
    amount: Double,
    note: String = "SplitTrip",
): String = "upi://pay?pa=$this&pn=$name&am=$amount&cu=INR&tn=$note"
