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
import com.example.unilocal.bd.Usuarios
import com.example.unilocal.databinding.FragmentMyPlacesBinding
import com.example.unilocal.models.Place
import com.example.unilocal.models.StatusPlace
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


class MyPlacesFragment : Fragment() {
    lateinit var binding: FragmentMyPlacesBinding
    lateinit var adapter: PlaceAdapter
    var placesByUser: ArrayList<Place> = ArrayList()
    var bundle:Bundle = Bundle()
    var user: FirebaseUser? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyPlacesBinding.inflate(inflater,container,false)
        user = FirebaseAuth.getInstance().currentUser
        if(user!= null){
            adapter = PlaceAdapter(placesByUser,"user")
            binding.listPlacesSearch.adapter = adapter
            binding.listPlacesSearch.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false)
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        placesByUser.clear()
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
                        //adapter.notifyItemInserted(placesByUser.size-1)
                    }
                 adapter.notifyDataSetChanged()
                }
        }
    }

}