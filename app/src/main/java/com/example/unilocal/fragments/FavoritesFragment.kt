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


class FavoritesFragment : Fragment() {
    lateinit var binding: FragmentFavoritesBinding
    var places: ArrayList<Place> = ArrayList()
    lateinit var adapter: PlaceAdapter
    var code:Int? = -1
    var bundle:Bundle = Bundle()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater,container,false)
        code = this.bundle.getInt("code")

        if(code!=null) {
            Usuarios.getListFavoritesUser(places, code!!)
            //Log.e("FavoritesFragment", placesFavorites.toString())
            adapter = PlaceAdapter(places, "Busqueda")
            binding.listFavorites.adapter = adapter
            binding.listFavorites.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if(code!=null) {
            Usuarios.getListFavoritesUser(places, code!!)
            adapter.notifyDataSetChanged()
        }
    }

}