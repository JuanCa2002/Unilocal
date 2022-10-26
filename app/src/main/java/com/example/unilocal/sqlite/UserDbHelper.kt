package com.example.unilocal.sqlite

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.unilocal.models.User
import java.lang.Exception

class UserDbHelper(context: Context):SQLiteOpenHelper(context, "users.db",null, 1) {
    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL("create table ${UserContract.TABLE_NAME}(" +
                "  ${UserContract.ID} INTEGER primary key AUTOINCREMENT," +
                " ${UserContract.NOMBRE} varchar(100) not null," +
                " ${UserContract.NICKNAME} varchar(100) not null unique, " +
                " ${UserContract.CORREO} varchar(150) not null unique," +
                " ${UserContract.PASSWORD} varchar(50) not null," +
                " ${UserContract.ID_CITY} int"+
                "  )")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
       p0?.execSQL("drop table if exists ${UserContract.TABLE_NAME}")
       onCreate(p0)
    }

    fun createUser(user: User){
        try{
            writableDatabase.insert(
                UserContract.TABLE_NAME,
                null,
                user.toContentValues())
        }catch (e:Exception){
            throw Exception("Se presento el siguiente error: ${e.message}")
        }
    }

    fun updateUser(user:User){
        writableDatabase.update(
            UserContract.TABLE_NAME,
            user.toContentValues(),
            "${UserContract.ID} = ?",
            arrayOf(user.id.toString())
        )
    }

    fun deleteUser(id:Int){
        writableDatabase.delete(
            UserContract.TABLE_NAME,
            "${UserContract.ID} = ?",
            arrayOf(id.toString())
        )
    }

    fun listUsers():ArrayList<User>{
        val users: ArrayList<User> = ArrayList()
        val c:Cursor = readableDatabase.query(
            UserContract.TABLE_NAME,
            arrayOf(UserContract.ID,UserContract.NOMBRE, UserContract.NICKNAME,
            UserContract.CORREO, UserContract.PASSWORD, UserContract.ID_CITY),
            null, null, null, null, null
        )
        if(c.moveToFirst()){
            do{
              users.add(User(c.getInt(0),c.getString(1), c.getString(2),c.getString(3),c.getString(4), c.getInt(5)))
            } while (c.moveToNext())
        }
        return users
    }

    fun getUserById(id:Int):User?{
        var user:User? = null
        val c:Cursor = readableDatabase.query(
            UserContract.TABLE_NAME,
            arrayOf(UserContract.ID,UserContract.NOMBRE, UserContract.NICKNAME,
                UserContract.CORREO, UserContract.PASSWORD, UserContract.ID_CITY),
            "${UserContract.ID} = ?", arrayOf(id.toString()), null, null, null
        )
        if(c.moveToFirst()){
            user = User(c.getInt(0),c.getString(1), c.getString(2),c.getString(3),c.getString(4), c.getInt(5))
        }
        return user
    }

    fun login(correo:String, password:String):User?{
        var user:User? = null
        val c:Cursor = readableDatabase.query(
            UserContract.TABLE_NAME,
            arrayOf(UserContract.ID,UserContract.NOMBRE, UserContract.NICKNAME,
                UserContract.CORREO, UserContract.PASSWORD, UserContract.ID_CITY),
            "${UserContract.CORREO} = ? and ${UserContract.PASSWORD} = ?", arrayOf(correo, password), null, null, null
        )
        if(c.moveToFirst()){
            user = User(c.getInt(0),c.getString(1), c.getString(2),c.getString(3),c.getString(4), c.getInt(5))
        }
        return user
    }
}