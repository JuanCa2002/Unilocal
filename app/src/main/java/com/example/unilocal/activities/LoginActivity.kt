package com.example.unilocal.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast

import com.example.unilocal.R
import com.example.unilocal.bd.Persons
import com.example.unilocal.bd.Usuarios
import com.example.unilocal.databinding.ActivityLoginBinding
import com.example.unilocal.models.Moderator
import com.example.unilocal.models.User
import java.lang.Exception

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sp = getSharedPreferences("sesion",Context.MODE_PRIVATE)

        val email = sp.getString("correo_usuario","")
        val type = sp.getString("tipo_usuario","")

        if(email!!.isNotEmpty() && type!!.isNotEmpty()){
            when(type){
                "Usuario" -> startActivity( Intent(this, MainActivity::class.java) )
                "Moderador"-> startActivity( Intent(this, ModeratorActivity::class.java) )
            }
            finish()

        }else{
            binding = ActivityLoginBinding.inflate(layoutInflater)
            setContentView(binding.root)
            binding.btnLogin.setOnClickListener { login() }
            binding.btnRegistro.setOnClickListener{ registrar() }
        }

    }

    fun login() {

        val correo = binding.emailUsuario.text
        val password = binding.passwordUsuario.text
        if(correo.isEmpty()){
            binding.emailLayout.error = getString(R.string.txt_obligatorio)
        }
        else{
            binding.emailLayout.error = null
        }

        if(password.isEmpty()){
            binding.passwordLayout.error = getString(R.string.txt_obligatorio)
        }
        else{
            binding.passwordLayout.error = null
        }
        if (correo.isNotEmpty() && password.isNotEmpty()) {
            try {
                val persona = Persons.login(correo.toString(), password.toString())

                if(persona !=null){

                    val type = if(persona is User) "Usuario" else if(persona is Moderator) "Moderador" else "Administrador"

                    val sharedPreferences= this.getSharedPreferences("sesion",Context.MODE_PRIVATE).edit()
                    sharedPreferences.putString("correo_usuario", persona.correo)
                    sharedPreferences.putString("tipo_usuario",type)

                    sharedPreferences.commit()

                    when(persona){
                        is User -> startActivity( Intent(this, MainActivity::class.java) )
                        is Moderator -> startActivity( Intent(this, ModeratorActivity::class.java) )
                    }
                }else{
                    Log.d(MainActivity::class.java.simpleName, "los datos son erroneos")
                }

            } catch (e: Exception) {
                Log.d(MainActivity::class.java.simpleName, "los datos son erroneos")
            }

        }
    }


    fun registrar() {
        val intent = Intent(this, RegistroActivity::class.java)
        startActivity(intent)
    }



}