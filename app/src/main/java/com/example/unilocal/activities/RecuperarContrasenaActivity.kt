package com.example.unilocal.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.unilocal.R

import com.example.unilocal.databinding.ActivityRecuperarContrasenaBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class RecuperarContrasenaActivity : AppCompatActivity() {
    lateinit var binding: ActivityRecuperarContrasenaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecuperarContrasenaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnEnviarCorreo.setOnClickListener { getBackPassword() }

    }


    fun getBackPassword(){
        val correo = binding.email.text.toString()
        FirebaseAuth.getInstance()
            .sendPasswordResetEmail(correo)
            .addOnSuccessListener {
                Snackbar.make(binding.root,"El correo ha sido enviado, revisa tu bandeja de mensajes", Snackbar.LENGTH_LONG).show()
                Handler(Looper.getMainLooper()).postDelayed({
                    finish()
                },4000)
            }
    }
}