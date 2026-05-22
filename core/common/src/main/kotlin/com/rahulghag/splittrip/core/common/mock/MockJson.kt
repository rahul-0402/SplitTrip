package com.rahulghag.splittrip.core.common.mock

import kotlinx.serialization.json.Json

val MockJson = Json {
    ignoreUnknownKeys = true
    isLenient = true
    coerceInputValues = true
}
