package com.example.unilocal.models

import android.content.ContentValues
import com.example.unilocal.sqlite.UserContract

class User(id: Int, nombre: String, nickname:String, correo: String, password: String, idCity:Int ): Person(id, nombre,nickname, correo, password, idCity){

    override fun toString(): String {
        return "Usuario(nickname='$nickname') ${super.toString()}"
    }
    var favorities: ArrayList<Int> = ArrayList()

    fun toContentValues():ContentValues{
        val values = ContentValues()
        values.put(UserContract.NOMBRE,nombre)
        values.put(UserContract.CORREO,correo)
        values.put(UserContract.NICKNAME,nickname)
        values.put(UserContract.PASSWORD,password)
        values.put(UserContract.ID_CITY, idCity)

        return values
    }
}



