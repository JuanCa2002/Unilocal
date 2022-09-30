package com.example.unilocal.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.unilocal.R
import com.example.unilocal.activities.DetalleLugarActivity
import com.example.unilocal.activities.GestionModeratorActivity
import com.example.unilocal.models.Moderator
import com.example.unilocal.models.Place

class ModeratorAdapter(var moderators:ArrayList<Moderator>): RecyclerView.Adapter<ModeratorAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_moderator_layout,parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ModeratorAdapter.ViewHolder, position: Int) {
        holder.bind(moderators[position])
    }

    override fun getItemCount() = moderators.size

    inner class ViewHolder(var itemView: View):RecyclerView.ViewHolder(itemView),View.OnClickListener{

        val name: TextView = itemView.findViewById(R.id.name_moderator)
        val nickname: TextView = itemView.findViewById(R.id.nickname_moderator)
        val email: TextView = itemView.findViewById(R.id.email_moderator)
        var codeModerator:Int = 0

        init {
            itemView.setOnClickListener (this)
        }

        fun bind(moderator: Moderator){
            name.text = moderator.nombre
            nickname.text = moderator.nickname
            email.text = moderator.correo
            codeModerator = moderator.id

        }

        override fun onClick(p0: View?) {
            val intent = Intent(name.context, GestionModeratorActivity::class.java)
            name.context.startActivity(intent)
        }
    }
}