package com.example.unilocal.bd

import com.example.unilocal.models.Administrator
import com.example.unilocal.models.Moderator

object Administrators {

    private val administrators:ArrayList<Administrator> = ArrayList()

    init {
        administrators.add(Administrator(1,"Tommy","ElTomy","Tommy@gmail.com","1234",1))
        administrators.add(Administrator(2,"Julio","ElJulio","JulioBestoAdmin@hotmail.com","1414",2))
    }

    fun obtener(id:Int):Administrator?{
        return administrators.firstOrNull{a -> a.id == id }
    }

    fun listar():ArrayList<Administrator>{
        return administrators
    }

    fun login(email:String, password:String): Administrator{
        val response = administrators.first{a -> a.correo == email && a.password == password}
        return response
    }

    fun updateAdministrator( administrator: Administrator,id: Int){
        var administratorExist = administrators.firstOrNull{ u -> u.id == id}
        if(administratorExist!= null){
            var index = administrators.indexOf(administratorExist)
           administrators.set(index, administrator)
        }
    }
}