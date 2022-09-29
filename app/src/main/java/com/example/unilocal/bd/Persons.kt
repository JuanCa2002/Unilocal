package com.example.unilocal.bd

import com.example.unilocal.models.Person

object Persons {
    fun login(correo:String, password:String): Person?{
        var respuesta:Person?

        respuesta = Usuarios.listar().firstOrNull{ u -> u.password == password && u.correo == correo }

        if(respuesta == null){
            respuesta = Moderators.listar().firstOrNull{ u -> u.password == password && u.correo == correo }

            if(respuesta == null) {
                respuesta = Administrators.listar().firstOrNull{ u -> u.password == password && u.correo == correo }
            }
        }

        return respuesta
    }
}