package com.example.unilocal.bd

import com.example.unilocal.models.Category

object Categories {

    private val categories:ArrayList<Category> = ArrayList()

    fun listar():ArrayList<Category>{
        return categories
    }

    fun getById(id:String, categories:ArrayList<Category>): Category? {
        return categories.firstOrNull { c -> c.key == id }
    }
}