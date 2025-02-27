package com.example.sampleapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sampleapp.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set a welcome message using string resource
        binding.welcomeMessage.text = getString(R.string.welcome_message)
    }
}
