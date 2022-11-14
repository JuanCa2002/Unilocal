package com.example.unilocal.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import com.example.unilocal.R
import com.example.unilocal.bd.Usuarios
import com.example.unilocal.databinding.ActivityLoginBinding
import com.example.unilocal.models.Rol
import com.example.unilocal.models.User
import com.example.unilocal.sqlite.UniLocalDbHelper
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.lang.Exception

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    private lateinit var db: UniLocalDbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val sp = getSharedPreferences("sesion",Context.MODE_PRIVATE)
//        val email = sp.getString("correo_usuario","")
//        val type = sp.getString("tipo_usuario","")
//        val code = sp.getInt("id",0)
        db = UniLocalDbHelper(this)

        val userLogin = FirebaseAuth.getInstance().currentUser
        if(userLogin!=null){
            makeRedirection(userLogin)
        } else{
            binding = ActivityLoginBinding.inflate(layoutInflater)
            setContentView(binding.root)
            binding.btnLogin.setOnClickListener { login() }
            binding.btnRegistro.setOnClickListener{ registrar() }
      }

    }

    fun login() {
        val correo = binding.emailUsuario.text.toString()
        val password = binding.passwordUsuario.text.toString()
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
            // Hacerlo mas adelante con la base de datos
            FirebaseAuth.getInstance()
                        .signInWithEmailAndPassword(correo, password)
                        .addOnCompleteListener {
                             if(it.isSuccessful){
                                 val userLogin = FirebaseAuth.getInstance().currentUser
                                 Log.e("Llego ", userLogin.toString())
                                 if(userLogin!=null){
                                     Log.e("Llego ", userLogin.toString())
                                     makeRedirection(userLogin)
                                 }
                             }else{
                                 Snackbar.make(binding.root,R.string.txt_datos_erroneos,Snackbar.LENGTH_LONG).show()
                             }
                        }.addOnFailureListener{
                            Snackbar.make(binding.root,it.message.toString(),Snackbar.LENGTH_LONG).show()
                        }
                }else{
                    Snackbar.make(binding.root,R.string.txt_datos_erroneos,Snackbar.LENGTH_LONG).show()
                }

    }

    fun registrar() {
        val intent = Intent(this, RegistroActivity::class.java)
        startActivity(intent)
    }

    fun makeRedirection(user:FirebaseUser){
        Firebase.firestore
            .collection("users")
            .document(user.uid)
            .get()
            .addOnSuccessListener { u ->
                    val rol = u.toObject(User::class.java)!!.rol
                    val intent = when(rol){
                        Rol.ADMINISTRATOR -> Intent(this, GestionModeratorActivity::class.java)
                        Rol.USER-> Intent(this, MainActivity::class.java)
                        Rol.MODERATOR -> Intent(this, ModeratorActivity::class.java)
                    }
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity( intent )
                    finish()

            }.addOnFailureListener {
                Log.e("USUARIO", it.message.toString())
            }
    }

}