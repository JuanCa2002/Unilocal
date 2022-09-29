package com.example.unilocal.bd

import com.example.unilocal.models.Moderator


object Moderators {

    private val moderators:ArrayList<Moderator> = ArrayList()

    init {
        moderators.add(Moderator(1,"Helen","Helen@gmail.com","1234"))
        moderators.add(Moderator(2,"Sandro","SandroBestoModerador@hotmail.com","1414"))
    }

    fun obtener(id:Int): Moderator?{
        return moderators.firstOrNull{a -> a.id == id }
    }


    fun listar():ArrayList<Moderator>{
        return moderators
    }

    fun login(email:String, password:String): Moderator {
        val response = moderators.first{a -> a.correo == email && a.password == password}
        return response
    }
}