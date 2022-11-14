package com.example.unilocal.models

class City() {

    constructor( id:Int, name:String):this(){
        this.id = id
        this.name = name
    }

    var id:Int = 0
    var key:String =""
    var name:String = ""

    override fun toString(): String {
        return name
    }
}