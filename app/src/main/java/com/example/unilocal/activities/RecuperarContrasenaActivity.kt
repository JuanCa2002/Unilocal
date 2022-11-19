package com.example.unilocal.activities

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AlertDialog
import com.example.unilocal.R
import com.example.unilocal.databinding.ActivityRecuperarContrasenaBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class RecuperarContrasenaActivity : AppCompatActivity() {
    lateinit var binding: ActivityRecuperarContrasenaBinding
    lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecuperarContrasenaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnEnviarCorreo.setOnClickListener { getBackPassword() }
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setView(R.layout.dialogo_progreso)
        dialog = builder.create()
    }

    fun getBackPassword(){
        setDialog(true)
        val correo = binding.email.text.toString()
        FirebaseAuth.getInstance()
            .sendPasswordResetEmail(correo)
            .addOnSuccessListener {
                Snackbar.make(binding.root,getString(R.string.cooreo_enviado_email), Snackbar.LENGTH_LONG).show()
                setDialog(false)
                Handler(Looper.getMainLooper()).postDelayed({
                    finish()
                },4000)
            }
    }

    private fun setDialog(show: Boolean) {
        if (show) dialog.show() else dialog.dismiss()
    }

}