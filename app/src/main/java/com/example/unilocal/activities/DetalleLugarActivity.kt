package com.example.unilocal.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.unilocal.R
import com.example.unilocal.bd.Places
import com.example.unilocal.databinding.ActivityDetalleLugarBinding

class DetalleLugarActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetalleLugarBinding
    var codePlace:Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleLugarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        codePlace = intent.extras!!.getInt("code")

        val place = Places.obtener(codePlace)

        binding.namePlace.text = place!!.name
        binding.txtDescipcionLugar.text = place!!.description
    }
}