package com.example.unilocal.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.unilocal.R
import com.example.unilocal.bd.Administrators
import com.example.unilocal.bd.Cities
import com.example.unilocal.bd.Moderators
import com.example.unilocal.bd.Usuarios
import com.example.unilocal.databinding.ActivityDetallesUsuarioBinding
import com.example.unilocal.models.*

class DetallesUsuarioActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetallesUsuarioBinding
    lateinit var cities: ArrayList<City>
    var cityPosition: Int = -1
    var tipo: String? = ""
    var code: Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetallesUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sp = getSharedPreferences("sesion", Context.MODE_PRIVATE)
        code = sp.getInt("id",0)
        tipo = sp.getString("tipo_usuario","")

         var user = when(tipo){
            "Usuario" -> Usuarios.getUser(code)
            "Moderador" -> Moderators.obtener(code)
            else -> Administrators.obtener(code)
        }
        if(user != null){
            binding.nombreUsuario.text = user.nombre
            binding.nombreLayout.hint = user.nombre
            binding.nicknameLayout.hint = user.nickname
            binding.correoLayout.hint = user.correo
        }
        loadCities(user)
        binding.btnGuardarCambiosDetallesUsuario.setOnClickListener { updateUser(user) }
    }

    fun loadCities(person:Person?){
        cities = Cities.listar()
        var city = Cities.obtener(person!!.idCity)
        var position = cities.indexOf(city)
        var adapter= ArrayAdapter(this,android.R.layout.simple_spinner_item,cities)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.cityEdit.adapter= adapter
        binding.cityEdit.setSelection(position)
        binding.cityEdit.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                cityPosition = p2
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
    }

    fun updateUser(person: Person?){
        var nombre = binding.campoNombreUsuario.text.toString()
        var nickname = binding.nicknameUsuario.text.toString()
        var correo = binding.correoUsuario.text.toString()
        var idCity = cities[cityPosition].id

        if(nombre.isEmpty()){
            nombre = binding.nombreLayout.hint.toString()
        }

        if(nickname.isEmpty()){
            nickname = binding.nicknameLayout.hint.toString()
        }

        if(correo.isEmpty()){
            correo = binding.correoLayout.hint.toString()
        }
        Log.e("Nuevo nombre", tipo.toString())
        if(nombre.isNotEmpty() && nickname.isNotEmpty() && correo.isNotEmpty() && idCity!=-1){
            when(tipo){
                "Usuario" -> Usuarios.updateUser(User(person!!.id,nombre,nickname,correo,person.password,idCity), person.id)
                "Moderador" -> Moderators.updateModerator(Moderator(person!!.id,nombre,nickname,correo,person.password,idCity),person.id)
                else -> Administrators.updateAdministrator(Administrator(person!!.id,nombre,nickname,correo,person.password,idCity),person.id)
            }
            when(tipo){
                "Usuario" -> startActivity(Intent(this, MainActivity::class.java))
                "Moderador" -> startActivity(Intent(this, ModeratorActivity::class.java))
                else -> startActivity(Intent(this, GestionModeratorActivity::class.java))
            }
        }

    }
}