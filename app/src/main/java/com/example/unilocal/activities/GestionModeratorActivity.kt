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
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.unilocal.R
import com.example.unilocal.adapter.ModeratorAdapter
import com.example.unilocal.bd.Usuarios
import com.example.unilocal.databinding.ActivityGestionModeratorBinding
import com.example.unilocal.models.Rol
import com.example.unilocal.models.StatusUser
import com.example.unilocal.models.User
import com.example.unilocal.sqlite.UniLocalDbHelper
import com.example.unilocal.utils.ConectionStatus
import com.example.unilocal.utils.Idioma
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView

class GestionModeratorActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var sharedPreferences: SharedPreferences
    lateinit var binding: ActivityGestionModeratorBinding
    lateinit var moderators : ArrayList<User>
    lateinit var bd: UniLocalDbHelper
    lateinit var adapter: ModeratorAdapter
    var user:FirebaseUser? = null
    var estadoConexion: Boolean = false
    var codeAdministrador: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGestionModeratorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        moderators = ArrayList()
        bd = UniLocalDbHelper(this)
        user = FirebaseAuth.getInstance().currentUser
        if(user != null){
            codeAdministrador= user!!.uid
        }
        comprobarConexionInternet()
        mostrarDatos(false)
        adapter = ModeratorAdapter(moderators, this)
        binding.listModerators.adapter = adapter
        binding.listModerators.layoutManager  = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        binding.navigationView.setNavigationItemSelectedListener(this)
        binding.btnCreateModerator.setOnClickListener { irCrearModerator() }

        var menu = this.findViewById<Button>(R.id.btn_menu)
        menu.setOnClickListener { abrirMenu()}
    }

    fun abrirMenu(){
        binding.drawerLayout.openDrawer(GravityCompat.START)
    }

    fun mostrarDatos(estado:Boolean){
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
            val user = bd.getUserById(codeAdministrador!!)
            val header = binding.navigationView.getHeaderView(0)
            header.findViewById<TextView>(R.id.name_user_session).text = user!!.nombre
            header.findViewById<TextView>(R.id.email_user_session).text = user!!.correo
        }
    }

    fun comprobarConexion(estado:Boolean){
        estadoConexion = estado
        mostrarDatos(estado)
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

    fun cambiarIdioma(){
        Idioma.selecionarIdioma(this)
        val intent = intent
        if (intent != null) {
            intent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            finish()
            startActivity(intent)
        }
    }


    fun cerrarSesion(){
        FirebaseAuth.getInstance().signOut()
        bd.deleteUser(codeAdministrador!!)
        bd.listPlaces().forEach{
            bd.deletePlace(it.key)
        }
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
            R.id.menu_cambiar_idioma -> cambiarIdioma()
            R.id.menu_cerrar_sesion -> cerrarSesion()
            R.id.navCategorias -> abrirCategorias()
        }
        item.isChecked = true
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    fun abrirPerfil(){
        val intent = Intent(this, DetallesUsuarioActivity::class.java)
        intent.putExtra("code",codeAdministrador)
        startActivity(intent)
    }

    fun abrirCategorias(){
        val intent = Intent(this, CategoriesActivity::class.java)
        intent.putExtra("code",codeAdministrador)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        moderators.clear()
        if(estadoConexion){
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
                   adapter.notifyDataSetChanged()
                }
        }else{
            Snackbar.make(binding.root, getString(R.string.no_cargar_apartado), Snackbar.LENGTH_LONG).show()
        }
    }
}