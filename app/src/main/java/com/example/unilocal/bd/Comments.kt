package com.example.unilocal.bd

import com.example.unilocal.models.Comment
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

object Comments {

    private val comments:ArrayList<Comment> = ArrayList()

//    init {
//        comments.add(Comment("Excelente servicio y buen ambiente",1,,5))
//        comments.add(Comment("Muy demorado no volveria ",1,1,2))
//        comments.add(Comment("Buena comida, precios razonables",3,3,4))
//        comments.add(Comment("El lugar es muy bonito pero muy lento",2,2,3))
//        comments.add(Comment("Los precios son muy altos ",2,2,2))
//        comments.add(Comment("Genia el hotel",1,5,4))
//    }

    fun lista(idLugar:String):ArrayList<Comment>{
        return comments.filter { c -> c.key == idLugar }.toCollection(ArrayList())
    }

    fun crearComentario(comment:Comment){
        comments.add(comment)
    }
}