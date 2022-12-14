package com.example.unilocal.bd

import android.util.Log
import com.example.unilocal.models.Place
import com.example.unilocal.models.Rol
import com.example.unilocal.models.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

object Usuarios {

    private val usuarios:ArrayList<User> = ArrayList()

    fun listar():ArrayList<User>{
       return usuarios
    }

    fun deleteUser(id:String?){
        val user = usuarios.firstOrNull{ p -> p.key == id }
        if(user!=null){
           usuarios.remove(user)
        }
    }

    fun getUser(code:String?):User?{
        var user: User? = null
        Firebase.firestore
            .collection("users")
            .document(code!!)
            .get()
            .addOnSuccessListener {
                user = it.toObject(User::class.java)
            }
        return user
    }

    fun agregar(user:User){
        usuarios.add(user)
    }

    fun listarModeradores():ArrayList<User>{
        return usuarios.filter { u -> u.rol == Rol.MODERATOR }.toCollection(ArrayList())
    }

    fun getListFavorites(id:String?):ArrayList<String?>{
        var favorities: ArrayList<String?> = ArrayList()
        Firebase.firestore
            .collection("users")
            .document(id!!)
            .collection("favorites")
            .get()
            .addOnSuccessListener {
                for (doc in it){
                    var favorite = doc.id
                    favorities.add(favorite)
                    Log.e("numbers", favorities.toString())
                }
            }
        return favorities
    }

    fun getListFavoritesUser(places:ArrayList<Place>, idUser:String?):ArrayList<Place>{
        places.clear()
        var favorities: ArrayList<String?> = ArrayList()
        Firebase.firestore
            .collection("users")
            .document(idUser!!)
            .collection("favorites")
            .get()
            .addOnSuccessListener {
                for (doc in it){
                    var favorite = doc.id
                    favorities.add(favorite)
                    Log.e("numbers", favorities.toString())
                }
                places.addAll( favorities.map {id -> Places.obtener(id)!!  }.toCollection(ArrayList()))
            }
        return places
    }

    fun agregarFavoritos(id:String?,idPlace:String?){
        val fecha = HashMap<String, Date>()
        fecha.put("fecha", Date())
        Firebase.firestore
            .collection("users")
            .document(id!!)
            .collection("favorites")
            .document(idPlace!!)
            .set(fecha)

    }

    fun eliminarFavoritos(id:String?,idPlace:String){
        val fecha = HashMap<String, Date>()
        fecha.put("fecha", Date())
        Firebase.firestore
            .collection("users")
            .document(id!!)
            .collection("favorites")
            .document(idPlace!!)
            .delete()
    }
}