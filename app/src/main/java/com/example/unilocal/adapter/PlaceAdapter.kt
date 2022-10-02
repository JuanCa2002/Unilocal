package com.example.unilocal.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.unilocal.R
import com.example.unilocal.activities.DetalleLugarActivity
import com.example.unilocal.activities.DetalleLugarUsuarioActivity
import com.example.unilocal.models.Place

class PlaceAdapter(var places:ArrayList<Place>,var origen:String):RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_layout,parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(places[position])
    }

    override fun getItemCount() = places.size

    inner class ViewHolder(var itemView: View):RecyclerView.ViewHolder(itemView),View.OnClickListener{

        val name:TextView = itemView.findViewById(R.id.name_place)
        val address:TextView = itemView.findViewById(R.id.address_place)
        val status:TextView = itemView.findViewById(R.id.status_place)
        val schedule:TextView = itemView.findViewById(R.id.schedule_place)
        val image:ImageView = itemView.findViewById(R.id.image_place)
        var codePlace:Int = 0

        init {
            itemView.setOnClickListener (this)
        }

        fun bind(place:Place){
            name.text = place.name
            address.text = place.address
            status.text = "Abierto"
            schedule.text = "Cierra a las 2"
            codePlace = place.id

        }

        override fun onClick(p0: View?) {
            if(origen == "Busqueda"){
                val intent = Intent(name.context, DetalleLugarActivity::class.java)
                intent.putExtra("code", codePlace)
                name.context.startActivity(intent)
            }else{
                val intent = Intent(name.context, DetalleLugarUsuarioActivity::class.java)
                intent.putExtra("pos",adapterPosition)
                intent.putExtra("code", codePlace)
                name.context.startActivity(intent)
            }

        }
    }
}