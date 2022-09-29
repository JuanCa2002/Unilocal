package com.example.unilocal.models

class Administrator(id: Int, nombre: String, correo: String, password: String): Person(id, nombre, correo, password) {

    override fun toString(): String {
        return "Administrador() ${super.toString()}"
    }
}