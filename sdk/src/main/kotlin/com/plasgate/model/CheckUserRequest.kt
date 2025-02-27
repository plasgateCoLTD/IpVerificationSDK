package com.plasgate.model

data class CheckUserRequest (
    val code: String,
    val redirectUri: String,
    val state: String,
    val responseType: String
)