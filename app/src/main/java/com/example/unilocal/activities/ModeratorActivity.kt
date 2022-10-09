package com.example.unilocal.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Canvas
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.unilocal.R
import com.example.unilocal.adapter.PlaceAdapter
import com.example.unilocal.bd.Moderators
import com.example.unilocal.bd.Places
import com.example.unilocal.bd.Usuarios
import com.example.unilocal.databinding.ActivityModeratorBinding
import com.example.unilocal.fragments.*
import com.example.unilocal.models.Moderator
import com.example.unilocal.models.Place
import com.example.unilocal.models.StatusPlace
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

class ModeratorActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener  {
    lateinit var binding: ActivityModeratorBinding
    var codeModerator: Int = -1
    private var MENU_PENDIENTES = "Pendiente"
    private lateinit var sharedPreferences: SharedPreferences
    private var MENU_REGISTRO = "Registro"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModeratorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences= this.getSharedPreferences("sesion", Context.MODE_PRIVATE)
        codeModerator = sharedPreferences.getInt("id",-1)
        if(codeModerator != -1){
            val moderator = Moderators.obtener(codeModerator)
            val header = binding.navigationView.getHeaderView(0)
            header.findViewById<TextView>(R.id.name_user_session).text = moderator!!.nombre
            header.findViewById<TextView>(R.id.email_user_session).text = moderator!!.correo
        }
        changeFragments(1, MENU_PENDIENTES)
        binding.barraInferiorModerator.setOnItemSelectedListener {
            when(it.itemId){
                R.id.menu_pendiente -> changeFragments(1,MENU_PENDIENTES)
                R.id.menu_registro -> changeFragments(2,MENU_REGISTRO)
            }
            true
        }
        binding.navigationView.setNavigationItemSelectedListener (this)
        var menu = this.findViewById<Button>(R.id.btn_menu)
        menu.setOnClickListener { abrirMenu()}

    }


    fun changeFragments(valor:Int, nombre:String){
        var fragment: Fragment
        if(valor ==  1){
            fragment = PendientesPlaceFragment.newInstance(codeModerator)
        }else{
            fragment = RegistreFragment.newInstance(codeModerator)
        }

        supportFragmentManager.beginTransaction().replace(binding.contenidoPrincipal.id,fragment)
            .addToBackStack(nombre)
            .commit()
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