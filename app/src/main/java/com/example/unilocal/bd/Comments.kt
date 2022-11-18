package com.example.unilocal.bd

import com.example.unilocal.models.Comment
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

object Comments {

    private val comments:ArrayList<Comment> = ArrayList()

    fun lista(idLugar:String):ArrayList<Comment>{
        return comments.filter { c -> c.key == idLugar }.toCollection(ArrayList())
    }

    fun crearComentario(comment:Comment){
        comments.add(comment)
    }
}