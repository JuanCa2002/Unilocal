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
import androidx.fragment.app.Fragment
import com.example.unilocal.R
import com.example.unilocal.databinding.ActivityMainBinding
import com.example.unilocal.fragments.FavoritesFragment
import com.example.unilocal.fragments.InicioFragment
import com.example.unilocal.fragments.MyPlacesFragment

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.barraInferior.setOnItemSelectedListener {
            when(it.itemId){
                R.id.menu_home -> changeFragments(1)
                R.id.menu_favorites -> changeFragments(2)
                R.id.menu_my_places -> changeFragments(3)
            }
            true
        }

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
        val fragment:Fragment
        if(valor ==  1){
           fragment = InicioFragment()
        }else if(valor == 2){
            fragment = MyPlacesFragment()
        }else{
            fragment = FavoritesFragment()
        }
       supportFragmentManager.beginTransaction().replace(binding.contenidoPrincipal.id,fragment)
           .addToBackStack("fragmento_$valor")
           .commit()

    }
}