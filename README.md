# IPVerification SDK Integration Guide

## Overview
The PlasGate IPVerification SDK allows developers to integrate IP-based phone number verification into Android applications. This guide will walk you through setting up, configuring, and using the SDK effectively.

---

## Prerequisites
- Android Studio (Arctic Fox or later)
- Android SDK 21+
- Kotlin support enabled
- Internet access for API communication
- Familiarity with Android development and Kotlin programming

---

## SDK Installation
1. **Add the Dependency**

```kotlin
dependencies {
    implementation("com.plasgate:ipverification-sdk:1.0.0")
}
```

2. **Sync Gradle**
- Ensure all dependencies are resolved by syncing the project in Android Studio.

---

## Setup and Configuration

### 1. Initialize the SDK

```kotlin
private lateinit var clientBuilder: IPVerificationClient.Builder

clientBuilder = IPVerificationClient.Builder()
    .setPrivateKey(PRIVATE_KEY)
    .setSecretKey(SECRET_KEY)
    .setRedirectUri(REDIRECT_URI)
    .setBaseUrl(BASE_URL)
    .enableLogging(true)
```

### 2. Required Parameters
- **PRIVATE_KEY**: Your API private key from PlasGate
- **SECRET_KEY**: The secret key associated with your PlasGate account
- **REDIRECT_URI**: URI to handle the verification callback
- **BASE_URL**: Endpoint for the PlasGate IP Verification API

```kotlin
companion object {
    private const val BASE_URL = "https://stageapi.plasgate.com/ip/"
    private const val REDIRECT_URI = "https://yourdomain.com/redirect"
    private const val SECRET_KEY = "your-secret-key"
    private const val PRIVATE_KEY = "your-private-key"
}
```

---

## Requesting Phone Number Verification

To initiate a verification request, use the `requestVerification` method:

```kotlin
verificationHttpClient.requestVerification(REDIRECT_URI, phoneNumber)
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
            }
        }

        override fun onFailure(call: Call<VerificationResponse>, t: Throwable) {
            Log.e(TAG, "Request failed: ${t.message}", t)
        }
    })
```

### Validation Utility
To validate phone numbers, use the built-in `ValidationUtil`:

```kotlin
if (ValidationUtil.isValidPhoneNumber(phoneNumberInput)) {
    requestVerification(phoneNumberInput)
} else {
    Log.w(TAG, "Invalid phone number format.")
}
```

---

## Handling Deep Links
The SDK supports deep linking to handle verification callbacks. Ensure your `AndroidManifest.xml` is configured properly with an `<intent-filter>`:

```xml
<intent-filter>
    <action android:name="android.intent.action.VIEW" />
    <category android:name="android.intent.category.DEFAULT" />
    <category android:name="android.intent.category.BROWSABLE" />
    <data android:scheme="https" android:host="yourdomain.com" />
</intent-filter>
```

```kotlin
override fun onNewIntent(intent: Intent?) {
    super.onNewIntent(intent)
    handleDeepLink(intent)
}

private fun handleDeepLink(intent: Intent?) {
    intent?.data?.let { uri: Uri ->
        val state = uri.getQueryParameter("state")
        val sessionState = uri.getQueryParameter("session_state")
        val code = uri.getQueryParameter("code")
        if (!code.isNullOrEmpty() && !sessionState.isNullOrEmpty() && !state.isNullOrEmpty()) {
            requestCheckInfo(state, code)
        }
    }
}
```

---

## Error Handling
Always implement robust error handling to manage API failures and unexpected scenarios:

```kotlin
private fun showError(message: String) {
    Log.e(TAG, "Error: $message")
    binding.phoneNumberInput.error = message
    binding.phoneNumberInput.requestFocus()
}
```

---

## Best Practices
- **Validation:** Always validate user inputs before sending a request.
- **Logging:** Enable SDK logging only in development environments.
- **Security:** Never expose API keys in the client code. Use secure vaults or environment variables.

---

## Testing Recommendations
- **Postman:** Test API requests separately before integrating.
- **Android Studio Testing:** Utilize built-in testing frameworks for unit and integration testing.
- **Network Interceptor:** Use OkHttp's logging interceptor during development to debug API calls.

---

## Troubleshooting
1. **Invalid Phone Number Error:** Ensure the phone number is in E.164 format.
2. **API Request Failed:** Check the internet connection and verify API credentials.
3. **Deep Link Not Triggered:** Verify the intent filter configuration in `AndroidManifest.xml`.

---

## Additional Resources
- [PlasGate API Documentation](https://plasgate.com)
- [SDK Source Code on GitHub](https://github.com/plasgateCoLTD/IpVerificationSDK)

---

## Support
For any issues, contact support at [support@plasgate.com](mailto:support@plasgate.com) or visit our [Support Center](https://plasgate.com/support).

