package com.example.unilocal.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.unilocal.R
import com.example.unilocal.activities.CrearLugarActivity
import com.example.unilocal.adapter.PlaceAdapter
import com.example.unilocal.bd.Places
import com.example.unilocal.databinding.FragmentMyPlacesBinding
import com.example.unilocal.models.Place


class MyPlacesFragment : Fragment() {
    lateinit var binding: FragmentMyPlacesBinding
    lateinit var placesByUser: ArrayList<Place>
    var code:Int? = -1
    var bundle:Bundle = Bundle()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyPlacesBinding.inflate(inflater,container,false)
        code = this.bundle.getInt("code")
        placesByUser = Places.listByUser(code!!)
        Log.e("Lugares",code.toString())
        val adapter = PlaceAdapter(placesByUser,"user")
        binding.listPlacesSearch.adapter = adapter
        binding.listPlacesSearch.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false)
        return binding.root
    }


    fun irCrearLugar(){
        val intent = Intent(activity, CrearLugarActivity::class.java)
        startActivity(intent)
    }
}