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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class InfoPlaceFragment : Fragment() {
    lateinit var binding: FragmentInfoPlaceBinding
    lateinit var placeAdapter: PlaceAdapter
    lateinit var favorites: ArrayList<String?>
    var codePlace:String?= ""
    var code:String? = ""
    var placesFavorites: ArrayList<Place> = ArrayList()
    var pos:Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(arguments != null){
            codePlace = requireArguments().getString("id_lugar")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInfoPlaceBinding.inflate(inflater,container,false)
        val user= FirebaseAuth.getInstance().currentUser
        if(user!= null){
            code = user.uid
        }
        favorites = Usuarios.getListFavorites(code) //TODO Obtener desde firestore
        val places = Places.list()
        for (i in favorites){
            for (j in places){
                if(j.key == i){
                    placesFavorites.add(j)
                }
            }
        }
        placeAdapter = PlaceAdapter(placesFavorites,"Busqueda")
        //val place = Places.obtener(codePlace)

        Firebase.firestore
            .collection("placesF")
            .document(codePlace!!)
            .get()
            .addOnSuccessListener {
                var place = it.toObject(Place::class.java)
                if(place != null){
                    place.key = it.id
                    var telefonos = ""

                    if(place!!.phones.isNotEmpty()) {
                        for (tel in place.phones) {
                            telefonos += "$tel, "
                        }
                        telefonos = telefonos.substring(0, telefonos.length - 2)
                    }else{
                        telefonos = "No hay teléfono"
                    }

                    binding.phonePlace.text = telefonos

                    var horarios = ""

                    for( horario in place!!.schedules ){
                        for(dia in horario.dayOfWeek){
                            horarios += "${dia.toString().lowercase().replaceFirstChar { it.uppercase() }}: ${horario.startTime}:00 - ${horario.finishTime}:00\n"
                        }
                    }

                    binding.schedulePlace.text = horarios

                    if(place != null){
                        val qualification = place.obtenerCalificacionPromedio(Comments.lista(place.key))
                        for (i in 0..qualification){
                            (binding.listStars[i] as TextView).setTextColor(ContextCompat.getColor(binding.listStars.context,R.color.yellow))
                        }
                        binding.txtDescripcionLugar.text = place!!.description
                        binding.txtDireccionLugar.text = place!!.address
                    }
                    val favorito = favorites.firstOrNull{f -> f.equals(codePlace)}
                    if(favorito != null){
                        binding.btnFavorito.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.ic_baseline_favorite_red))
                        binding.btnFavorito.setOnClickListener{eliminarFavoritos()}
                    }else{
                        binding.btnFavorito.setOnClickListener{agregarFavoritos()}
                    }
                }

            }.addOnFailureListener{
                Log.e("Fallo al traer el lugar",it.message.toString())
            }


        return binding.root
    }

    fun agregarFavoritos(){
        binding.btnFavorito.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.ic_baseline_favorite_red))
        binding.btnFavorito.setOnClickListener{eliminarFavoritos()}
        Usuarios.agregarFavoritos(code,codePlace)

    }

    fun eliminarFavoritos(){
        binding.btnFavorito.setImageDrawable(ContextCompat.getDrawable(requireActivity(),
            R.drawable.ic_baseline_favorite_black_24
        ))
        binding.btnFavorito.setOnClickListener{agregarFavoritos()}
        val user= FirebaseAuth.getInstance().currentUser
        if(user!= null){
            Usuarios.eliminarFavoritos(user.uid,codePlace!!)
        }
    }

    companion object{
        fun newInstance(codigoLugar: String):InfoPlaceFragment{
            val args = Bundle()
            args.putString("id_lugar",codigoLugar)
            val fragment = InfoPlaceFragment()
            fragment.arguments = args
            return fragment
        }
    }

}