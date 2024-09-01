package com.example.coursesbuyingapp1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.coursesbuyingapp1.databinding.ActivityDataBinding


class DataActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDataBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = intent.getStringExtra("data")

        binding.data.text = data.toString()

    }
}