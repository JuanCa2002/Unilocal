package com.example.unilocal.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.unilocal.adapter.PlaceAdapter
import com.example.unilocal.bd.Places
import com.example.unilocal.databinding.ActivityResultadoBusquedaBinding
import com.example.unilocal.models.Place

class ResultadoBusquedaActivity : AppCompatActivity() {
    lateinit var binding: ActivityResultadoBusquedaBinding
    var textSearch:String = ""
    lateinit var placesCoincidences:ArrayList<Place>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultadoBusquedaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        textSearch = intent.extras!!.getString("txt_search","")
        placesCoincidences = ArrayList()

        if(textSearch.isNotEmpty()){
            placesCoincidences= Places.buscarNombre(textSearch)
            Log.e(ResultadoBusquedaActivity::class.java.simpleName, placesCoincidences.toString())
        }

        val adapter = PlaceAdapter(placesCoincidences)
        binding.listPlacesSearch.adapter = adapter
        binding.listPlacesSearch.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)

    }
}