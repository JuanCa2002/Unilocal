package com.example.unilocal.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.unilocal.R
import com.example.unilocal.bd.Usuarios
import com.example.unilocal.databinding.ActivityRegistroBinding
import com.example.unilocal.models.User
import com.google.android.material.snackbar.Snackbar

class RegistroActivity : AppCompatActivity() {

    lateinit var binding:ActivityRegistroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegistro.setOnClickListener{ registraUsuario()}
    }

    fun registraUsuario(){
        val name = binding.userName.text.toString()
        val nickname = binding.nicknameUsuario.text.toString()
        val email = binding.emailUsuario.text.toString()
        val password = binding.userPassword.text.toString()
        val birthday = binding.userBirthday.text.toString()
        val confirmPassword = binding.userPasswordConfirm.text.toString()

        if(confirmPassword.isEmpty()){
            binding.nameLayout.error = getString(R.string.txt_obligatorio)
        }else{
            binding.nameLayout.error = null
        }

        if(birthday.isEmpty()){
            binding.nameLayout.error = getString(R.string.txt_obligatorio)
        }else{
            binding.nameLayout.error = null
        }
        if(name.isEmpty()){
            binding.nameLayout.error = getString(R.string.txt_obligatorio)
        }
        else{
            binding.nameLayout.error = null
        }
        if(nickname.isEmpty()){
            binding.nicknameLayout.error = getString(R.string.txt_obligatorio)
        }
        else{
            if (nickname.length >10){
                binding.nicknameUsuario.error = getString(R.string.txt_maximo_caracteres)
            }
            else {
                binding.nicknameLayout.error = null
            }
        }
        if(email.isEmpty()){
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

        if(name.isNotEmpty() && email.isNotEmpty() && birthday.isEmpty() && confirmPassword.isEmpty() && nickname.isNotEmpty() && nickname.length<=10 && password.isNotEmpty()){
            val user = User(1, name, nickname, email, password,birthday)
            Usuarios.agregar(user)
            Log.w(MainActivity::class.java.simpleName, "Se registro correctamente")
            Log.w(MainActivity::class.java.simpleName, Usuarios.listar().toString())
        }

    }

}