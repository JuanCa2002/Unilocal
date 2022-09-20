package com.example.unilocal.models

class Category(var id:Int,
               var name:String,
               var icon:String) {

    override fun toString(): String {
        return name
    }
}