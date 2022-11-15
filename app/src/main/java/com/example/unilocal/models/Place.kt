package com.example.unilocal.models

import android.content.ContentValues
import com.example.unilocal.sqlite.PlaceContract
import com.example.unilocal.sqlite.UserContract
import java.time.LocalDateTime
import java.util.*
import java.util.function.DoubleUnaryOperator
import kotlin.collections.ArrayList

class Place () {


    constructor( id:Int, name: String, description: String, idCreator: String, status: StatusPlace, idCategory:String, position: Position, address:String, idCity:String) :this(){
                    this.id = id
                    this.name = name
                    this.description = description
                    this.idCreator = idCreator
                    this.status = status
                    this.idCategory = idCategory
                    this.position = position
                    this.address = address
                    this.idCity = idCity
                 }
            var id:Int = 0
            var key:String = ""
            var name: String = ""
            var description: String= ""
            var idCreator: String = ""
            var status: StatusPlace = StatusPlace.SIN_REVISAR
            var idCategory:String = ""
            var position: Position? = null
            var address:String = ""
            var idCity:String = ""
            var idModeratorReview: String = ""
            var creationDate: Date = Date()
            var images:ArrayList<String> = ArrayList()
            var schedules:ArrayList<Schedule> = ArrayList()
            var phones: List<String> = ArrayList()

    constructor(id:Int, name:String, description: String, lat: Double, lng: Double, address: String, idCategory: String, idCreator: String):this(){
        this.id = id
        this.name = name
        this.description = description
        val pos = Position(lat, lng)
        this.position = pos
        this.address = address
        this.idCategory = idCategory
        this.idCreator = idCreator
    }

    constructor(id:Int):this(){
        this.id = id
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
                "${schedule.dayOfWeek[0].toString().lowercase()}  a las ${schedule.startTime}:00"
            }else{
                "${schedule.dayOfWeek[0].toString().lowercase()}  a las ${schedule.startTime}:00"
            }
            break
        }
        return mensaje
    }

    fun toContentValues(): ContentValues {
        val values = ContentValues()
        values.put(PlaceContract.NOMBRE,id)
        values.put(PlaceContract.KEY_FIREBASE,key)
        values.put(PlaceContract.NOMBRE,name)
        values.put(PlaceContract.DESCRIPCION,description)
        values.put(PlaceContract.DIRECCION,address)
        values.put(PlaceContract.LAT,position?.lat)
        values.put(PlaceContract.LNG, position?.lng)
        values.put(PlaceContract.CATEGORIA,idCategory)
        values.put(PlaceContract.ID_CREADOR, idCreator)

        return values
    }

    override fun toString(): String {
        return "Place(id=$id, name='$name', description='$description', idCreator=$idCreator, status=$status, idCategory=$idCategory, position=$position, address='$address', idCity=$idCity, idModeratorReview=$idModeratorReview, creationDate=$creationDate, images=$images, schedules=$schedules, phones=$phones)"
    }


}