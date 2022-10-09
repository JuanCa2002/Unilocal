package com.example.unilocal.models

class Administrator(id: Int, nombre: String, nickname:String,  correo: String, password: String, idCity:Int): Person(id, nombre, nickname , correo, password, idCity) {

    override fun toString(): String {
        return "Administrador() ${super.toString()}"
    }
}