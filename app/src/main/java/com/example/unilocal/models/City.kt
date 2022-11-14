package com.example.unilocal.models

class City() {

    constructor( name:String):this(){
        this.name = name
    }

    var key:String =""
    var name:String = ""

    override fun toString(): String {
        return name
    }
}