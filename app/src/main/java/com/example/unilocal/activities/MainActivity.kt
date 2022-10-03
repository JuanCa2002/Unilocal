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
     var codeUser: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        changeFragments(2)
        binding.btnCreatePlace.hide()

        binding.barraInferior.setOnItemSelectedListener {
            when(it.itemId){
                R.id.menu_favorites -> changeFragments(1)
                R.id.menu_home -> changeFragments(2)
                R.id.menu_my_places -> changeFragments(3)
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

    fun changeFragments(valor:Int){
        var fragment:Fragment
        if(valor ==  1){
            val bundle:Bundle = Bundle()
            bundle.putInt("code", codeUser)
            fragment = FavoritesFragment()
            fragment.bundle = bundle
            binding.btnCreatePlace.hide()
        }else if(valor == 2){
            val bundle:Bundle = Bundle()
            bundle.putInt("code", codeUser)
            fragment = InicioFragment()
            fragment.bundle = bundle
            binding.btnCreatePlace.hide()
        }else{
            val bundle:Bundle = Bundle()
            bundle.putInt("code", codeUser)
            fragment = MyPlacesFragment()
            fragment.bundle = bundle
            binding.btnCreatePlace.show()


        }

       supportFragmentManager.beginTransaction().replace(binding.contenidoPrincipal.id,fragment)
           .addToBackStack("fragmento_$valor")
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


}