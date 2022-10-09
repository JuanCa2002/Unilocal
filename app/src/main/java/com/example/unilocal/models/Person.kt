package com.example.unilocal.models

import android.provider.ContactsContract

open class Person(var id:Int, var nombre:String, var nickname:String, var correo:String, var password:String, var idCity:Int) {

    override fun toString(): String {
        return "Persona(id=$id, nombre='$nombre', correo='$correo', password='$password')"
    }

}