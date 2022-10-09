package com.example.unilocal.bd

import androidx.recyclerview.widget.RecyclerView
import com.example.unilocal.models.Moderator
import com.example.unilocal.models.User


object Moderators {

    private val moderators:ArrayList<Moderator> = ArrayList()

    init {
        moderators.add(Moderator(1,"Helen","helen","Helen@gmail.com","1234",1))
        moderators.add(Moderator(2,"Sandro","san","SandroBestoModerador@hotmail.com","1414",5))
    }

    fun obtener(id:Int): Moderator?{
        return moderators.firstOrNull{a -> a.id == id }
    }


    fun listar():ArrayList<Moderator>{
        return moderators
    }

    fun deleteModerator(id:Int){
        val moderator = moderators.firstOrNull{m-> m.id== id}
        if(moderator != null){
            moderators.remove(moderator)
        }
    }

    fun createModerator(moderator: Moderator){
        moderators.add(moderator)
    }

    fun updateModerator( moderator: Moderator,id: Int){
        var moderatorExist = moderators.firstOrNull{ u -> u.id == id}
        if(moderatorExist!= null){
            var index = moderators.indexOf(moderatorExist)
           moderators.set(index, moderator)
        }
    }
}