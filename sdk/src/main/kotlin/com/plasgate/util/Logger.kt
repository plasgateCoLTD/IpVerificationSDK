package com.plasgate.util

object Logger {

    fun d(message: String) {
        println("DEBUG: $message")
    }

    fun e(message: String, throwable: Throwable? = null) {
        println("ERROR: $message")
        throwable?.printStackTrace()
    }
}
