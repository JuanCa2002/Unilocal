package com.example.unilocal.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.unilocal.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun irRegistro(view:View){
        val intent = Intent(this, RegistroActivity::class.java)
        startActivity(intent)
    }

    fun irLogin(view:View){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}