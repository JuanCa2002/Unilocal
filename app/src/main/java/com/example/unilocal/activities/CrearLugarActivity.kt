package com.example.unilocal.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.unilocal.R
import com.example.unilocal.bd.Categories
import com.example.unilocal.bd.Cities
import com.example.unilocal.bd.Places
import com.example.unilocal.databinding.ActivityCrearLugarBinding
import com.example.unilocal.fragments.DialogSchedulesFragment
import com.example.unilocal.models.*
import java.text.FieldPosition

class CrearLugarActivity : AppCompatActivity(),DialogSchedulesFragment.onHorarioCreadoListener {
    lateinit var binding: ActivityCrearLugarBinding
    lateinit var categories: ArrayList<Category>
    lateinit var cities: ArrayList<City>
    var cityPosition: Int = -1
    var categoryPosition: Int = -1
    lateinit var horarios: ArrayList<Schedule>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrearLugarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        horarios = ArrayList()
        loadCities()
        loadCategories()
        binding.btnCreatePlace.setOnClickListener { createPlace() }
        binding.btnAsignarHorario.setOnClickListener { mostrarDialogo()}
    }

    fun mostrarDialogo(){
        val dialog = DialogSchedulesFragment()
        dialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogoTitulo)
        dialog.listener = this
        dialog.show(supportFragmentManager, "Agregar")

    }

    fun loadCities(){
        cities = Cities.listar()
        var adapter= ArrayAdapter(this,android.R.layout.simple_spinner_item,cities)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.cityPlace.adapter= adapter
        binding.cityPlace.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                cityPosition = p2
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    fun loadCategories(){
        categories = Categories.listar()
        var adapter= ArrayAdapter(this,android.R.layout.simple_spinner_item,categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categoryPlace.adapter= adapter
        binding.categoryPlace.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                categoryPosition = p2
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    fun createPlace(){
        val name = binding.placeName.text.toString()
        val description = binding.placeDescription.text.toString()
        val phone = binding.placePhone.text.toString()
        val address = binding.addressPlace.text.toString()
        val idCity = cities[cityPosition].id
        val idCategory  = categories[categoryPosition].id

        if(name.isEmpty()){
            binding.placeNameLayout.error = getString(R.string.txt_obligatorio)
        }else{
            binding.placeNameLayout.error = null
        }

        if(description.isEmpty()){
            binding.placeDescriptionLayout.error = getString(R.string.txt_obligatorio)
        }else{
            binding.placeDescriptionLayout.error = null
        }

        if(phone.isEmpty()){
            binding.placePhoneLayout.error = getString(R.string.txt_obligatorio)
        }else{
            binding.placePhoneLayout.error = null
        }

        if(address.isEmpty()){
            binding.addressPlaceLayout.error = getString(R.string.txt_obligatorio)
        }else{
            binding.addressPlaceLayout.error = null
        }

        if(name.isNotEmpty() && description.isNotEmpty() && phone.isNotEmpty() && horarios.isNotEmpty() && address.isNotEmpty() &&idCity != -1 && idCategory !=-1){
            val newPlace = Place(Places.list().size,name,description,1,StatusPlace.SIN_REVISAR,idCategory,0f,address,0f,idCity)
            val phones:ArrayList<String> = ArrayList()
            phones.add(phone)
            newPlace.phones= phones
            newPlace.schedules = horarios
            Places.crear(newPlace)
            Log.e("LUGAR CREADO",newPlace.toString())
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun elegirHorario(horario: Schedule) {
        horarios.add(horario)
    }
}