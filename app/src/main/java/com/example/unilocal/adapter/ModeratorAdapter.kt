package com.example.unilocal.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.unilocal.R
import com.example.unilocal.activities.DatallesModeradorActivity
import com.example.unilocal.models.User
import de.hdodenhof.circleimageview.CircleImageView

class ModeratorAdapter(var moderators:ArrayList<User>, var context: Context): RecyclerView.Adapter<ModeratorAdapter.ViewHolder>() {

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
        val image: CircleImageView = itemView.findViewById(R.id.image_moderator)
        val nickname: TextView = itemView.findViewById(R.id.nickname_moderator)
        val email: TextView = itemView.findViewById(R.id.email_moderator)
        var codeModerator:String = ""

        init {
            itemView.setOnClickListener (this)
        }

        fun bind(moderator: User){
            Glide.with( context )
                .load(moderator.imageUri)
                .into(image)
            name.text = moderator.nombre
            nickname.text = moderator.nickname
            email.text = moderator.correo
            codeModerator = moderator.key

        }

        override fun onClick(p0: View?) {
            val intent = Intent(name.context, DatallesModeradorActivity::class.java)
            intent.putExtra("position",adapterPosition)
            intent.putExtra("code", codeModerator)
            name.context.startActivity(intent)
        }
    }
}