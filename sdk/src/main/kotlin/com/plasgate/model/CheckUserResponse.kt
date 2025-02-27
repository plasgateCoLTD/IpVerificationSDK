package com.plasgate.model

data class CheckUserResponse(
    val sub: String,
    val login_hint: String,
    val phone_number_verified: String,
    val mobile_id: String
)
