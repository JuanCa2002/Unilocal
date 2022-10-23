package com.example.unilocal.models

class Position (){

    var lat:Double = 0.0
    var lng:Double = 0.0

    constructor(lat:Double, lng:Double):this(){
        this.lng = lng
        this.lat = lat
    }

    override fun toString(): String {
        return "Position(lat=$lat, lng=$lng)"
    }
}