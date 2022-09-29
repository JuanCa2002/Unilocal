package com.example.unilocal.models

open class Person(var id:Int, var nombre:String, var correo:String, var password:String) {

    override fun toString(): String {
        return "Persona(id=$id, nombre='$nombre', correo='$correo', password='$password')"
    }

}