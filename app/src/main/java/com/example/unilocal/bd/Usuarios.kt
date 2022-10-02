package com.example.unilocal.bd

import com.example.unilocal.models.User

object Usuarios {

    private val usuarios:ArrayList<User> = ArrayList()

    init {
        usuarios.add(User(1,"Juan","Nexus","Juan@gmail.com","1234"))
        usuarios.add(User(2,"Jaime","Jai","Jaime@gmail.com","1238"))
        usuarios.add(User(3,"Laura","Lau","Lau@gmail.com","1235"))
    }

    fun listar():ArrayList<User>{
       return usuarios
    }

    fun getUser(code:Int):User?{
        return usuarios.firstOrNull { u -> u.id == code }
    }

    fun agregar(user:User){
        usuarios.add(user)
    }

    fun login(email: String, password: String): User? {
        return usuarios.firstOrNull{ u -> u.correo == email && u.password == password }
    }

    fun getListFavorites(id:Int):ArrayList<Int>{
        val usuario= usuarios.firstOrNull{u -> u.id== id}
        return usuario!!.favorities
    }

    fun agregarFavoritos(id:Int,idPlace:Int){
        val usuario= usuarios.firstOrNull{u -> u.id== id}
        val index = usuarios.indexOf(usuario)
        usuario!!.favorities.add(idPlace)
        usuarios.set(index, usuario)
    }

    fun eliminarFavoritos(id:Int,idPlace:Int){
        val usuario= usuarios.firstOrNull{u -> u.id== id}
        val index = usuarios.indexOf(usuario)
        usuario!!.favorities.remove(idPlace)
        usuarios.set(index, usuario)
    }
}