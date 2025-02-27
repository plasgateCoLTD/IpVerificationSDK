package com.plasgate.security

import android.util.Log

object ValidationUtil {
    private const val TAG = "ValidationUtil"  // ✅ Allowed directly in 'object'

    fun isValidPhoneNumber(phoneNumber: String): Boolean {
        Log.d(TAG, "Validating phone number: $phoneNumber")

        val isValid = phoneNumber.matches(Regex("^\\+?[1-9]\\d{1,14}\$"))

        if (isValid) {
            Log.i(TAG, "Phone number is valid: $phoneNumber")
        } else {
            Log.w(TAG, "Invalid phone number format: $phoneNumber")
        }

        return isValid
    }
}
