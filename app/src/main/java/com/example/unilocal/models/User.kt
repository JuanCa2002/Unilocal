package com.example.unilocal.models

class User (var id: Int,
            var name: String,
            var nickname: String ,
            var email: String,
            var password: String ) {


    override fun toString(): String {
        return "User(id=$id, name='$name', nickname='$nickname', email='$email', password='$password')"
    }
}