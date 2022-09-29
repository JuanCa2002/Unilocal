package com.example.unilocal.bd

import com.example.unilocal.models.Administrator

object Administrators {

    private val administrators:ArrayList<Administrator> = ArrayList()

    init {
        administrators.add(Administrator(1,"Tommy","Tommy@gmail.com","1234"))
        administrators.add(Administrator(2,"Julio","JulioBestoAdmin@hotmail.com","1414"))
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
}