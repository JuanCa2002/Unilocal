package com.example.unilocal.bd

import com.example.unilocal.models.Comment
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

object Comments {

    private val comments:ArrayList<Comment> = ArrayList()


    init {
        comments.add(Comment(1,"Excelente servicio y buen ambiente",1,2,5))
        comments.add(Comment(2,"Muy demorado no volveria ",4,1,2))
        comments.add(Comment(3,"Buena comida, precios razonables",3,3,4))
        comments.add(Comment(4,"El lugar es muy bonito pero muy lento",2,2,3))
        comments.add(Comment(5,"Los precios son muy altos ",5,2,2))
        comments.add(Comment(6,"Genia el hotel",1,5,4))
    }

    fun lista(idLugar:Int):ArrayList<Comment>{
        return comments.filter { c -> c.idPlace == idLugar }.toCollection(ArrayList())
    }

    fun crearComentario(comment:Comment){
        comments.add(comment)
    }
}