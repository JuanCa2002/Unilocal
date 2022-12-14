package com.example.unilocal.activities

import android.app.Dialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.unilocal.R
import com.example.unilocal.bd.Usuarios
import com.example.unilocal.databinding.ActivityMainBinding

import com.example.unilocal.fragments.FavoritesFragment
import com.example.unilocal.fragments.InicioFragment
import com.example.unilocal.fragments.MyPlacesFragment
import com.example.unilocal.models.User
import com.example.unilocal.sqlite.UniLocalDbHelper
import com.example.unilocal.utils.ConectionStatus
import com.example.unilocal.utils.Idioma
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView

class MainActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {
    lateinit var bd: UniLocalDbHelper
    lateinit var binding: ActivityMainBinding
    private var MENU_INICIO = "Inicio"
    private var MENU_MIS_LUGARES = "Mis lugares"
    private var MENU_FAVORITOS = "favoritos"
    var codeUser: String = ""
    var estadoConexion: Boolean = false
    var userLogin:FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        bd = UniLocalDbHelper(this)
        userLogin = FirebaseAuth.getInstance().currentUser
        if(userLogin!=null){
            codeUser = userLogin!!.uid
        }

        comprobarConexionInternet()
        mostrarDatos(false)
        changeFragments(2,MENU_INICIO)

        binding.barraInferior.setOnItemSelectedListener {
            when(it.itemId){
                R.id.menu_favorites -> changeFragments(1,MENU_FAVORITOS)
                R.id.menu_home -> changeFragments(2,MENU_INICIO)
                R.id.menu_my_places -> changeFragments(3,MENU_MIS_LUGARES)
            }
            true
        }
        binding.navigationView.setNavigationItemSelectedListener (this)
        setContentView(binding.root)
    }

    fun mostrarDatos(estado:Boolean){

        if(estado){

            if(userLogin!=null){

                Firebase.firestore
                    .collection("users")
                    .document(userLogin!!.uid)
                    .get()
                    .addOnSuccessListener { u ->
                        val header = binding.navigationView.getHeaderView(0)
                        header.findViewById<TextView>(R.id.name_user_session).text = u.toObject(User::class.java)?.nombre
                        header.findViewById<TextView>(R.id.email_user_session).text = userLogin!!.email
                        val imagePerfil = header.findViewById<CircleImageView>(R.id.image_perfil)
                        Glide.with(  baseContext )
                            .load(u.toObject(User::class.java)!!.imageUri)
                            .into(imagePerfil)
                    }
            }
        }else{
            val userLogin = bd.getUserById(codeUser!!)
            val header = binding.navigationView.getHeaderView(0)
            if(userLogin!=null) {
                header.findViewById<TextView>(R.id.name_user_session).text = userLogin!!.nombre
                header.findViewById<TextView>(R.id.email_user_session).text = userLogin!!.correo
            }
        }
    }

    fun irCrearLugar(view:View){
        val intent = Intent(this, CrearLugarActivity::class.java)
        startActivity(intent)
    }

    fun cerrarSesion(){
        FirebaseAuth.getInstance().signOut()
        bd.deleteUser(codeUser)
        bd.listPlaces().forEach{
            bd.deletePlace(it.key)
        }
        val intent = Intent(this, LoginActivity::class.java)
        startActivity( intent )
        finish()

    }

    fun changeFragments(valor:Int, nombre:String){
        var fragment:Fragment
        if(valor ==  1){
            fragment = FavoritesFragment()
        }else if(valor == 2){
            fragment = InicioFragment.newInstance("Main")
        }else{
            fragment = MyPlacesFragment()
        }
        supportFragmentManager.beginTransaction().replace(binding.contenidoPrincipal.id,fragment)
            .addToBackStack(nombre)
            .commit()
    }

    fun abrirMenu(){
        binding.drawerLayout.openDrawer(GravityCompat.START)
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
        intent.putExtra("code",codeUser)
        startActivity(intent)
    }

    fun abrirCategorias(){
        val intent = Intent(this, CategoriesActivity::class.java)
        intent.putExtra("code",codeUser)
        startActivity(intent)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val count = supportFragmentManager.backStackEntryCount
        if(count > 0){
            val nombre = supportFragmentManager.getBackStackEntryAt(count-1).name
            when(nombre){
                MENU_FAVORITOS -> binding.barraInferior.menu.getItem(0).isChecked = true
                MENU_INICIO -> binding.barraInferior.menu.getItem(1).isChecked = true
                MENU_MIS_LUGARES -> binding.barraInferior.menu.getItem(2).isChecked = true
            }
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

    override fun attachBaseContext(newBase: Context?) {
        val localeUpdatedContext: ContextWrapper? = Idioma.cambiarIdioma(newBase!!)
        super.attachBaseContext(localeUpdatedContext)
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
                ConectionStatus(::comprobarConexion))
        }
    }

    fun comprobarConexion(estado:Boolean){
        estadoConexion = estado
        mostrarDatos(estado)
    }

    override fun onPause() {
        super.onPause()
        val userLogin = FirebaseAuth.getInstance().currentUser
        if (userLogin != null) {
            Firebase.firestore
                .collection("users")
                .document(userLogin.uid)
                .get()
                .addOnSuccessListener { u ->
                    val header = binding.navigationView.getHeaderView(0)
                    header.findViewById<TextView>(R.id.name_user_session).text =
                        u.toObject(User::class.java)?.nombre
                    header.findViewById<TextView>(R.id.email_user_session).text = userLogin.email

                }
        }
    }
}