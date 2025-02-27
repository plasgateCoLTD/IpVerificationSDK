package com.plasgate.model

data class VerificationResponse(
    val sessionId: String,
    val status: String,
    val message: String
)