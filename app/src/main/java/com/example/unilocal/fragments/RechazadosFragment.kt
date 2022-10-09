package com.example.unilocal.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.unilocal.R
import com.example.unilocal.adapter.PlaceAdapter
import com.example.unilocal.bd.Places
import com.example.unilocal.databinding.FragmentAceptadosBinding
import com.example.unilocal.databinding.FragmentRechazadosBinding
import com.example.unilocal.models.Place

class RechazadosFragment : Fragment() {
    lateinit var binding: FragmentRechazadosBinding
    var codeModerator:Int = -1
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
        binding =  FragmentRechazadosBinding.inflate(inflater,container,false)
        places = Places.placesDeclinedByModerator(codeModerator)
        adapterPlace = PlaceAdapter(places,"Busqueda")
        binding.listPlacesRechazados.adapter = adapterPlace
        binding.listPlacesRechazados.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
        return binding.root
    }

    companion object{
        fun newInstance(codeModerator: Int):RechazadosFragment{
            val args = Bundle()
            args.putInt("code_moderator",codeModerator)
            val fragment = RechazadosFragment()
            fragment.arguments = args
            return fragment

        }
    }

}