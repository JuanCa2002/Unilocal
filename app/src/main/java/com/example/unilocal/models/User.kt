package com.example.unilocal.models

import android.content.ContentValues
import com.example.unilocal.sqlite.UserContract

class User(){
    var key:String = ""
    var id:Int = 0
    var rol:Rol = Rol.USER
    var nombre:String = ""
    var nickname:String = ""
    var password:String = ""
    var idCity:Int = 0

    constructor(id: Int, nombre: String, nickname:String, idCity:Int, rol:Rol ):this(){
        this.id= id
        this.nombre = nombre
        this.nickname= nickname
        this.password = password
        this.idCity = idCity
        this.rol = rol
    }

    var favorities: ArrayList<String?> = ArrayList()

    fun toContentValues():ContentValues{
        val values = ContentValues()
        values.put(UserContract.NOMBRE,nombre)
        //values.put(UserContract.CORREO,correo)
        values.put(UserContract.NICKNAME,nickname)
        values.put(UserContract.PASSWORD,password)
        values.put(UserContract.ID_CITY, idCity)

        return values
    }

    override fun toString(): String {
        return "User(nombre='$nombre', nickname='$nickname', password='$password')"
    }
}



