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
import com.example.unilocal.bd.Administrators
import com.example.unilocal.bd.Moderators
import com.example.unilocal.databinding.ActivityGestionModeratorBinding
import com.example.unilocal.models.Moderator
import com.google.android.material.navigation.NavigationView

class GestionModeratorActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var sharedPreferences: SharedPreferences
    lateinit var binding: ActivityGestionModeratorBinding
    lateinit var moderators : ArrayList<Moderator>
    var codeAdministrador: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGestionModeratorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences= this.getSharedPreferences("sesion", Context.MODE_PRIVATE)
        codeAdministrador = sharedPreferences.getInt("id",-1)
        if(codeAdministrador != -1){
            val administrator = Administrators.obtener(codeAdministrador)
            val header = binding.navigationView.getHeaderView(0)
            header.findViewById<TextView>(R.id.name_user_session).text = administrator!!.nombre
            header.findViewById<TextView>(R.id.email_user_session).text = administrator!!.correo
        }
        moderators = Moderators.listar()
        var menu = this.findViewById<Button>(R.id.btn_menu)
        menu.setOnClickListener { abrirMenu()}
        val adapter = ModeratorAdapter(moderators)
        binding.listModerators.adapter = adapter
        binding.listModerators.layoutManager  = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        binding.navigationView.setNavigationItemSelectedListener(this)
        binding.btnCreateModerator.setOnClickListener { irCrearModerator() }
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

    fun irCrearModerator(){
        val intent = Intent(this, CrearModeradorActivity::class.java)
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