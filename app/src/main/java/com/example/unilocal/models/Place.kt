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

    fun estaAbierto():Boolean{

        val fecha = Calendar.getInstance()
        val dia = fecha.get(Calendar.DAY_OF_WEEK)
        val hora = fecha.get(Calendar.HOUR_OF_DAY)

        var mensaje = false

        for(schedule in schedules){
            if( schedule.dayOfWeek.contains( DayWeek.values()[dia-1] ) && hora < schedule.finishTime && hora > schedule.startTime  ){
                mensaje = true
                break
            }
        }

        return mensaje
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

    fun obtenerHoraCierre():String{
       val fecha = Calendar.getInstance()
       val dia = fecha.get(Calendar.DAY_OF_WEEK)
       var mensaje = ""

       for(schedule in schedules){
          if(schedule.dayOfWeek.contains(DayWeek.values()[dia-1])){
              mensaje = "${schedule.finishTime}:00"
              break
          }
       }
        return mensaje
    }

    fun obtenerHoraApertura():String{
        val fecha = Calendar.getInstance()
        val dia = fecha.get(Calendar.DAY_OF_WEEK)
        var mensaje = ""
        var pos = 0

        for(schedule in schedules){
            pos = schedule.dayOfWeek.indexOf(DayWeek.values()[dia-1])
            mensaje = if(pos != -1){
                "${schedule.dayOfWeek[pos-1].toString().lowercase()}  a las ${schedule.startTime}:00"
            }else{
                "${schedule.dayOfWeek[0].toString().lowercase()}  a las ${schedule.startTime}:00"
            }
            break
        }
        return mensaje
    }


}