package com.example.unilocal.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.unilocal.R
import com.example.unilocal.bd.Cities
import com.example.unilocal.databinding.ActivityRegistroBinding
import com.example.unilocal.models.City
import com.example.unilocal.models.User
import com.example.unilocal.sqlite.UniLocalDbHelper
import com.google.android.material.snackbar.Snackbar
import java.lang.Exception

class RegistroActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegistroBinding
    private lateinit var db:UniLocalDbHelper
    lateinit var cities: ArrayList<City>
    var cityPosition: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadCities()
        binding.btnRegistro.setOnClickListener{ registraUsuario()}
        db = UniLocalDbHelper(this)
    }

    fun registraUsuario(){
        val name = binding.userName.text.toString()
        val nickname = binding.nicknameUsuario.text.toString()
        val email = binding.emailUsuario.text.toString()
        val password = binding.userPassword.text.toString()
        val idCity = cities[cityPosition].id
        val confirmPassword = binding.userPasswordConfirm.text.toString()

        if(confirmPassword.isEmpty()){
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

        if(name.isNotEmpty() && email.isNotEmpty() && confirmPassword.isNotEmpty() && nickname.isNotEmpty() && nickname.length<=10 && password.isNotEmpty() && idCity!= -1){

            val user = User(0, name, nickname, email, password,idCity)
            try {
                //Usuarios.agregar(user)
                db.createUser(user)
                startActivity(Intent(this, LoginActivity::class.java))

            }catch (e:Exception){
              Snackbar.make(binding.root,e.message.toString(), Snackbar.LENGTH_LONG).show()
            }
        }
    }

    fun loadCities(){
        cities = Cities.listar()
        var adapter= ArrayAdapter(this,android.R.layout.simple_spinner_item,cities)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.departmentPlace.adapter= adapter
        binding.departmentPlace.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                cityPosition = p2
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }
}