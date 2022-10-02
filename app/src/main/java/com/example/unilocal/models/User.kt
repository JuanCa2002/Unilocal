package com.example.unilocal.models

class User(id: Int, nombre: String, var nickname:String, correo: String, password: String): Person(id, nombre, correo, password){

    override fun toString(): String {
        return "Usuario(nickname='$nickname') ${super.toString()}"
    }
    var favorities: ArrayList<Int> = ArrayList()
}



