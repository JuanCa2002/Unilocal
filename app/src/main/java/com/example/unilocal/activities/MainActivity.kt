package com.example.unilocal.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.unilocal.R
import com.example.unilocal.databinding.ActivityMainBinding
import com.example.unilocal.fragments.FavoritesFragment
import com.example.unilocal.fragments.InicioFragment
import com.example.unilocal.fragments.MyPlacesFragment
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {
    lateinit var binding: ActivityMainBinding
    private var MENU_INICIO = "Inicio"
    private var MENU_MIS_LUGARES = "Mis lugares"
    private var MENU_FAVORITOS = "favoritos"
     var codeUser: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        changeFragments(2,MENU_INICIO)

        binding.barraInferior.setOnItemSelectedListener {
            when(it.itemId){
                R.id.menu_favorites -> changeFragments(1,MENU_FAVORITOS)
                R.id.menu_home -> changeFragments(2,MENU_INICIO)
                R.id.menu_my_places -> changeFragments(3,MENU_MIS_LUGARES)
            }
            true
        }
        val menuBoton: Button = findViewById(R.id.btn_menu)
        menuBoton.setOnClickListener {  abrirMenu()}
        codeUser = intent.extras!!.getInt("code")
        Log.e("MainActivity",codeUser.toString())

    }

    fun irCrearLugar(view:View){
        val intent = Intent(this, CrearLugarActivity::class.java)
        startActivity(intent)
    }

    fun cerrarSesion(view: View){
        val sharedPreferences= this.getSharedPreferences("sesion",Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().commit()

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    fun changeFragments(valor:Int, nombre:String){
        var fragment:Fragment
        if(valor ==  1){
            val bundle:Bundle = Bundle()
            bundle.putInt("code", codeUser)
            fragment = FavoritesFragment()
            fragment.bundle = bundle
        }else if(valor == 2){
            val bundle:Bundle = Bundle()
            bundle.putInt("code", codeUser)
            fragment = InicioFragment()
            fragment.bundle = bundle
        }else{
            val bundle:Bundle = Bundle()
            bundle.putInt("code", codeUser)
            fragment = MyPlacesFragment()
            fragment.bundle = bundle


        }

       supportFragmentManager.beginTransaction().replace(binding.contenidoPrincipal.id,fragment)
           .addToBackStack(nombre)
           .commit()
    }

    fun abrirMenu(){
        binding.drawerLayout.openDrawer(GravityCompat.START)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.navHome -> Log.e("MainActivity","Dandole al boton home")
        }
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val count = supportFragmentManager.backStackEntryCount

        if(count > 0){
            val nombre = supportFragmentManager.getBackStackEntryAt(count-1).name
            when(nombre){
                MENU_FAVORITOS -> binding.barraInferior.menu.getItem(0).isChecked = true
                MENU_INICIO -> binding.barraInferior.menu.getItem(1).isChecked = true
                MENU_MIS_LUGARES -> binding.barraInferior.menu.getItem(2).isChecked = true
            }
        }
    }


}