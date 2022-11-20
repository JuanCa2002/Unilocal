package com.example.unilocal.activities

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.unilocal.R
import com.example.unilocal.adapter.ModeratorAdapter
import com.example.unilocal.bd.Cities
import com.example.unilocal.bd.Usuarios
import com.example.unilocal.databinding.ActivityDatallesModeradorBinding
import com.example.unilocal.models.City
import com.example.unilocal.models.StatusUser
import com.example.unilocal.models.User
import com.example.unilocal.utils.ConectionStatus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthActionCodeException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class DatallesModeradorActivity : AppCompatActivity() {
    lateinit var binding: ActivityDatallesModeradorBinding
    lateinit var moderatorAdapter: ModeratorAdapter
    lateinit var moderators: ArrayList<User>
    var codeModerator:String? = ""
    var pos: Int = -1
    var moderator: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDatallesModeradorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        codeModerator = intent.extras!!.getString("code")
        if(codeModerator!= ""){
            Firebase.firestore
                .collection("users")
                .document(codeModerator!!)
                .get()
                .addOnSuccessListener {
                    moderator = it.toObject(User::class.java)
                    moderator!!.key = it.id
                    Firebase.firestore
                        .collection("citiesF")
                        .document(moderator!!.idCity)
                        .get()
                        .addOnSuccessListener {m->
                            var city = m.toObject(City::class.java)
                            city!!.key = m.id
                            binding.cityPlace.text = city!!.name
                        }
                    val image = binding.imageModerator
                    Glide.with( baseContext )
                        .load(it.toObject(User::class.java)!!.imageUri)
                        .into(image)
                    binding.moderatorName.text = moderator!!.nombre
                    binding.nickname.text = moderator!!.nickname
                    binding.modEmail.text = moderator!!.correo
                    binding.mainNameMod.text= moderator!!.nombre
                }
        }
        val user = FirebaseAuth.getInstance().currentUser
        if(user != null){
            binding.btnEliminar.setOnClickListener{deleteModerator()}
        }
        moderators = ArrayList()
        moderatorAdapter = ModeratorAdapter(moderators,this)

    }

    fun deleteModerator(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.txt_eliminar_mod)
        builder.setMessage(R.string.txt_eliminar_mod_pregunta)
        builder.setPositiveButton(R.string.txt_si){dialogInterface, which ->
           Firebase.firestore
               .collection("users")
               .document(codeModerator!!)
               .get()
               .addOnSuccessListener {
                   val user = it.toObject(User::class.java)
                   user!!.key = it.id
                   user.status = StatusUser.INHABILITADO
                   Firebase.firestore
                       .collection("users")
                       .document(it.id)
                       .set(user)
                       .addOnSuccessListener {
                           moderatorAdapter.notifyItemRemoved(pos)
                           startActivity(Intent(this, GestionModeratorActivity::class.java))
                       }
               }
        }
        builder.setNeutralButton(R.string.txt_cancel){dialogInterface , which -> }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
}