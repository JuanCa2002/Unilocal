package com.example.unilocal.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.unilocal.databinding.ActivityCrearModeradorBinding

class CrearModeradorActivity : AppCompatActivity() {
    lateinit var binding: ActivityCrearModeradorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrearModeradorBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


}