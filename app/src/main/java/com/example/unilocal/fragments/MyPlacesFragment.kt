package com.example.unilocal.fragments

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.unilocal.R
import com.example.unilocal.activities.CrearLugarActivity
import com.example.unilocal.activities.MainActivity
import com.example.unilocal.adapter.PlaceAdapter
import com.example.unilocal.bd.Places
import com.example.unilocal.bd.Usuarios
import com.example.unilocal.databinding.FragmentMyPlacesBinding
import com.example.unilocal.models.Place
import com.example.unilocal.models.StatusPlace
import com.example.unilocal.sqlite.UniLocalDbHelper
import com.example.unilocal.utils.ConectionStatus
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


class MyPlacesFragment : Fragment() {
    lateinit var binding: FragmentMyPlacesBinding
    lateinit var adapter: PlaceAdapter
    lateinit var bd: UniLocalDbHelper
    var placesByUser: ArrayList<Place> = ArrayList()
    var bundle:Bundle = Bundle()
    var estadoConexion: Boolean = false
    var user: FirebaseUser? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyPlacesBinding.inflate(inflater,container,false)
        user = FirebaseAuth.getInstance().currentUser
        bd = UniLocalDbHelper(requireContext())
        adapter = PlaceAdapter(placesByUser,"user")
        binding.listPlacesSearch.adapter = adapter
        binding.listPlacesSearch.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        placesByUser.clear()
        val estado = (requireActivity()as MainActivity).estadoConexion
        if(estado){
            if(user!=null) {
                Firebase.firestore
                    .collection("placesF")
                    .whereEqualTo("idCreator", user!!.uid)
                    .whereEqualTo("status",StatusPlace.ACEPTADO)
                    .get()
                    .addOnSuccessListener {
                        for(doc in it){
                            val place = doc.toObject(Place::class.java)
                            place.key = doc.id
                            placesByUser.add(place)
                        }
                        adapter.notifyDataSetChanged()
                    }
            }
        }else{
            Snackbar.make(binding.root, getString(R.string.no_cargar_apartado), Snackbar.LENGTH_LONG).show()
        }
    }

}