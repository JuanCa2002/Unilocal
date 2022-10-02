package com.example.unilocal.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.unilocal.R
import com.example.unilocal.adapter.PlaceAdapter
import com.example.unilocal.bd.Places
import com.example.unilocal.bd.Usuarios
import com.example.unilocal.databinding.ActivityDetalleLugarBinding
import com.example.unilocal.fragments.FavoritesFragment
import com.example.unilocal.models.Place

class DetalleLugarActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetalleLugarBinding
    var codePlace:Int = -1
    lateinit var placeAdapter: PlaceAdapter
    lateinit var favorites: ArrayList<Int>
    var placesFavorites: ArrayList<Place> = ArrayList()
    var pos:Int = -1
    var codeUser:Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleLugarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sp = getSharedPreferences("sesion", Context.MODE_PRIVATE)

        codeUser = sp.getInt("id",0)

        favorites = Usuarios.getListFavorites(codeUser)

        val places = Places.list()
        val usuario = Usuarios.getUser(codeUser!!)
        for (i in usuario!!.favorities){
            for (j in places){
                if(j.id == i){
                    placesFavorites.add(j)
                }
            }
        }

        placeAdapter = PlaceAdapter(placesFavorites,"Busqueda")

        codePlace = intent.extras!!.getInt("code")

        pos = intent.extras!!.getInt("position")

        val place = Places.obtener(codePlace)

        if(place != null){
            binding.namePlace.text = place!!.name
            //Hay que agregar un campo de telefono.
            binding.txtDescipcionLugar.text = place!!.description
            binding.txtDireccionLugar.text = place!!.address
        }
        val favorito = favorites.firstOrNull{f -> f == codePlace}
        if(favorito != null){
            binding.btnFavorito.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_baseline_favorite_red))
            binding.btnFavorito.setOnClickListener{eliminarFavoritos()}

        }else{
            binding.btnFavorito.setOnClickListener{agregarFavoritos()}
        }
    }

    fun agregarFavoritos(){
        Usuarios.agregarFavoritos(codeUser,codePlace)
        val intent = Intent(this, DetalleLugarActivity::class.java)
        intent.putExtra("code",codePlace)
        finish()
        startActivity(intent)
    }

    fun eliminarFavoritos(){
        Usuarios.eliminarFavoritos(codeUser,codePlace)
        val intent = Intent(this, DetalleLugarActivity::class.java)
        intent.putExtra("code",codePlace)
        finish()
        startActivity(intent)
    }
}