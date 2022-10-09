package com.example.unilocal.activities

import android.R
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.unilocal.adapter.ModeratorAdapter
import com.example.unilocal.adapter.PlaceAdapter
import com.example.unilocal.bd.*
import com.example.unilocal.databinding.ActivityCategoriesBinding
import com.example.unilocal.models.Category
import com.example.unilocal.models.Place
import com.google.android.material.navigation.NavigationView

class CategoriesActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener  {
    lateinit var categories: ArrayList<Category>
    lateinit var binding: ActivityCategoriesBinding
    private lateinit var sharedPreferences: SharedPreferences
    var code: Int = -1
    var places:ArrayList<Place> = ArrayList()
    var categoryPosition: Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadCategories()
        var menu = this.findViewById<Button>(com.example.unilocal.R.id.btn_menu)
        menu.setOnClickListener { abrirMenu()}

        sharedPreferences= this.getSharedPreferences("sesion", Context.MODE_PRIVATE)
        code = sharedPreferences.getInt("id",-1)
        var typeUser = sharedPreferences.getString("tipo_usuario","")
        code = sharedPreferences.getInt("id",-1)
        if(code != -1) {
            if (typeUser == "Usuario") {
                val usuario = Usuarios.getUser(code)
                val header = binding.navigationView.getHeaderView(0)
                header.findViewById<TextView>(com.example.unilocal.R.id.name_user_session).text =
                    usuario!!.nombre
                header.findViewById<TextView>(com.example.unilocal.R.id.email_user_session).text =
                    usuario!!.correo
            } else if (typeUser == "Moderador") {
                val moderator = Moderators.obtener(code)
                val header = binding.navigationView.getHeaderView(0)
                header.findViewById<TextView>(com.example.unilocal.R.id.name_user_session).text =
                    moderator!!.nombre
                header.findViewById<TextView>(com.example.unilocal.R.id.email_user_session).text =
                    moderator!!.correo
            } else {
                val administrator = Administrators.obtener(code)
                val header = binding.navigationView.getHeaderView(0)
                header.findViewById<TextView>(com.example.unilocal.R.id.name_user_session).text =
                    administrator!!.nombre
                header.findViewById<TextView>(com.example.unilocal.R.id.email_user_session).text =
                    administrator!!.correo
            }
        }
        binding.navigationView.setNavigationItemSelectedListener(this)
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

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            com.example.unilocal.R.id.navPerfil -> abrirPerfil()
            com.example.unilocal.R.id.menu_cerrar_sesion -> cerrarSesion()
            com.example.unilocal.R.id.navCategorias -> abrirCategorias()
        }
        item.isChecked = true
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    fun abrirPerfil(){
        val intent = Intent(this, DetallesUsuarioActivity::class.java)
        startActivity(intent)
    }

    fun abrirCategorias(){
        val intent = Intent(this, CategoriesActivity::class.java)
        startActivity(intent)
    }

    fun abrirMenu(){
        binding.drawerLayout.openDrawer(GravityCompat.START)
    }

    fun cerrarSesion(){
        sharedPreferences.edit().clear().commit()
        finish()

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    fun loadPlacesByCategory(){
        places = Places.buscarCategoria(categories[categoryPosition].id)
        val adapter = PlaceAdapter(places,"Busqueda")
        binding.listPlacesCategory.adapter = adapter
        binding.listPlacesCategory.layoutManager  = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
    }
}