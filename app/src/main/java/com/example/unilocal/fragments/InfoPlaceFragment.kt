package com.example.unilocal.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.get
import com.example.unilocal.R
import com.example.unilocal.adapter.PlaceAdapter
import com.example.unilocal.bd.Comments
import com.example.unilocal.bd.Places
import com.example.unilocal.bd.Usuarios
import com.example.unilocal.databinding.FragmentInfoPlaceBinding
import com.example.unilocal.models.Place


class InfoPlaceFragment : Fragment() {

    lateinit var binding: FragmentInfoPlaceBinding
    var codePlace:Int = -1
    lateinit var placeAdapter: PlaceAdapter
    lateinit var favorites: ArrayList<Int>
    var placesFavorites: ArrayList<Place> = ArrayList()
    var pos:Int = -1
    var codeUser:Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(arguments != null){
            codePlace = requireArguments().getInt("id_lugar")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInfoPlaceBinding.inflate(inflater,container,false)
        val sp = requireActivity().getSharedPreferences("sesion", Context.MODE_PRIVATE)

        codeUser = sp.getInt("id",0)

        favorites = Usuarios.getListFavorites(codeUser)

        val places = Places.list()
        val usuario = Usuarios.getUser(codeUser!!)
        for (i in usuario!!.favorities){
            for (j in places){
                if(j.id == i){
                    placesFavorites.add(j)
                }
            }
        }

        placeAdapter = PlaceAdapter(placesFavorites,"Busqueda")

        //pos = requireActivity().intent.extras!!.getInt("position")

        val place = Places.obtener(codePlace)

        if(place != null){

            val qualification = place.obtenerCalificacionPromedio(Comments.lista(place.id))
            for (i in 0..qualification){
                (binding.listStars[i] as TextView).setTextColor(ContextCompat.getColor(binding.listStars.context,R.color.yellow))
            }
            //Hay que agregar un campo de telefono.
            binding.txtDescripcionLugar.text = place!!.description
            binding.txtDireccionLugar.text = place!!.address
        }
        val favorito = favorites.firstOrNull{f -> f == codePlace}
        if(favorito != null){
            binding.btnFavorito.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.ic_baseline_favorite_red))
            binding.btnFavorito.setOnClickListener{eliminarFavoritos()}

        }else{
            //Log.e("prueba","se agrego")
            binding.btnFavorito.setOnClickListener{agregarFavoritos()}
        }
        return binding.root
    }

    fun agregarFavoritos(){
        binding.btnFavorito.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.ic_baseline_favorite_red))
        binding.btnFavorito.setOnClickListener{eliminarFavoritos()}
        Usuarios.agregarFavoritos(codeUser,codePlace)
    }

    fun eliminarFavoritos(){
        binding.btnFavorito.setImageDrawable(ContextCompat.getDrawable(requireActivity(),
            R.drawable.ic_baseline_favorite_black_24
        ))
        binding.btnFavorito.setOnClickListener{agregarFavoritos()}
        Usuarios.eliminarFavoritos(codeUser,codePlace)
    }

    companion object{
        fun newInstance(codigoLugar: Int):InfoPlaceFragment{
            val args = Bundle()
            args.putInt("id_lugar",codigoLugar)
            val fragment = InfoPlaceFragment()
            fragment.arguments = args
            return fragment

        }
    }


}