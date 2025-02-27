package com.plasgate.core

import android.util.Log
import com.plasgate.model.CheckUserRequest
import com.plasgate.model.CheckUserResponse
import com.plasgate.model.VerificationResponse
import com.plasgate.network.ApiFactory
import com.plasgate.network.NetworkClient
import com.plasgate.security.ValidationUtil
import retrofit2.Call

class IPVerificationClient private constructor(
    private val privateKey: String,
    private val secretKey: String,
    private val scope: String,
    private val redirectUri: String,
    baseUrl: String,
    timeout: Long,
    enableLogging: Boolean
) {
    private val TAG = "IPVerificationClient"
    private val api = ApiFactory.createApi(NetworkClient.create(baseUrl, timeout, enableLogging))

    /**
     * Request phone number verification.
     */
    fun requestVerification(redirectUri: String, phoneNumber: String): Call<VerificationResponse> {
        Log.d(TAG, "Requesting verification for phone number: $phoneNumber")

        if (!ValidationUtil.isValidPhoneNumber(phoneNumber)) {
            Log.e(TAG, "Invalid phone number format: $phoneNumber")
            throw IllegalArgumentException("Invalid phone number format")
        }

        return api.requestVerification(privateKey, scope, redirectUri, phoneNumber)
    }

    /**
     * Request to check user info with the provided state and code.
     */
    fun requestCheckInfo(state: String, code: String, responseType: String = "code"): Call<CheckUserResponse> {
        Log.d(TAG, "Requesting check info for state: $state with code: $code")

        // ✅ Match the Python request structure and ensure correct header and body
        val request = CheckUserRequest(
            responseType = responseType,
            state = state,
            redirectUri = redirectUri,
            code = code
        )

        return api.requestCheckInfo(
            secretKey = secretKey,
            contentType = "application/json",
            privateKey = privateKey,
            request = request
        )
    }

    /**
     * Builder class for constructing IPVerificationClient instances.
     */
    class Builder {
        private var privateKey: String = ""
        private var secretKey: String = ""
        private var redirectUri: String = ""
        private var scope: String = "mobile_id phone_verify"
        private var baseUrl: String = "https://cloudapi.plasgate.com/ip/"
        private var timeout: Long = 30L
        private var enableLogging: Boolean = false

        fun setPrivateKey(privateKey: String) = apply { this.privateKey = privateKey }
        fun setSecretKey(secretKey: String) = apply { this.secretKey = secretKey }
        fun setRedirectUri(redirectUri: String) = apply { this.redirectUri = redirectUri }
        fun setBaseUrl(baseUrl: String) = apply { this.baseUrl = baseUrl }
        fun setTimeout(timeout: Long) = apply { this.timeout = timeout }
        fun enableLogging(enable: Boolean) = apply { this.enableLogging = enable }

        fun build(): IPVerificationClient {
            require(privateKey.isNotBlank()) { "Private key must not be empty" }
            require(secretKey.isNotBlank()) { "Secret key must not be empty" }
            require(redirectUri.isNotBlank()) { "Redirect URI must not be empty" }

            return IPVerificationClient(
                privateKey,
                secretKey,
                scope,
                redirectUri,
                baseUrl,
                timeout,
                enableLogging
            )
        }
    }
}
