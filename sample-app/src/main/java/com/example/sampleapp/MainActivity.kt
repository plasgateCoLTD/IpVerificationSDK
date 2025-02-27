package com.example.sampleapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.sampleapp.databinding.ActivityMainBinding
import com.plasgate.core.IPVerificationClient
import com.plasgate.model.CheckUserResponse
import com.plasgate.model.VerificationResponse
import com.plasgate.security.ValidationUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var clientBuilder: IPVerificationClient.Builder

    companion object {
        private const val TAG = "MainActivity"
        private const val BASE_URL = "https://stageapi.plasgate.com/ip/"
        private const val REDIRECT_URI = "https://webhook-test.com/8ede25a856ec101dd3278c2b7fe7ffea"
        private const val SECRET_KEY = "\$5\$rounds=535000\$clBgiBoC6o56qlIZ\$iebb/pFoOfuJTm7Evlb0F5J2F/qmtFxvlE9RsULyVt1"
        private const val PRIVATE_KEY = "Lxtso_Rhcn-s3NH6W38xJgr4tjxrBjk73632A2pFrVrmXtWLEnnrBatJHAC_Hk7DdPbJ6seGIWp777wKc5K5-w"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClientBuilder()
        handleDeepLink(intent)

        binding.loginButton.setOnClickListener {
            val phoneNumberInput = binding.phoneNumberInput.text.toString().trim()

            Log.d(TAG, "User input phone number: $phoneNumberInput")

            if (ValidationUtil.isValidPhoneNumber(phoneNumberInput)) {
                Log.i(TAG, "Phone number passed validation: $phoneNumberInput")
                requestVerification(phoneNumberInput)
            } else {
                Log.w(TAG, "Invalid phone number format detected.")
                binding.phoneNumberInput.error = "Invalid phone number format. Use E.164 format (e.g., +855972432661)."
            }
        }
    }

    private fun setupClientBuilder() {
        clientBuilder = IPVerificationClient.Builder()
            .setPrivateKey(PRIVATE_KEY)
            .setSecretKey(SECRET_KEY)
            .setRedirectUri(REDIRECT_URI)
            .setBaseUrl(BASE_URL)
            .enableLogging(true)
    }

    private fun requestVerification(phoneNumberInput: String) {
        try {
            val verificationHttpClient = clientBuilder.build()

            verificationHttpClient.requestVerification(REDIRECT_URI, phoneNumberInput)
                .enqueue(object : Callback<VerificationResponse> {
                    override fun onResponse(
                        call: Call<VerificationResponse>,
                        response: Response<VerificationResponse>
                    ) {
                        if (response.isSuccessful) {
                            val verificationResponse = response.body()
                            Log.i(TAG, "Request successful: $verificationResponse")
                        } else {
                            Log.e(TAG, "Request failed with code: ${response.code()}")
                            Log.e(TAG, "Error Body: ${response.errorBody()?.string()}")
                            showError("Failed to send verification request. Please try again.")
                        }
                    }

                    override fun onFailure(call: Call<VerificationResponse>, t: Throwable) {
                        Log.e(TAG, "Request failed: ${t.message}", t)
                        showError("Network error occurred. Please check your connection.")
                    }
                })

        } catch (e: IllegalArgumentException) {
            Log.e(TAG, "Exception in requestVerification: ${e.message}", e)
            binding.phoneNumberInput.error = "Invalid phone number format. Please use the correct format."
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleDeepLink(intent)
    }

    private fun handleDeepLink(intent: Intent?) {
        intent?.data?.let { uri: Uri ->
            Log.i(TAG, "Received deep link: $uri")

            for (key in uri.queryParameterNames) {
                Log.i(TAG, "Param: $key = ${uri.getQueryParameter(key)}")
            }

            val state = uri.getQueryParameter("state")
            val sessionState = uri.getQueryParameter("session_state")
            val code = uri.getQueryParameter("code")

            if (!code.isNullOrEmpty() && !sessionState.isNullOrEmpty() && !state.isNullOrEmpty()) {
                val verificationHttpClient = clientBuilder.build()

                verificationHttpClient.requestCheckInfo(state, code)
                    .enqueue(object : Callback<CheckUserResponse> {
                        override fun onResponse(call: Call<CheckUserResponse>, response: Response<CheckUserResponse>) {
                            if (response.isSuccessful) {
                                val checkUserResponse = response.body()
                                Log.i(TAG, "Check Info Request Successful: $checkUserResponse")
                            } else {
                                Log.e(TAG, "Check Info Request Failed. Code: ${response.code()}")
                                Log.e(TAG, "Error Body: ${response.errorBody()?.string()}")
                                showError("Failed to verify user info. Please try again.")
                            }
                        }

                        override fun onFailure(call: Call<CheckUserResponse>, t: Throwable) {
                            Log.e(TAG, "Check Info Request Failed: ${t.message}", t)
                            showError("Network error occurred. Please check your connection.")
                        }
                    })
                return
            }

            val error = uri.getQueryParameter("error")
            val errorDescription = uri.getQueryParameter("error_description")

            if (!error.isNullOrEmpty()) {
                val errorMessage = "Error: $error\nDescription: $errorDescription"
                Log.e(TAG, "Verification failed: $errorMessage")
                showError(errorMessage)
                return
            }

            Log.e(TAG, "Unknown response received: $uri")
            showError("Unknown response received. Please try again.")
        }
    }

    private fun showError(message: String) {
        Log.e(TAG, "Error: $message")
        binding.phoneNumberInput.error = message
        binding.phoneNumberInput.requestFocus()
    }
}
