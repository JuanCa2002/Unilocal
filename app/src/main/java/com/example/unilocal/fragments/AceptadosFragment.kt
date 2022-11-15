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
import com.example.unilocal.models.StatusPlace
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class AceptadosFragment : Fragment() {
    lateinit var binding: FragmentAceptadosBinding
    lateinit var adapterPlace: PlaceAdapter
    lateinit var places: ArrayList<Place>
    var codeModerator: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAceptadosBinding.inflate(inflater,container,false)
        var user = FirebaseAuth.getInstance().currentUser
        if(user!= null){
            codeModerator = user.uid
        }
        places = ArrayList()
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

    override fun onResume() {
        super.onResume()
        places.clear()
        Firebase.firestore
            .collection("placesF")
            .whereEqualTo("status", StatusPlace.ACEPTADO)
            .whereEqualTo("idModeratorReview",codeModerator)
            .get()
            .addOnSuccessListener {
                for(doc in it){
                    val place = doc.toObject(Place::class.java)
                    place.key = doc.id
                    places.add(place)
                }
                adapterPlace.notifyDataSetChanged()
            }
    }
}