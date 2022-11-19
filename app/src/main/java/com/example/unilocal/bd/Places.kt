package com.example.unilocal.bd

import android.util.Log
import android.view.Display
import com.example.unilocal.models.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.sql.Array

object Places {

    private val places: ArrayList<Place> = ArrayList()
        val horario1 = Schedule(1,Schedules.obtenerTodos(),12,29)
        val horario2 = Schedule(2, Schedules.obtenerEntresSemana(),9,20)
        val horario3 = Schedule(3, Schedules.obtenerFinSemana(),14,23)

    fun list():ArrayList<Place>{
        var places: ArrayList<Place> = ArrayList()
        Firebase.firestore
            .collection("placesF")
            .get()
            .addOnSuccessListener {
                for(doc in it){
                    val place = doc.toObject(Place::class.java)
                    place.key = doc.id
                    places.add(place)
                }
            }
        return places
    }
    fun listByStatus(status: StatusPlace):ArrayList<Place>{
           return places.filter { l->l.status == status }.toCollection(ArrayList())
    }

    fun listByUser(codeUser: String,lista:ArrayList<Place>):ArrayList<Place>{
        lista.clear()
        lista.addAll(places.filter { l->l.idCreator== codeUser }.toCollection(ArrayList()))
        return lista
    }

    fun obtener(key:String?): Place?{
        var place:Place? = null
        Firebase.firestore
            .collection("placesF")
            .document(key!!)
            .get()
            .addOnSuccessListener {
               place = it.toObject(Place::class.java)
                Log.e("place", place.toString())
            }
        return place
    }

    fun buscarNombre(nombre:String, lista:ArrayList<Place>, places:ArrayList<Place>): ArrayList<Place> {
        lista.clear()
        lista.addAll(places.filter { l -> l.name.lowercase().contains(nombre.lowercase()) }.toCollection(ArrayList()))
        return lista
    }

    fun crear(place:Place){
        places.add( place )
    }

    fun deletePlace(codePlace:String?){
       Firebase.firestore
           .collection("places")

    }

    fun updatePlace(codePlace: Int, place: Place){
        val placeExist = places.firstOrNull{p-> p.id == codePlace}
        val index = places.indexOf(placeExist)
        if(placeExist!=null){
            places.set(index,place)
        }
    }

}