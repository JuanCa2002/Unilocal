package com.example.unilocal.models

class Category() {

    var id:Int = 0
    var key:String = ""
    var name:String = ""
    var icon:String = ""

    constructor( id:Int, name:String, icon:String):this(){
        this.id = id
        this.name = name
        this.icon = icon
    }

    override fun toString(): String {
        return name
    }
}