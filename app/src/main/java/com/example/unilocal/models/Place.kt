package com.example.unilocal.models

import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

class Place (var id:Int,
             var name: String,
             var description: String,
             var idCreator: Int,
             var status: StatusPlace,
             var idCategory:Int,
             var latitude: Float,
             var address:String,
             var length: Float,
             var idCity:Int,

             ) {
            var creationDate: Date = Date()
            var images:ArrayList<String> = ArrayList()
            var schedules:ArrayList<Schedule> = ArrayList()
            var phones: List<String> = ArrayList()
    override fun toString(): String {
        return "Place(id=$id, name='$name', description='$description', idCreator=$idCreator, status=$status, idCategory=$idCategory, latitude=$latitude, address='$address', length=$length, idCity=$idCity, creationDate=$creationDate, schedules=$schedules)"
    }


    fun obtenerCalificacionPromedio(comentarios:ArrayList<Comment>):Int{
        var promedio = 0
        if(comentarios.size >0) {
            val suma = comentarios.stream().map { c -> c.qualification }
                .reduce { suma, valor -> suma + valor }.get()
            promedio = suma / comentarios.size
        }
      return promedio
    }


}