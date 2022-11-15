package com.example.unilocal.bd

import com.example.unilocal.models.Category

object Categories {

    private val categories:ArrayList<Category> = ArrayList()

//    init {
//        categories.add(Category("Hotel","\uf594"))
//        categories.add(Category("Restaurante","\uf0f4"))
//        categories.add(Category("Tienda de ropa","\uf553"))
//        categories.add(Category("Almacen","\uf290"))
//        categories.add(Category("Cafe","\uf0f4"))
//    }

    fun listar():ArrayList<Category>{
        return categories
    }

    fun getById(id:String, categories:ArrayList<Category>): Category? {
        return categories.firstOrNull { c -> c.key == id }
    }
}