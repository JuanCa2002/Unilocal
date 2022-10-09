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
import com.example.unilocal.adapter.ModeratorAdapter
import com.example.unilocal.adapter.PlaceAdapter
import com.example.unilocal.bd.Administrators
import com.example.unilocal.bd.Moderators
import com.example.unilocal.bd.Places
import com.example.unilocal.bd.Usuarios
import com.example.unilocal.databinding.ActivityResultadoBusquedaBinding
import com.example.unilocal.models.Place
import com.google.android.material.navigation.NavigationView

class ResultadoBusquedaActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener{
    lateinit var binding: ActivityResultadoBusquedaBinding
    var textSearch:String = ""
    var code:Int = -1
    private lateinit var sharedPreferences: SharedPreferences
    lateinit var placesCoincidences:ArrayList<Place>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultadoBusquedaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences= this.getSharedPreferences("sesion", Context.MODE_PRIVATE)
        textSearch = intent.extras!!.getString("txt_search","")
        placesCoincidences = ArrayList()

        var typeUser = sharedPreferences.getString("tipo_usuario","")
        code = sharedPreferences.getInt("id",-1)
        if(code != -1){
            if(typeUser == "Usuario"){
                val usuario = Usuarios.getUser(code)
                val header = binding.navigationView.getHeaderView(0)
                header.findViewById<TextView>(R.id.name_user_session).text = usuario!!.nombre
                header.findViewById<TextView>(R.id.email_user_session).text = usuario!!.correo
            }else if(typeUser == "Moderador"){
                val moderator = Moderators.obtener(code)
                val header = binding.navigationView.getHeaderView(0)
                header.findViewById<TextView>(R.id.name_user_session).text = moderator!!.nombre
                header.findViewById<TextView>(R.id.email_user_session).text = moderator!!.correo
            }else{
                val administrator = Administrators.obtener(code)
                val header = binding.navigationView.getHeaderView(0)
                header.findViewById<TextView>(R.id.name_user_session).text = administrator!!.nombre
                header.findViewById<TextView>(R.id.email_user_session).text = administrator!!.correo
            }

        }

        if(textSearch.isNotEmpty()){
            placesCoincidences= Places.buscarNombre(textSearch)
            Log.e(ResultadoBusquedaActivity::class.java.simpleName, placesCoincidences.toString())
        }
        binding.navigationView.setNavigationItemSelectedListener (this)
        var menu = this.findViewById<Button>(R.id.btn_menu)
        menu.setOnClickListener { abrirMenu()}
        val adapter = PlaceAdapter(placesCoincidences,"Busqueda")
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
}