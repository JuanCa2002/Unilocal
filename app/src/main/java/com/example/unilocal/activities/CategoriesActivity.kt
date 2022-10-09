package com.example.unilocal.activities

import android.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.unilocal.adapter.ModeratorAdapter
import com.example.unilocal.adapter.PlaceAdapter
import com.example.unilocal.bd.Categories
import com.example.unilocal.bd.Places
import com.example.unilocal.databinding.ActivityCategoriesBinding
import com.example.unilocal.models.Category
import com.example.unilocal.models.Place

class CategoriesActivity : AppCompatActivity() {
    lateinit var categories: ArrayList<Category>
    lateinit var binding: ActivityCategoriesBinding
    var places:ArrayList<Place> = ArrayList()
    var categoryPosition: Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadCategories()
        binding.filter.setOnClickListener { loadPlacesByCategory() }

    }

    fun loadCategories(){
        categories = Categories.listar()
        var adapter= ArrayAdapter(this, R.layout.simple_spinner_item,categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categories.adapter= adapter
        binding.categories.setSelection(0)
        binding.categories.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                categoryPosition = p2
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
    }

    fun loadPlacesByCategory(){
        places = Places.buscarCategoria(categories[categoryPosition].id)
        val adapter = PlaceAdapter(places,"Busqueda")
        binding.listPlacesCategory.adapter = adapter
        binding.listPlacesCategory.layoutManager  = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
    }
}