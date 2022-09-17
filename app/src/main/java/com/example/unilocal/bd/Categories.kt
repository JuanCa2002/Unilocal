package com.example.unilocal.bd

import com.example.unilocal.models.Category

object Categories {

    private val categories:ArrayList<Category> = ArrayList()

    init {
        categories.add(Category(1,"Hotel","\uf594"))
        categories.add(Category(2,"Restaurante","\uf0f4"))
        categories.add(Category(3,"Tienda de ropa","\uf553"))
        categories.add(Category(4,"Almacen","\uf290"))
        categories.add(Category(5,"Cafe","\uf0f4"))
    }

    fun listar():ArrayList<Category>{
        return categories
    }

    fun getById(id:Int): Category? {
        return categories.firstOrNull { c -> c.id == id }
    }
}