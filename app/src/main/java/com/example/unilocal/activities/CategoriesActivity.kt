package com.example.unilocal.activities

import android.R
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
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.unilocal.adapter.ModeratorAdapter
import com.example.unilocal.adapter.PlaceAdapter
import com.example.unilocal.bd.*
import com.example.unilocal.databinding.ActivityCategoriesBinding
import com.example.unilocal.models.Category
import com.example.unilocal.models.Place
import com.example.unilocal.models.StatusPlace
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

class CategoriesActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener  {
    lateinit var categories: ArrayList<Category>
    lateinit var binding: ActivityCategoriesBinding
    lateinit var bd: UniLocalDbHelper
    var codeUser: String ?= ""
    var estadoConexion: Boolean = false
    var places:ArrayList<Place> = ArrayList()
    var userLogin: FirebaseUser? = null
    var categoryPosition: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bd = UniLocalDbHelper(this)
        codeUser= intent.extras!!.getString("code")
        categories = ArrayList()
        var menu = this.findViewById<Button>(com.example.unilocal.R.id.btn_menu)
        menu.setOnClickListener { abrirMenu()}
        userLogin = FirebaseAuth.getInstance().currentUser
        if(userLogin != null){
            loadCategories()
            binding.filter.setOnClickListener { loadPlacesByCategory() }
        }
        comprobarConexionInternet()
        mostrarDatos(false)

        binding.navigationView.setNavigationItemSelectedListener(this)
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
            if(userLogin != null){
                Firebase.firestore
                    .collection("users")
                    .document(userLogin!!.uid)
                    .get()
                    .addOnSuccessListener { u ->
                        val header = binding.navigationView.getHeaderView(0)
                        header.findViewById<TextView>(com.example.unilocal.R.id.name_user_session).text = u.toObject(User::class.java)?.nombre
                        header.findViewById<TextView>(com.example.unilocal.R.id.email_user_session).text = userLogin!!.email
                    }
            }
        }else{
            val user = bd.getUserById(codeUser!!)
            val header = binding.navigationView.getHeaderView(0)
            header.findViewById<TextView>(com.example.unilocal.R.id.name_user_session).text = user!!.nombre
            header.findViewById<TextView>(com.example.unilocal.R.id.email_user_session).text = user!!.correo
        }
    }

    fun comprobarConexion(estado:Boolean){
        estadoConexion = estado
        mostrarDatos(estado)
    }

    fun loadCategories(){
        categories.clear()
        Firebase.firestore
            .collection("categoriesF")
            .get()
            .addOnSuccessListener {
                for(doc in it ){
                    val category = doc.toObject(Category::class.java)
                    category.key = doc.id
                    categories.add(category)
                }
                var adapter= ArrayAdapter(this, R.layout.simple_spinner_item,categories)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.categories.adapter= adapter
                binding.categories.setSelection(0)
                binding.categories.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        categoryPosition = p2
                    }
                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }
                }
            }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            com.example.unilocal.R.id.menu_cambiar_idioma -> cambiarIdioma()
            com.example.unilocal.R.id.navPerfil -> abrirPerfil()
            com.example.unilocal.R.id.menu_cerrar_sesion -> cerrarSesion()
            com.example.unilocal.R.id.navCategorias -> abrirCategorias()
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

    fun cambiarIdioma(){
        Idioma.selecionarIdioma(this)
        val intent = intent
        if (intent != null) {
            intent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            finish()
            startActivity(intent)
        }
    }

    fun abrirMenu(){
        binding.drawerLayout.openDrawer(GravityCompat.START)
    }

    fun cerrarSesion(){
        FirebaseAuth.getInstance().signOut()
        bd.deleteUser(codeUser!!)
        bd.listPlaces().forEach{
            bd.deletePlace(it.key)
        }
        val intent = Intent(this, LoginActivity::class.java)
        startActivity( intent )
        finish()

    }

    fun loadPlacesByCategory(){
        if(estadoConexion) {
            places.clear()
            Firebase.firestore
                .collection("placesF")
                .whereEqualTo("idCategory", categories[categoryPosition].key)
                .whereEqualTo("status", StatusPlace.ACEPTADO)
                .get()
                .addOnSuccessListener {
                    for (doc in it) {
                        val place = doc.toObject(Place::class.java)
                        place.key = doc.id
                        places.add(place)
                    }
                    val adapter = PlaceAdapter(places, "Busqueda")
                    binding.listPlacesCategory.adapter = adapter
                    binding.listPlacesCategory.layoutManager =
                        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                }
        }else{
            Snackbar.make(binding.root, getString(com.example.unilocal.R.string.no_cargar_apartado), Snackbar.LENGTH_LONG).show()
        }
    }
}