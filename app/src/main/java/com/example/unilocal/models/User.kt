package com.example.unilocal.models

class User(id: Int, nombre: String, nickname:String, correo: String, password: String, idCity:Int ): Person(id, nombre,nickname, correo, password, idCity){

    override fun toString(): String {
        return "Usuario(nickname='$nickname') ${super.toString()}"
    }
    var favorities: ArrayList<Int> = ArrayList()
}



