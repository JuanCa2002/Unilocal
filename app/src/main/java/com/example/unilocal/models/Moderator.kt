package com.example.unilocal.models

class Moderator(id: Int, nombre: String,var nickname:String, correo: String, password: String): Person(id, nombre, correo, password) {

    override fun toString(): String {
        return "Moderador() ${super.toString()}"
    }
}