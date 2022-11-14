package com.example.unilocal.fragments

import android.content.Context
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
import com.example.unilocal.bd.Usuarios
import com.example.unilocal.databinding.FragmentFavoritesBinding
import com.example.unilocal.databinding.FragmentMyPlacesBinding
import com.example.unilocal.models.Place
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


class FavoritesFragment : Fragment() {
    lateinit var binding: FragmentFavoritesBinding
    lateinit var adapter: PlaceAdapter
    var places: ArrayList<Place> = ArrayList()
    var code:String? = ""
    var bundle:Bundle = Bundle()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater,container,false)
        var user = FirebaseAuth.getInstance().currentUser
        if(user!=null) {
            code = user.uid
            adapter = PlaceAdapter(places, "Busqueda")
            binding.listFavorites.adapter = adapter
            binding.listFavorites.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }
        return binding.root
    }

    override fun onPause() {
        super.onPause()
        places.clear()
        Firebase.firestore
            .collection("users")
            .document(code!!)
            .collection("favorites")
            .get()
            .addOnSuccessListener {
                for(doc in it){
                    Firebase.firestore
                        .collection("placesF")
                        .document(doc.id)
                        .get()
                        .addOnSuccessListener {l->
                            val place = l.toObject(Place::class.java)
                            place!!.key = l.id
                            places.add(place)
                            adapter.notifyDataSetChanged()
                        }
                }
            }
    }

    override fun onResume() {
        super.onResume()
        places.clear()
        Firebase.firestore
            .collection("users")
            .document(code!!)
            .collection("favorites")
            .get()
            .addOnSuccessListener {
                for(doc in it){
                    Firebase.firestore
                        .collection("placesF")
                        .document(doc.id)
                        .get()
                        .addOnSuccessListener {l->
                            val place = l.toObject(Place::class.java)
                            place!!.key = l.id
                            places.add(place)
                            adapter.notifyDataSetChanged()
                        }
                }
            }
    }

}