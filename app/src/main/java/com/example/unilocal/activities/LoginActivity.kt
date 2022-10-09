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
import com.example.unilocal.models.Administrator
import com.example.unilocal.models.Moderator
import com.example.unilocal.models.User
import com.google.android.material.snackbar.Snackbar
import java.lang.Exception

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sp = getSharedPreferences("sesion",Context.MODE_PRIVATE)

        val email = sp.getString("correo_usuario","")
        val type = sp.getString("tipo_usuario","")
        val code = sp.getInt("id",0)


        if(email!!.isNotEmpty() && type!!.isNotEmpty() && code!= null){
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("code",code)
            when(type){
                "Administrador" -> startActivity(Intent(this, GestionModeratorActivity::class.java))
                "Usuario" -> startActivity( intent )
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
                    sharedPreferences.putInt("id",persona.id)
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("code",persona.id)

                    sharedPreferences.commit()

                    when(persona){
                        is Administrator -> startActivity(Intent(this, GestionModeratorActivity::class.java)  )
                        is User -> startActivity(intent )
                        is Moderator -> startActivity( Intent(this, ModeratorActivity::class.java) )
                    }
                }else{
                    Snackbar.make(binding.root,"Los datos ingresados son erroneos, porfavor confirma",Snackbar.LENGTH_LONG).show()

                }

            } catch (e: Exception) {
                Snackbar.make(binding.root,"Los datos ingresados son erroneos, porfavor confirma",Snackbar.LENGTH_LONG).show()
            }

        }
    }


    fun registrar() {
        val intent = Intent(this, RegistroActivity::class.java)
        startActivity(intent)
    }



}