package com.example.unilocal.models

import java.util.*

class Comment()  {
    var creationDate: Date = Date()
    var text:String = ""
    var key:String = ""
    var idUser:String? = ""
    var qualification: Int = 0

    constructor( text:String, idUser:String?, qualification: Int):this(){
        this.text = text
        this.idUser = idUser
        this.qualification = qualification
    }

}