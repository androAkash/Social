package com.akash.social

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.akash.social.databinding.ActivityPostBinding

class PostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Social)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}