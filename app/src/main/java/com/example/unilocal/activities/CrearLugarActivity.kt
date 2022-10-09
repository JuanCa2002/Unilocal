package com.example.unilocal.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.unilocal.R
import com.example.unilocal.bd.Categories
import com.example.unilocal.bd.Cities
import com.example.unilocal.bd.Places
import com.example.unilocal.databinding.ActivityCrearLugarBinding
import com.example.unilocal.models.Category
import com.example.unilocal.models.City
import com.example.unilocal.models.Place
import com.example.unilocal.models.StatusPlace
import java.text.FieldPosition

class CrearLugarActivity : AppCompatActivity() {
    lateinit var binding: ActivityCrearLugarBinding
    lateinit var categories: ArrayList<Category>
    lateinit var cities: ArrayList<City>
    var cityPosition: Int = -1
    var categoryPosition: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCrearLugarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadCities()
        loadCategories()
        binding.btnCreatePlace.setOnClickListener { createPlace() }
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

        if(name.isNotEmpty() && description.isNotEmpty() && phone.isNotEmpty() && address.isNotEmpty() &&idCity != -1 && idCategory !=-1){
            val newPlace = Place(7,name,description,1,StatusPlace.SIN_REVISAR,idCategory,0f,address,0f,idCity)

            val phones:ArrayList<String> = ArrayList()
            phones.add(phone)
            newPlace.phones= phones
            Places.crear(newPlace)
        }
    }
}