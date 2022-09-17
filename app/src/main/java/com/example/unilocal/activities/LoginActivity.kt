package com.example.unilocal.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast

import com.example.unilocal.R
import com.example.unilocal.bd.Usuarios
import com.example.unilocal.databinding.ActivityLoginBinding
import java.lang.Exception

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnLogin.setOnClickListener { login() }
        binding.btnRegistro.setOnClickListener{
           registrar()
        }
    }

    fun login() {

        val correo = binding.emailUsuario.text
        val password = binding.passwordUsuario.text
        if(correo.isEmpty()){
            binding.emailLayout.error = "Obligatorio"
        }
        else{
            binding.emailLayout.error = null
        }

        if(password.isEmpty()){
            binding.passwordLayout.error = "Obligatorio"
        }
        else{
            binding.passwordLayout.error = null
        }
        if (correo.isNotEmpty() && password.isNotEmpty()) {
            try {
                val usuario = Usuarios.login(correo.toString(), password.toString())
                Log.d(MainActivity::class.java.simpleName, "Informacion correcta")
            } catch (e: Exception) {
                Log.d(MainActivity::class.java.simpleName, "los datos son erroneos")
            }

        }
    }


    fun registrar() {
        val intent = Intent(this, RegistroActivity::class.java)
    }
    fun irADetalles(v:View){
        val intent = Intent(this, DetalleLugarActivity::class.java)

        startActivity(intent)
    }
}