package com.example.unilocal.models

class City(var id:Int,
           var name:String) {

    override fun toString(): String {
        return name
    }
}