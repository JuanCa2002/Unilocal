package com.example.unilocal.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.unilocal.R
import com.example.unilocal.adapter.PlaceAdapter
import com.example.unilocal.bd.Places
import com.example.unilocal.bd.Usuarios
import com.example.unilocal.databinding.ActivityResultadoBusquedaBinding
import com.example.unilocal.models.Place
import com.google.android.material.navigation.NavigationView

class ResultadoBusquedaActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener{
    private lateinit var sharedPreferences: SharedPreferences
    lateinit var placesCoincidences:ArrayList<Place>
    lateinit var binding: ActivityResultadoBusquedaBinding
    var textSearch:String = ""
    var code:String? = ""
    lateinit var adapter: PlaceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultadoBusquedaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences= this.getSharedPreferences("sesion", Context.MODE_PRIVATE)
        textSearch = intent.extras!!.getString("txt_search","")
        placesCoincidences = ArrayList()
        if(code != ""){
            val usuario = Usuarios.getUser(code)
            val header = binding.navigationView.getHeaderView(0)
            header.findViewById<TextView>(R.id.name_user_session).text = usuario!!.nombre
            //header.findViewById<TextView>(R.id.email_user_session).text = usuario!!.correo
        }

        if(textSearch.isNotEmpty()){
            placesCoincidences= Places.buscarNombre(textSearch, placesCoincidences)
            Log.e(ResultadoBusquedaActivity::class.java.simpleName, placesCoincidences.toString())
        }
        binding.navigationView.setNavigationItemSelectedListener (this)
        var menu = this.findViewById<Button>(R.id.btn_menu)
        menu.setOnClickListener { abrirMenu()}
        adapter = PlaceAdapter(placesCoincidences,"Busqueda")
        binding.listPlacesSearch.adapter = adapter
        binding.listPlacesSearch.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
    }

    fun cerrarSesion(){
        sharedPreferences.edit().clear().commit()
        finish()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    fun abrirMenu(){
        binding.drawerLayout.openDrawer(GravityCompat.START)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.navPerfil -> abrirPerfil()
            R.id.menu_cerrar_sesion -> cerrarSesion()
            R.id.navCategorias -> abrirCategorias()
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

    override fun onResume() {
        super.onResume()
        Log.e("textxo",textSearch.toString())
        if(textSearch.isNotEmpty()) {
            placesCoincidences= Places.buscarNombre(textSearch,placesCoincidences)
            adapter.notifyDataSetChanged()
        }
    }
}