package com.example.unilocal.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.unilocal.R
import com.example.unilocal.activities.DatallesModeradorActivity
import com.example.unilocal.bd.Usuarios
import com.example.unilocal.models.Comment
import com.example.unilocal.models.Moderator
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
        var codePlace:Int = 0

        init {
            itemView.setOnClickListener (this)
        }

        fun bind(comment: Comment){
            val simpleDateFormat: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
            nickname.text = Usuarios.getUser(comment.idUser)!!.nickname
            date.text = simpleDateFormat.format(comment.creationDate.time)
            text.text = comment.text
            codePlace = comment.idPlace
        }

        override fun onClick(p0: View?) {

        }
    }

}