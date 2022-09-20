package com.example.unilocal.bd

import com.example.unilocal.models.Comment
import com.example.unilocal.models.Place
import com.example.unilocal.models.Schedule
import java.sql.Array

object Places {

    private val places: ArrayList<Place> = ArrayList()
        val horario1 = Schedule(1,Schedules.obtenerTodos(),12,29)
        val horario2 = Schedule(2, Schedules.obtenerEntresSemana(),9,20)
        val horario3 = Schedule(3, Schedules.obtenerFinSemana(),14,23)


    init {
        val place1= Place(1,"Los cucos","Muy genial",1,true,2,48.3434f,"Cra 15 #3-56",82.455f,1)
        place1.schedules.add(horario2)

        val place2= Place(2,"Restaurante mariana","Muy genial",2,true,2,48.3434f,"Cra 13 #3-56",82.455f,2)
        place2.schedules.add(horario1)

        val place3= Place(3,"Cafe tigres","Muy genial",3,true,5,48.3434f,"Cra 14 #3-56",82.455f,3)
        place3.schedules.add(horario1)

        val place4= Place(4,"Hotel genial","Muy genial",1,true,1,48.3434f,"Cra 16 #3-56",82.455f,4)
        place4.schedules.add(horario3)

        val place5= Place(5,"Hotel zarpos","Muy genial",1,true,3,48.3434f,"Cra 19 #3-56",82.455f,2)
        place5.schedules.add(horario1)

        val place6= Place(6,"Mall","Muy genial",2,false,4,48.3434f,"Cra 18 #3-56",82.455f,5)
        place6.schedules.add(horario2)

        places.add(place1)
        places.add(place2)
        places.add(place3)
        places.add(place4)
        places.add(place5)
        places.add(place6)
    }

    fun listarAprobados():ArrayList<Place>{
        return places.filter { l -> l.status }.toCollection(ArrayList())
    }

    fun listarRechazados():ArrayList<Place>{
        return places.filter { l -> !l.status }.toCollection(ArrayList())
    }

    fun obtener(id:Int): Place?{
        return places.firstOrNull { l -> l.id == id }
    }

    fun buscarNombre(nombre:String): ArrayList<Place> {
        return places.filter { l -> l.name.lowercase().contains(nombre.lowercase()) && l.status }.toCollection(ArrayList())
    }

    fun crear(place:Place){
        places.add( place )
    }

    fun buscarCiudad(codigoCiudad:Int): ArrayList<Place> {
        return places.filter { l -> l.idCity == codigoCiudad && l.status }.toCollection(ArrayList())
    }

    fun buscarCategoria(codigoCategoria:Int): ArrayList<Place> {
        return places.filter { l -> l.idCategory == codigoCategoria && l.status }.toCollection(ArrayList())
    }

}