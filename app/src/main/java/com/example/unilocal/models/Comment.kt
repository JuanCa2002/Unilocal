package com.example.unilocal.models

import java.time.LocalDateTime
import java.util.*

class Comment (var text:String,
               var idUser:Int,
               var idPlace:Int,
               var qualification: Int) {

    var creationDate: Date = Date()
}