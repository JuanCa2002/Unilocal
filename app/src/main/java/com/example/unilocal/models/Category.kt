package com.example.unilocal.models

class Category() {

    var key:String = ""
    var name:String = ""
    var icon:String = ""

    constructor(  name:String, icon:String):this(){
        this.name = name
        this.icon = icon
    }

    override fun toString(): String {
        return name
    }
}