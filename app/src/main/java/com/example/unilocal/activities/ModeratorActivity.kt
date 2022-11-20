package com.example.unilocal.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.unilocal.R
import com.example.unilocal.bd.Usuarios
import com.example.unilocal.databinding.ActivityModeratorBinding
import com.example.unilocal.fragments.*
import com.example.unilocal.models.User
import com.example.unilocal.sqlite.UniLocalDbHelper
import com.example.unilocal.utils.ConectionStatus
import com.example.unilocal.utils.Idioma
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView

class ModeratorActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener  {
    private lateinit var sharedPreferences: SharedPreferences
    lateinit var binding: ActivityModeratorBinding
    lateinit var bd: UniLocalDbHelper
    private var MENU_PENDIENTES = "Pendiente"
    private var MENU_REGISTRO = "Registro"
    var codeModerator: String = ""
    var user: FirebaseUser? = null
    var estadoConexion: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModeratorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        user = FirebaseAuth.getInstance().currentUser
        bd = UniLocalDbHelper(this)

        if(user != null){
            codeModerator = user!!.uid
        }

        comprobarConexionInternet()
        mostrarDatos(false)

        changeFragments(1, MENU_PENDIENTES)
        binding.barraInferiorModerator.setOnItemSelectedListener {
            when(it.itemId){
                R.id.menu_pendiente -> changeFragments(1,MENU_PENDIENTES)
                R.id.menu_registro -> changeFragments(2,MENU_REGISTRO)
            }
            true
        }
        binding.navigationView.setNavigationItemSelectedListener (this)
        var menu = this.findViewById<Button>(R.id.btn_menu)
        menu.setOnClickListener { abrirMenu()}
    }

    fun changeFragments(valor:Int, nombre:String){
        var fragment: Fragment
        if(valor ==  1){
            fragment = PendientesPlaceFragment.newInstance(codeModerator)
        }else{
            fragment = RegistreFragment.newInstance(codeModerator)
        }
        supportFragmentManager.beginTransaction().replace(binding.contenidoPrincipal.id,fragment)
            .addToBackStack(nombre)
            .commit()
    }

    fun abrirMenu(){
        binding.drawerLayout.openDrawer(GravityCompat.START)
    }

    fun cerrarSesion(){
        FirebaseAuth.getInstance().signOut()
        bd.deleteUser(codeModerator)
        bd.listPlaces().forEach{
            bd.deletePlace(it.key)
        }
        val intent = Intent(this, LoginActivity::class.java)
        startActivity( intent )
        finish()

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.navPerfil -> abrirPerfil()
            R.id.menu_cambiar_idioma -> cambiarIdioma()
            R.id.menu_cerrar_sesion -> cerrarSesion()
            R.id.navCategorias -> abrirCategorias()
        }
        item.isChecked = true
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    fun comprobarConexionInternet() {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as
                ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager?.let {
                it.registerDefaultNetworkCallback(ConectionStatus(::comprobarConexion))
            }
        }else{
            val request =
                NetworkRequest.Builder().addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build()
            connectivityManager.registerNetworkCallback(request,
                ConectionStatus(::comprobarConexion)
            )
        }
    }

    fun mostrarDatos(estado: Boolean){
        if(estado){
            Firebase.firestore
                .collection("users")
                .document(user!!.uid)
                .get()
                .addOnSuccessListener {
                    val header = binding.navigationView.getHeaderView(0)
                    val image = header.findViewById<CircleImageView>(R.id.image_perfil)
                    Glide.with( baseContext )
                        .load(it.toObject(User::class.java)!!.imageUri)
                        .into(image)
                    header.findViewById<TextView>(R.id.name_user_session).text = it.toObject(User::class.java)!!.nombre
                    header.findViewById<TextView>(R.id.email_user_session).text = user!!.email
                }
        }else{
            val user = bd.getUserById(codeModerator)
            val header = binding.navigationView.getHeaderView(0)
            header.findViewById<TextView>(R.id.name_user_session).text = user!!.nombre
            header.findViewById<TextView>(R.id.email_user_session).text = user!!.correo
        }
    }

    fun comprobarConexion(estado:Boolean){
        estadoConexion = estado
        mostrarDatos(estado)
    }

    fun cambiarIdioma(){
        Idioma.selecionarIdioma(this)
        val intent = intent
        if (intent != null) {
            intent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            finish()
            startActivity(intent)
        }
    }

    fun abrirPerfil(){
        val intent = Intent(this, DetallesUsuarioActivity::class.java)
        intent.putExtra("code",codeModerator)
        startActivity(intent)
    }

    fun abrirCategorias(){
        val intent = Intent(this, CategoriesActivity::class.java)
        intent.putExtra("code",codeModerator)
        startActivity(intent)
    }
}