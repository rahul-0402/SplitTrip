package com.rahulghag.splittrip.core.common.model

// Platform-agnostic text wrapper
// Android layer resolves StringResource using Context
// KMP: iOS layer would resolve it differently
sealed class UiText {
    data class DynamicString(val value: String) : UiText()
    data class StringResource(
        val id: Int,
        val args: List<Any> = emptyList(),
    ) : UiText()

    companion object {
        fun dynamic(value: String): UiText = DynamicString(value)
        fun resource(id: Int, vararg args: Any): UiText =
            StringResource(id, args.toList())
    }
}
