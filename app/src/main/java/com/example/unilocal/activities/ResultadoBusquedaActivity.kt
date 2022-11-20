package com.example.unilocal.activities

import android.app.Dialog
import android.content.Context
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
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.unilocal.R
import com.example.unilocal.adapter.PlaceAdapter
import com.example.unilocal.bd.Places
import com.example.unilocal.bd.Usuarios
import com.example.unilocal.databinding.ActivityResultadoBusquedaBinding
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
import de.hdodenhof.circleimageview.CircleImageView

class ResultadoBusquedaActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener{
    private lateinit var sharedPreferences: SharedPreferences
    lateinit var placesCoincidences:ArrayList<Place>
    lateinit var binding: ActivityResultadoBusquedaBinding
    lateinit var bd: UniLocalDbHelper
    lateinit var adapter: PlaceAdapter
    lateinit var dialog: Dialog
    var places:ArrayList<Place> = ArrayList()
    var textSearch:String = ""
    var code:String? = ""
    var estadoConexion: Boolean = false
    var userLogin: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultadoBusquedaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        textSearch = intent.extras!!.getString("txt_search","")
        placesCoincidences = ArrayList()
        adapter = PlaceAdapter(placesCoincidences,"Busqueda",this)
        userLogin = FirebaseAuth.getInstance().currentUser
        bd = UniLocalDbHelper(this)
        if(userLogin!=null){
           code = userLogin!!.uid
        }
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setView(R.layout.dialogo_progreso)
        dialog = builder.create()
        comprobarConexionInternet()
        mostrarDatos(false)
        binding.navigationView.setNavigationItemSelectedListener (this)
        var menu = this.findViewById<Button>(R.id.btn_menu)
        menu.setOnClickListener { abrirMenu()}
    }

    fun mostrarDatos(estado: Boolean){
        if(estado){
            Firebase.firestore
                .collection("users")
                .document(userLogin!!.uid)
                .get()
                .addOnSuccessListener { u ->
                    val header = binding.navigationView.getHeaderView(0)
                    val image = header.findViewById<CircleImageView>(R.id.image_perfil)
                    Glide.with( baseContext )
                        .load(u.toObject(User::class.java)!!.imageUri)
                        .into(image)
                    header.findViewById<TextView>(R.id.name_user_session).text = u.toObject(User::class.java)!!.nombre
                    header.findViewById<TextView>(R.id.email_user_session).text = userLogin!!.email
                }
        }else{
            val user = bd.getUserById(code!!)
            val header = binding.navigationView.getHeaderView(0)
            header.findViewById<TextView>(R.id.name_user_session).text = user!!.nombre
            header.findViewById<TextView>(R.id.email_user_session).text = user!!.correo
        }
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

    fun comprobarConexion(estado:Boolean){
        estadoConexion = estado
        mostrarDatos(estado)
    }

    fun cerrarSesion(){
        FirebaseAuth.getInstance().signOut()
        bd.deleteUser(code!!)
        bd.listPlaces().forEach{
            bd.deletePlace(it.key)
        }
        val intent = Intent(this, LoginActivity::class.java)
        startActivity( intent )
        finish()

    }

    fun abrirMenu(){
        binding.drawerLayout.openDrawer(GravityCompat.START)
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

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_cambiar_idioma ->cambiarIdioma()
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
        intent.putExtra("code",code)
        startActivity(intent)
    }

    fun abrirCategorias(){
        val intent = Intent(this, CategoriesActivity::class.java)
        intent.putExtra("code",code)
        startActivity(intent)
    }

    override fun onResume() {
        setDialog(true)
        super.onResume()
        if(estadoConexion){
            places.clear()
            if(textSearch.isNotEmpty()){
                Firebase.firestore
                    .collection("placesF")
                    .whereEqualTo("status", StatusPlace.ACEPTADO)
                    .get()
                    .addOnSuccessListener {
                        for(doc in it){
                            val place = doc.toObject(Place::class.java)
                            place.key = doc.id
                            places.add(place)
                        }
                        placesCoincidences= Places.buscarNombre(textSearch, placesCoincidences, places)
                        Log.e(ResultadoBusquedaActivity::class.java.simpleName, placesCoincidences.toString())
                        adapter = PlaceAdapter(placesCoincidences,"Busqueda",this)
                        binding.listPlacesSearch.adapter = adapter
                        binding.listPlacesSearch.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
                        setDialog(false)
                    }
            }
            adapter.notifyDataSetChanged()
        }else{
            Snackbar.make(binding.root, getString(R.string.no_cargar_apartado), Snackbar.LENGTH_LONG).show()
            setDialog(false)
        }
    }

    private fun setDialog(show: Boolean) {
        if (show) dialog.show() else dialog.dismiss()
    }

}