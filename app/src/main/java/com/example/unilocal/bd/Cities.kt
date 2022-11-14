package com.example.unilocal.bd

import com.example.unilocal.models.City

object Cities {

    private val cities:ArrayList<City> = ArrayList()

//    init {
//        cities.add(City("Armenia"))
//        cities.add(City("Pereira"))
//        cities.add(City("Cali"))
//        cities.add(City("Bogota"))
//        cities.add(City("Medellin"))
//    }

    fun listar():ArrayList<City>{
        return cities;
    }

    fun obtener(id:String, cities:ArrayList<City>):City?{
        return cities.firstOrNull{c -> c.key == id}
    }
}