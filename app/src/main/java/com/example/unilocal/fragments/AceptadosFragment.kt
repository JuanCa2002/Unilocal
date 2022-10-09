package com.example.unilocal.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.unilocal.R
import com.example.unilocal.adapter.PlaceAdapter
import com.example.unilocal.bd.Places
import com.example.unilocal.databinding.FragmentAceptadosBinding
import com.example.unilocal.databinding.FragmentRegistreBinding
import com.example.unilocal.models.Place

class AceptadosFragment : Fragment() {
    lateinit var binding: FragmentAceptadosBinding
    var codeModerator: Int = -1
    lateinit var adapterPlace: PlaceAdapter
    lateinit var places: ArrayList<Place>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(arguments != null){
            codeModerator = requireArguments().getInt("code_moderator")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAceptadosBinding.inflate(inflater,container,false)
        places = Places.placesAcceptedByModerator(codeModerator)
        adapterPlace = PlaceAdapter(places,"Busqueda")
        binding.listPlacesAceptados.adapter = adapterPlace
        binding.listPlacesAceptados.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
        return binding.root
    }

    companion object{
        fun newInstance(codeModerator: Int):AceptadosFragment{
            val args = Bundle()
            args.putInt("code_moderator",codeModerator)
            val fragment = AceptadosFragment()
            fragment.arguments = args
            return fragment

        }
    }
}