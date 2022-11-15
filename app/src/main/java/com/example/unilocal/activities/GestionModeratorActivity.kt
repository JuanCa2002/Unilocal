package com.example.unilocal.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.unilocal.R
import com.example.unilocal.adapter.ModeratorAdapter
import com.example.unilocal.bd.Usuarios
import com.example.unilocal.databinding.ActivityGestionModeratorBinding
import com.example.unilocal.models.Rol
import com.example.unilocal.models.StatusUser
import com.example.unilocal.models.User
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class GestionModeratorActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var sharedPreferences: SharedPreferences
    lateinit var binding: ActivityGestionModeratorBinding
    lateinit var moderators : ArrayList<User>
    var codeAdministrador: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGestionModeratorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        moderators = ArrayList()
        var user = FirebaseAuth.getInstance().currentUser
        if(user != null){
            Firebase.firestore
                .collection("users")
                .document(user.uid)
                .get()
                .addOnSuccessListener {
                    val header = binding.navigationView.getHeaderView(0)
                    header.findViewById<TextView>(R.id.name_user_session).text = it.toObject(User::class.java)!!.nombre
                    header.findViewById<TextView>(R.id.email_user_session).text = user.email
                }
        }
        Firebase.firestore
            .collection("users")
            .whereEqualTo("rol", Rol.MODERATOR)
            .whereEqualTo("status", StatusUser.HABILITADO)
            .get()
            .addOnSuccessListener {
                for(doc in it){
                    val moderator = doc.toObject(User::class.java)
                    moderator.key = doc.id
                    moderators.add(moderator)
                }
                val adapter = ModeratorAdapter(moderators)
                binding.listModerators.adapter = adapter
                binding.listModerators.layoutManager  = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
                binding.navigationView.setNavigationItemSelectedListener(this)
            }
        binding.btnCreateModerator.setOnClickListener { irCrearModerator() }

        var menu = this.findViewById<Button>(R.id.btn_menu)
        menu.setOnClickListener { abrirMenu()}
    }

    fun abrirMenu(){
        binding.drawerLayout.openDrawer(GravityCompat.START)
    }

    fun cerrarSesion(){
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity( intent )
        finish()

    }

    fun irCrearModerator(){
        val intent = Intent(this, CrearModeradorActivity::class.java)
        startActivity(intent)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.navPerfil -> abrirPerfil()
            R.id.menu_cerrar_sesion -> cerrarSesion()
            R.id.navCategorias -> abrirCategorias()
        }
        item.isChecked = true
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    fun abrirPerfil(){
        val intent = Intent(this, DetallesUsuarioActivity::class.java)
        startActivity(intent)
    }

    fun abrirCategorias(){
        val intent = Intent(this, CategoriesActivity::class.java)
        startActivity(intent)
    }
}