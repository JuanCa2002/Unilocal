package com.example.unilocal.bd

import com.example.unilocal.models.Category

object Categories {

    private val categories:ArrayList<Category> = ArrayList()

    init {
        categories.add(Category(1,"Hotel"))
        categories.add(Category(2,"Restaurante"))
        categories.add(Category(3,"Tienda de ropa"))
        categories.add(Category(4,"Almacen"))
        categories.add(Category(5,"Cafe"))
    }

    fun listar():ArrayList<Category>{
        return categories
    }

    fun getById(id:Int): Category? {
        return categories.firstOrNull { c -> c.id == id }
    }
}