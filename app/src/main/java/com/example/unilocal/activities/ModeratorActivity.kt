package com.example.unilocal.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.unilocal.R
import com.example.unilocal.bd.Usuarios
import com.example.unilocal.databinding.ActivityModeratorBinding
import com.example.unilocal.fragments.*
import com.google.android.material.navigation.NavigationView

class ModeratorActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener  {
    private lateinit var sharedPreferences: SharedPreferences
    private var MENU_PENDIENTES = "Pendiente"
    private var MENU_REGISTRO = "Registro"
    lateinit var binding: ActivityModeratorBinding
    var codeModerator: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModeratorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(codeModerator != ""){
            val moderator = Usuarios.getUser(codeModerator)
            val header = binding.navigationView.getHeaderView(0)
            header.findViewById<TextView>(R.id.name_user_session).text = moderator!!.nombre
            //header.findViewById<TextView>(R.id.email_user_session).text = moderator!!.correo
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