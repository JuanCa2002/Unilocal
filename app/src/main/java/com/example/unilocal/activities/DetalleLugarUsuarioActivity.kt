package com.example.unilocal.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.unilocal.R
import com.example.unilocal.adapter.ModeratorAdapter
import com.example.unilocal.adapter.PlaceAdapter
import com.example.unilocal.bd.Moderators
import com.example.unilocal.bd.Places
import com.example.unilocal.databinding.ActivityDetalleLugarUsuarioBinding
import com.example.unilocal.fragments.InicioFragment
import com.example.unilocal.models.Moderator
import com.example.unilocal.models.Place

class DetalleLugarUsuarioActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetalleLugarUsuarioBinding
    var codePlace:Int = -1
    var codeUser:Int = -1
    var pos: Int = -1
    lateinit var placeAdapter: PlaceAdapter
    var place: Place? = null
    lateinit var placesByUser: ArrayList<Place>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleLugarUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sp = getSharedPreferences("sesion", Context.MODE_PRIVATE)
        codeUser = sp.getInt("id",0)
        pos = intent.extras!!.getInt("position")
        codePlace= intent.extras!!.getInt("code")
        placesByUser = Places.listByUser(codeUser)
        placeAdapter = PlaceAdapter(placesByUser,"usuario")
        place = Places.obtener(codePlace)

        if(place != null){

            binding.nombreLayout.hint = place!!.name
            binding.telefonoLayout.hint = "311"
            binding.campoDireccionLayout.hint= place!!.address
            binding.descripcionLayout.hint = place!!.description
        }

        binding.btnEliminarLugarUsuario.setOnClickListener{deletePlace()}

    }

    fun deletePlace(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Eliminar Lugar")
        builder.setMessage("¿Esta seguro de eliminar este lugar?")

        builder.setPositiveButton("Si") { dialogInterface, which ->
            Places.deletePlace(codePlace)
            placesByUser.remove(place)
            placeAdapter.notifyItemRemoved(pos)
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("code",codeUser)
            startActivity(intent)
        }

        builder.setNeutralButton("Cancel"){dialogInterface , which -> }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    fun updatePlace(){
        
    }
}