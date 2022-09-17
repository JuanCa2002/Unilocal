package com.example.unilocal.bd

import com.example.unilocal.models.User

object Usuarios {

    private val usuarios:ArrayList<User> = ArrayList()

    init {
        usuarios.add(User(1,"Juan","Nexus","Juan@gmail.com","1234","22/08/2022"))
        usuarios.add(User(2,"Jaime","Jai","Jaime@gmail.com","1238","22/09/2022"))
        usuarios.add(User(3,"Laura","Lau","Lau@gmail.com","1235","22/07/2022"))
    }

    fun listar():ArrayList<User>{
       return usuarios
    }

    fun agregar(user:User){
        usuarios.add(user)
    }

    fun login(email: String, password: String): User {
        return usuarios.first() { u -> u.email == email && u.password == password }
    }
}