package com.example.unilocal.activities

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.unilocal.R
import com.example.unilocal.bd.Cities
import com.example.unilocal.bd.Usuarios
import com.example.unilocal.databinding.ActivityDetallesUsuarioBinding
import com.example.unilocal.models.*
import com.example.unilocal.sqlite.UniLocalDbHelper
import com.example.unilocal.utils.ConectionStatus
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.ArrayList

class DetallesUsuarioActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetallesUsuarioBinding
    lateinit var cities: ArrayList<City>
    lateinit var bd: UniLocalDbHelper
    lateinit var dialog: Dialog
    var user:FirebaseUser? = null
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    var estadoConexion: Boolean = false
    var imageUri:String = ""
    var imageReference:String = ""
    var codigoArchivo: Int = 0
    var datatime: String = ""
    var selectedImageUri: Uri? = null
    var cityPosition: Int = -1
    var tipo: String? = ""
    var code: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetallesUsuarioBinding.inflate(layoutInflater)
        bd = UniLocalDbHelper(this)
        setContentView(binding.root)
        cities = ArrayList()
        user = FirebaseAuth.getInstance().currentUser

        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult() ) {
            onActivityResult(it.resultCode, it)
        }

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setView(R.layout.dialogo_progreso)
        dialog = builder.create()

        comprobarConexionInternet()
        mostrarInformacion(false)
    }

    fun mostrarInformacion(estado:Boolean){
        if(estado){
            if(user!= null){
                Firebase.firestore
                    .collection("users")
                    .document(user!!.uid)
                    .get()
                    .addOnSuccessListener {
                        val userF = it.toObject(User::class.java)
                        binding.nombreUsuario.text = it.toObject(User::class.java)!!.nombre
                        binding.nombreLayout.hint = it.toObject(User::class.java)!!.nombre
                        binding.nicknameLayout.hint = it.toObject(User::class.java)!!.nickname
                        binding.correoLayout.hint = user!!.email
                        val image = binding.imagePerfil
                        Glide.with( baseContext )
                            .load(it.toObject(User::class.java)!!.imageUri)
                            .into(image)
                        binding.btnGuardarCambiosDetallesUsuario.setOnClickListener { updateUser(userF) }
                        binding.btnAsignarFotoPerfil.setOnClickListener { seleccionarFoto() }
                        loadCities(it.toObject(User::class.java))
                    }
            }
        }else{
            code= intent.extras!!.getString("code")
            val user = bd.getUserById(code!!)
            binding.nombreUsuario.text = user!!.nombre
            binding.nombreLayout.hint = user!!.nombre
            binding.nicknameLayout.hint = user!!.nickname
            binding.correoLayout.hint = user!!.correo
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
        mostrarInformacion(estado)
    }

    fun loadCities(person:User?){
        cities.clear()
        Firebase.firestore
            .collection("citiesF")
            .get()
            .addOnSuccessListener {
                for (doc in it) {
                    val city = doc.toObject(City::class.java)
                    city.key = doc.id
                    cities.add(city)
                }
                var city = Cities.obtener(person!!.idCity, cities)
                var position = cities.indexOf(city)
                var adapter= ArrayAdapter(this,android.R.layout.simple_spinner_item,cities)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.cityEdit.adapter= adapter
                binding.cityEdit.setSelection(position)
                binding.cityEdit.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        cityPosition = p2
                    }
                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }
                }
            }
    }

    fun updateUser(person: User?){
        var nombre = binding.campoNombreUsuario.text.toString()
        var nickname = binding.nicknameUsuario.text.toString()
        var correo = binding.correoUsuario.text.toString()
        var idCity = cities[cityPosition].key
        var password = binding.passwordUsuario.text.toString()

        setDialog(true)

        if(nombre.isEmpty()){
            nombre = binding.nombreLayout.hint.toString()
        }

        if(nickname.isEmpty()){
            nickname = binding.nicknameLayout.hint.toString()
        }

        if(correo.isEmpty()){
            correo = binding.correoLayout.hint.toString()
        }
        if(nombre.isNotEmpty() && nickname.isNotEmpty() && correo.isNotEmpty() && idCity!=""){
            if(password.isNotEmpty()){
                eliminarImagenPerfil()
                val newUser = User(nombre,nickname,correo,idCity,person!!.rol)
                var user = FirebaseAuth.getInstance().currentUser
                val credential= EmailAuthProvider.getCredential(user!!.email!!, password)
                user!!.reauthenticate(credential)
                    .addOnSuccessListener {
                        user!!.updateEmail(correo)
                            .addOnSuccessListener {
                                val storageRef = FirebaseStorage.getInstance()
                                    .reference
                                    .child(datatime)
                                if(selectedImageUri!=null){
                                    storageRef.putFile(selectedImageUri!!).addOnSuccessListener {
                                        storageRef.downloadUrl.addOnSuccessListener {
                                            Snackbar.make(binding.root, "Nueva imagen subida", Toast.LENGTH_LONG).show()
                                        }
                                    }.addOnFailureListener {
                                        Snackbar.make(binding.root, "${it.message}", Snackbar.LENGTH_LONG).show()
                                    }
                                }
                                newUser.imageReference = imageReference
                                newUser.imageUri = imageUri
                                verificarEmail(user)
                                Firebase.firestore
                                    .collection("users")
                                    .document(user.uid)
                                    .set(newUser)
                                    .addOnSuccessListener {
                                        Snackbar.make(binding.root, getString(R.string.actualizacion_correcta_det_usu), Toast.LENGTH_LONG).show()
                                        setDialog(false)
                                        Handler(Looper.getMainLooper()).postDelayed({
                                            when(person.rol){
                                                Rol.USER -> startActivity(Intent(this, MainActivity::class.java))
                                                Rol.MODERATOR -> startActivity(Intent(this, ModeratorActivity::class.java))
                                                else -> startActivity(Intent(this, GestionModeratorActivity::class.java))
                                            }
                                        },4000)
                                    }
                            }.addOnFailureListener {
                                Snackbar.make(binding.root, getString(R.string.correo_invalido_det_usu), Toast.LENGTH_LONG).show()
                                setDialog(false)
                            }
                    }.addOnFailureListener {
                        Snackbar.make(binding.root, getString(R.string.contraseña_erronea_det_usu), Toast.LENGTH_LONG).show()
                        setDialog(false)
                    }
            }else{
                Snackbar.make(binding.root, getString(R.string.ingresar_contrasena_det_usu), Toast.LENGTH_LONG).show()
                setDialog(false)
            }
        }else{
            Snackbar.make(binding.root, getString(R.string.llenar_datos_det_usu), Toast.LENGTH_LONG).show()
            setDialog(false)
        }
    }

    fun eliminarImagenPerfil(){
        Firebase.firestore
            .collection("users")
            .document(user!!.uid)
            .get()
            .addOnSuccessListener {
                if(it.toObject(User::class.java)!!.imageReference != "p-1668873917292.jpg"){
                    FirebaseStorage.getInstance().reference.child(it.toObject(User::class.java)!!.imageReference).delete()
                }

            }
    }

    private fun verificarEmail(user: FirebaseUser){
        user.sendEmailVerification().addOnCompleteListener(this){
            if(it.isSuccessful){
                Toast.makeText(baseContext, R.string.email_enviado, Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(baseContext, R.string.email_error, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setDialog(show: Boolean) {
        if (show) dialog.show() else dialog.dismiss()
    }

    private fun onActivityResult(resultCode:Int, result: ActivityResult){
        if( resultCode == Activity.RESULT_OK ){
            setDialog(true)
            val fecha = Date()
            imageReference = "p-${fecha.time}.jpg"
            datatime = "/${imageReference}"
             if( codigoArchivo == 2 ){
                val data = result.data
                if(data!=null){
                    selectedImageUri = data.data
                    dibujarImagen(selectedImageUri!!)
                    setDialog(false)
                }
            }
        }
    }

    fun dibujarImagen(url:Uri){
        setDialog(false)
        imageUri = url.toString()

        val image = binding.imagePerfil

        Glide.with( baseContext )
            .load(imageUri)
            .into(image)
    }

    fun seleccionarFoto(){
        val i = Intent()
        i.type = "image/*"
        i.action = Intent.ACTION_GET_CONTENT
        codigoArchivo = 2
        resultLauncher.launch(i)
    }

}