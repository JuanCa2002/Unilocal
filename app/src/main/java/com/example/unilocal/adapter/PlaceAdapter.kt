package com.example.unilocal.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.unilocal.R
import com.example.unilocal.activities.DetalleLugarActivity
import com.example.unilocal.activities.DetalleLugarUsuarioActivity
import com.example.unilocal.bd.Categories
import com.example.unilocal.bd.Comments
import com.example.unilocal.models.Category
import com.example.unilocal.models.Comment
import com.example.unilocal.models.Place
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text

class PlaceAdapter(var places:ArrayList<Place>,var origen:String, var context: Context):RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {
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
        val category:TextView = itemView.findViewById(R.id.icon_place)
        val image:ImageView = itemView.findViewById(R.id.image_place)
        val stars:LinearLayout = itemView.findViewById(R.id.list_stars)
        var codePlace:String? = ""

        init {
            itemView.setOnClickListener (this)
        }

        fun bind(place:Place){

            val estaAbierto = place.estaAbierto()

            if(estaAbierto){
                status.setTextColor( ContextCompat.getColor(itemView.context, R.color.green ) )
                schedule.text = "Cierra a las ${place.obtenerHoraCierre()}"
            }else{
                status.setTextColor( ContextCompat.getColor(itemView.context, R.color.red ) )
                schedule.text = "Abre el ${place.obtenerHoraApertura()}"
            }
            status.text = if(estaAbierto){status.context.getString(R.string.abierto) }else{ status.context.getString(R.string.cerrado) }
            Firebase.firestore
                .collection("categoriesF")
                .whereEqualTo("key",place.idCategory)
                .get()
                .addOnSuccessListener {
                    for( doc in it){
                        val categoryF = doc.toObject(Category::class.java)
                        categoryF.key = doc.id
                        category.text = categoryF.icon
                        Log.e("icon", categoryF.icon)
                    }
                }
            Glide.with(  context )
                .load(place.images[0])
                .into(image)
            name.text = place.name
            address.text = place.address
            codePlace = place.key
            val comments: ArrayList<Comment> = ArrayList()
            Firebase.firestore
                .collection("placesF")
                .document(place.key)
                .collection("commentsF")
                .get()
                .addOnSuccessListener { c ->
                    for (doc in c) {
                        comments.add(doc.toObject(Comment::class.java))
                    }
                    val qualification = place.obtenerCalificacionPromedio(comments)
                    for (i in 0..qualification){
                        (stars[i] as TextView).setTextColor(ContextCompat.getColor(stars.context,R.color.yellow))
                    }
                }

        }


        override fun onClick(p0: View?) {
            if(origen == "Busqueda"){
                val intent = Intent(name.context, DetalleLugarActivity::class.java)
                intent.putExtra("code", codePlace)
                intent.putExtra("pos",adapterPosition)
                name.context.startActivity(intent)
            }else{
                val intent = Intent(name.context, DetalleLugarUsuarioActivity::class.java)
                intent.putExtra("code", codePlace)
                name.context.startActivity(intent)
            }

        }
    }
}