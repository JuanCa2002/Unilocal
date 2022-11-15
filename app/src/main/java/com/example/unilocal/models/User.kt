package com.example.unilocal.models

import android.content.ContentValues
import com.example.unilocal.sqlite.UserContract

class User(){
    var key:String = ""
    var correo:String = ""
    var status:StatusUser = StatusUser.HABILITADO
    var rol:Rol = Rol.USER
    var nombre:String = ""
    var nickname:String = ""
    var idCity:String = ""

    constructor( nombre: String, nickname:String,correo:String, idCity:String, rol:Rol ):this(){
        this.correo = correo
        this.nombre = nombre
        this.nickname= nickname
        this.idCity = idCity
        this.rol = rol
    }
    constructor( key:String, nombre: String, nickname:String,correo:String, idCity:String ):this(){
        this.correo = correo
        this.key = key
        this.nombre = nombre
        this.nickname= nickname
        this.idCity = idCity
    }

    var favorities: ArrayList<String?> = ArrayList()

    fun toContentValues():ContentValues{
        val values = ContentValues()
        values.put(UserContract.ID, key)
        values.put(UserContract.NOMBRE,nombre)
        values.put(UserContract.CORREO,correo)
        values.put(UserContract.NICKNAME,nickname)
        values.put(UserContract.ID_CITY, idCity)

        return values
    }

    override fun toString(): String {
        return "User(nombre='$nombre', nickname='$nickname')"
    }
}



