package com.example.unilocal.activities


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.unilocal.R
import com.example.unilocal.bd.Cities
import com.example.unilocal.bd.Moderators
import com.example.unilocal.bd.Places
import com.example.unilocal.databinding.ActivityCrearModeradorBinding
import com.example.unilocal.models.City
import com.example.unilocal.models.Moderator
import com.example.unilocal.models.Place
import com.example.unilocal.models.StatusPlace

class CrearModeradorActivity : AppCompatActivity() {
    lateinit var binding: ActivityCrearModeradorBinding
    var cityPosition: Int = -1
    lateinit var cities: ArrayList<City>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrearModeradorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadCities()
        binding.btnRegistro.setOnClickListener { createModerator() }
    }

    fun loadCities(){
        cities = Cities.listar()
        var adapter= ArrayAdapter(this, android.R.layout.simple_spinner_item,cities)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.cityModerator.adapter= adapter
        binding.cityModerator.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                cityPosition = p2
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    fun createModerator(){
        val nombre = binding.moderatorName.text.toString()
        val nickname = binding.nicknameModerator.text.toString()
        val correo = binding.moderatorEmail.text.toString()
        val password = binding.moderatorPassword.text.toString()
        val confirmPassword = binding.moderatorPasswordConfirm.text.toString()
        val idCity = cities[cityPosition].id

        if(nombre.isEmpty()){
            binding.nameLayout.error = getString(R.string.txt_obligatorio)
        }else{
            binding.nameLayout.error = null
        }

        if(nickname.isEmpty()){
            binding.nicknameLayout.error = getString(R.string.txt_obligatorio)
        }else{
            binding.nicknameLayout.error = null
        }

        if(correo.isEmpty()){
            binding.emailLayout.error = getString(R.string.txt_obligatorio)
        }else{
            binding.emailLayout.error = null
        }

        if(password.isEmpty()){
            binding.passwordLayout.error = getString(R.string.txt_obligatorio)
        }else{
            binding.passwordLayout.error = null
        }

        if(confirmPassword.isEmpty()){
            binding.confirmPasswordLayout.error = getString(R.string.txt_obligatorio)
        }else{
            binding.confirmPasswordLayout.error = null
        }

        if(confirmPassword != password){
            binding.confirmPasswordLayout.error = getString(R.string.txt_contrasenas_incorrectas)
            binding.passwordLayout.error = getString(R.string.txt_contrasenas_incorrectas)
        }

        if(nombre.isNotEmpty() && nickname.isNotEmpty() && correo.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() && password == confirmPassword && idCity != -1 ){
           val moderator = Moderator(Moderators.listar().size+1, nombre, nickname,correo,password, idCity)
           Moderators.createModerator(moderator)
            val intent = Intent(this, GestionModeratorActivity::class.java)
            startActivity(intent)
        }
    }

}