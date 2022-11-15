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
import com.example.unilocal.models.StatusPlace
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RechazadosFragment : Fragment() {
    lateinit var binding: FragmentRechazadosBinding
    var codeModerator:String = ""
    lateinit var adapterPlace: PlaceAdapter
    lateinit var places: ArrayList<Place>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =  FragmentRechazadosBinding.inflate(inflater,container,false)
        var user = FirebaseAuth.getInstance().currentUser
        if(user!= null){
            codeModerator = user.uid
        }
        places = ArrayList()
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

    override fun onResume() {
        super.onResume()
        places.clear()
        Firebase.firestore
            .collection("placesF")
            .whereEqualTo("status", StatusPlace.RECHAZADO)
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