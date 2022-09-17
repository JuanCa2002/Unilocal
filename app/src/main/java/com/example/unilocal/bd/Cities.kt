package com.example.unilocal.bd

import com.example.unilocal.models.City

object Cities {

    private val cities:ArrayList<City> = ArrayList()

    init {
        cities.add(City(1,"Armenia"))
        cities.add(City(2,"Pereira"))
        cities.add(City(3,"Cali"))
        cities.add(City(4,"Bogota"))
        cities.add(City(5,"Medellin"))

    }

    fun listar():ArrayList<City>{
        return cities;
    }

    fun obtener(id:Int):City?{
        return cities.firstOrNull{c -> c.id == id}
    }
}