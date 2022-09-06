package com.example.unilocal.models

import java.time.LocalDateTime

class Place (var id:Int,
             var name: String,
             var description: String,
             var images:List<String>,
             var idCreator: Int,
             var status: Boolean,
             var idCategory:Int,
             var latitude: Float,
             var length: Float,
             var idCity:Int,
             var creationDate: LocalDateTime
             ) {

            var phones: List<String> = ArrayList()
}