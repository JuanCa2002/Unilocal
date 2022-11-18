package com.example.unilocal.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.unilocal.R
import com.example.unilocal.bd.Usuarios
import com.example.unilocal.models.Comment
import com.example.unilocal.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat

class CommentAdapter(var comments:ArrayList<Comment>): RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_comment_layout,parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: CommentAdapter.ViewHolder, position: Int) {
        holder.bind(comments[position])
    }

    override fun getItemCount() = comments.size

    inner class ViewHolder(var itemView: View):RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val nickname: TextView = itemView.findViewById(R.id.nickname_user)
        val date: TextView = itemView.findViewById(R.id.date_comment)
        val text: TextView = itemView.findViewById(R.id.text_comment)

        init {
            itemView.setOnClickListener (this)
        }

        fun bind(comment: Comment){
            Firebase.firestore
                .collection("users")
                .document(comment.idUser!!)
                .get()
                .addOnSuccessListener { u ->
                        val simpleDateFormat: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
                        val user = u.toObject(User::class.java)
                        nickname.text = user!!.nickname
                        date.text = simpleDateFormat.format(comment.creationDate.time)
                        text.text = comment.text
                }
        }

        override fun onClick(p0: View?) {

        }
    }

}