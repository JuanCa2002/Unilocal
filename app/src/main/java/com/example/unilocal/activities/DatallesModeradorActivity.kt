package com.example.unilocal.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.unilocal.R
import com.example.unilocal.adapter.ModeratorAdapter
import com.example.unilocal.bd.Cities
import com.example.unilocal.bd.Usuarios
import com.example.unilocal.databinding.ActivityDatallesModeradorBinding
import com.example.unilocal.models.User

class DatallesModeradorActivity : AppCompatActivity() {
    lateinit var binding: ActivityDatallesModeradorBinding
    var codeModerator:String? = ""
    var pos: Int = -1
    lateinit var moderatorAdapter: ModeratorAdapter
    var moderator: User? = null
    lateinit var moderators: ArrayList<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDatallesModeradorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        pos = intent.extras!!.getInt("position")
        codeModerator = intent.extras!!.getString("code")
        moderators = Usuarios.listarModeradores()
        moderatorAdapter = ModeratorAdapter(moderators)
        moderator = Usuarios.getUser(codeModerator)

        if(moderator != null){
            //var city = Cities.obtener(moderator!!.idCity)
            //binding.cityPlace.text = city!!.name
            binding.moderatorName.text = moderator!!.nombre
            binding.nickname.text = moderator!!.nickname
            //binding.modEmail.text = moderator!!.correo
            binding.mainNameMod.text= moderator!!.nombre
        }
        binding.btnEliminar.setOnClickListener{deleteModerator()}
    }

    fun deleteModerator(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.txt_eliminar_mod)
        builder.setMessage(R.string.txt_eliminar_mod_pregunta)
        builder.setPositiveButton(R.string.txt_si){dialogInterface, which ->
            Usuarios.deleteUser(codeModerator)
            moderators.remove(moderator)
            moderatorAdapter.notifyItemRemoved(pos)
            startActivity(Intent(this, GestionModeratorActivity::class.java))
        }
        builder.setNeutralButton(R.string.txt_cancel){dialogInterface , which -> }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
}