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

//    init {
//        val place1= Place(1,"Los cucos","Muy genial",1,StatusPlace.ACEPTADO,2, Position(4.550923, -75.6557201), "Cra 15 #3-56",1)
//        place1.schedules.add(horario2)
//
//        val place2= Place(2,"Restaurante mariana","Muy genial",2,StatusPlace.ACEPTADO,2,Position(4.5301703, -75.6933855),"Cra 13 #3-56",2)
//        place2.schedules.add(horario1)
//
//        val place3= Place(3,"Cafe tigres","Muy genial",3,StatusPlace.ACEPTADO,5,Position(4.5278566, -75.6933958),"Cra 14 #3-56",3)
//        place3.schedules.add(horario1)
//
//        val place4= Place(4,"Hotel genial","Muy genial",1,StatusPlace.ACEPTADO,1,Position(4.5270961, -75.695078),"Cra 16 #3-56",4)
//        place4.schedules.add(horario3)
//
//        val place5= Place(5,"Hotel zarpos","Muy genial",1,StatusPlace.ACEPTADO,3,Position(4.5270715, -75.6949206),"Cra 19 #3-56",2)
//        place5.schedules.add(horario1)
//
//        val place6= Place(6,"Mall","Muy genial",2,StatusPlace.SIN_REVISAR,4,Position(4.5301703, -75.6933855),"Cra 18 #3-56",5)
//        place6.schedules.add(horario2)
//
//        places.add(place1)
//        places.add(place2)
//        places.add(place3)
//        places.add(place4)
//        places.add(place5)
//        places.add(place6)
//    }


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
       // Log.e("placeEEE",place.toString())
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

    fun buscarCiudad(codigoCiudad:Int): ArrayList<Place> {
        return places.filter { l -> l.idCity == codigoCiudad && l.status == StatusPlace.ACEPTADO}.toCollection(ArrayList())
    }

//    fun buscarCategoria(codigoCategoria:Int): ArrayList<Place> {
//        return places.filter { l -> l.idCategory == codigoCategoria && l.status == StatusPlace.ACEPTADO }.toCollection(ArrayList())
//    }

    fun changeStatus(codePlace:String?, status: StatusPlace, codeModerator: Int){
        val place = places.firstOrNull{p -> p.key == codePlace}
        if(place!=null){
            place.idModeratorReview = codeModerator
            place.status= status
        }

    }

    fun deletePlace(codePlace:String?){
       Firebase.firestore
           .collection("places")

    }

    fun placesAcceptedByModerator(codeModerator:Int):ArrayList<Place>{
        return places.filter{p -> p.idModeratorReview == codeModerator && p.status == StatusPlace.ACEPTADO}.toCollection(ArrayList())
    }

    fun placesDeclinedByModerator(codeModerator:Int):ArrayList<Place>{
        return places.filter{p -> p.idModeratorReview == codeModerator && p.status == StatusPlace.RECHAZADO}.toCollection(ArrayList())
    }

    fun updatePlace(codePlace: Int, place: Place){
        val placeExist = places.firstOrNull{p-> p.id == codePlace}
        val index = places.indexOf(placeExist)
        if(placeExist!=null){
            places.set(index,place)
        }
    }

}