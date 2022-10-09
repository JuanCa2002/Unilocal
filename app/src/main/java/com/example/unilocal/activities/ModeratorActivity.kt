package com.example.unilocal.activities

import android.content.Context
import android.graphics.Canvas
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.unilocal.R
import com.example.unilocal.adapter.PlaceAdapter
import com.example.unilocal.bd.Places
import com.example.unilocal.databinding.ActivityModeratorBinding
import com.example.unilocal.fragments.*
import com.example.unilocal.models.Place
import com.example.unilocal.models.StatusPlace
import com.google.android.material.snackbar.Snackbar
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

class ModeratorActivity : AppCompatActivity() {
    lateinit var binding:ActivityModeratorBinding
    var codeModerator: Int = -1
    private var MENU_PENDIENTES = "Pendiente"
    private var MENU_REGISTRO = "Registro"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModeratorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var sharedPreferences= this.getSharedPreferences("sesion", Context.MODE_PRIVATE)
        codeModerator = sharedPreferences.getInt("id",-1)
        Log.e("Code",codeModerator.toString())
        changeFragments(1, MENU_PENDIENTES)
        binding.barraInferiorModerator.setOnItemSelectedListener {
            when(it.itemId){
                R.id.menu_pendiente -> changeFragments(1,MENU_PENDIENTES)
                R.id.menu_registro -> changeFragments(2,MENU_REGISTRO)
            }
            true
        }
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
}