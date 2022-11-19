package com.example.unilocal.activities

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.unilocal.R
import com.example.unilocal.adapter.ModeratorAdapter
import com.example.unilocal.adapter.PlaceAdapter
import com.example.unilocal.bd.*
import com.example.unilocal.databinding.ActivityDetalleLugarUsuarioBinding

import com.example.unilocal.fragments.DialogSchedulesFragment
import com.example.unilocal.fragments.InicioFragment
import com.example.unilocal.models.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.ArrayList

class DetalleLugarUsuarioActivity : AppCompatActivity(), OnMapReadyCallback, DialogSchedulesFragment.onHorarioCreadoListener {
    lateinit var binding: ActivityDetalleLugarUsuarioBinding
    lateinit var placeAdapter: PlaceAdapter
    lateinit var cities: ArrayList<City>
    lateinit var categories: ArrayList<Category>
    lateinit var dialog: Dialog
    lateinit var horarios: ArrayList<Schedule>
    lateinit var gMap:GoogleMap
    private val defaultLocation = LatLng(4.550923, -75.6557201)
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private var position:Position? = null
    var codePlace:String? = ""
    var imagenes:ArrayList<String> = ArrayList()
    var codigoArchivo: Int = 0
    var user:FirebaseUser? = null
    var pos: Int = -1
    var categoryPosition: Int = -1
    var cityPosition: Int = -1
    var place: Place? = null
    var placesByUser: ArrayList<Place> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleLugarUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        horarios = ArrayList()
        codePlace =  intent.extras!!.getString("code")
        categories = ArrayList()
        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult() ) {
            onActivityResult(it.resultCode, it)
        }
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setView(R.layout.dialogo_progreso)
        dialog = builder.create()
        cities = ArrayList()
        user = FirebaseAuth.getInstance().currentUser
        placeAdapter = PlaceAdapter(placesByUser, "usuario",this)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapa_crear_lugar) as SupportMapFragment
        mapFragment.getMapAsync(this)
        setDialog(true)
        if(user != null){
           Firebase.firestore
               .collection("placesF")
               .whereEqualTo("key",codePlace)
               .get()
               .addOnSuccessListener {
                   for(doc in it){
                       place = doc.toObject(Place::class.java)
                       place!!.key = doc.id
                       if(place != null){
                           imagenes = place!!.images
                           horarios = place!!.schedules
                           position = place!!.position
                           gMap.addMarker(MarkerOptions().position(LatLng(place!!.position!!.lat, place!!.position!!.lng)).title(place!!.name).visible(true))!!.tag = place!!.key
                           binding.nombreLayout.hint = place!!.name
                           binding.telefonoLayout.hint = place!!.phones[0]
                           binding.campoDireccionLayout.hint= place!!.address
                           binding.descripcionLayout.hint = place!!.description
                       }

                   }
                   loadCategories()
                   loadCities()
                   setDialog(false)
               }

        }
        binding.btnEliminarLugarUsuario.setOnClickListener{deletePlace()}
        binding.btnGuardarCambiosLugarUsuario.setOnClickListener { updatePlace() }
        binding.btnTomarFoto.setOnClickListener { tomarFoto() }
        binding.btnAsignarHorario.setOnClickListener { mostrarDialogo()}
        binding.btnSelArchivo.setOnClickListener { seleccionarFoto() }

    }

    fun deletePlace(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.txt_eliminar_lugar)
        builder.setMessage(R.string.txt_eliminar_lugar_pregunta)

        builder.setPositiveButton(R.string.txt_si) { dialogInterface, which ->
            Log.e("Images", place!!.imageReference[0])
            place!!.imageReference.forEach{
                FirebaseStorage.getInstance().reference.child(it).delete()
            }
            Firebase.firestore
                .collection("placesF")
                .document(codePlace!!)
                .delete()
            placesByUser.clear()
            if(user!=null) {
                Firebase.firestore
                    .collection("placesF")
                    .whereEqualTo("idCreator", user!!.uid)
                    .whereEqualTo("status",StatusPlace.ACEPTADO)
                    .get()
                    .addOnSuccessListener {
                        for(doc in it){
                            val place = doc.toObject(Place::class.java)
                            place.key = doc.id
                            placesByUser.add(place)
                        }
                    }
                placeAdapter.notifyItemRemoved(pos)
            }
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        builder.setNeutralButton(R.string.txt_cancel){dialogInterface , which -> }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    fun loadCategories(){
        categories.clear()
        Firebase.firestore
            .collection("categoriesF")
            .get()
            .addOnSuccessListener {
                for(doc in it){
                    val categoryF = doc.toObject(Category::class.java)
                    categoryF.key = doc.id
                    categories.add(categoryF)
                }
                Log.e("place", place.toString())
                var category = Categories.getById(place!!.idCategory, categories)
                var position = categories.indexOf(category)
                var adapter= ArrayAdapter(this,android.R.layout.simple_spinner_item,categories)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.categoryEdit.adapter= adapter
                binding.categoryEdit.setSelection(position)
                binding.categoryEdit.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        categoryPosition = p2
                    }
                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }
                }
            }
    }

    fun tomarFoto(){
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                resultLauncher.launch(takePictureIntent)
                codigoArchivo = 1
            }
        }

    }

    override fun elegirHorario(horario: Schedule) {
        horarios.add(horario)
    }

    fun seleccionarFoto(){
        val i = Intent()
        i.type = "image/*"
        i.action = Intent.ACTION_GET_CONTENT
        codigoArchivo = 2
        resultLauncher.launch(i)

    }

    private fun onActivityResult(resultCode:Int, result: ActivityResult){
        if( resultCode == Activity.RESULT_OK ){
            setDialog(true)
            val fecha = Date()
            val storageRef = FirebaseStorage.getInstance()
                .reference
                .child("/p-${fecha.time}.jpg")
            if( codigoArchivo == 1 ){
                val data = result.data?.extras
                if( data?.get("data") is Bitmap){
                    val imageBitmap = data?.get("data") as Bitmap
                    val baos = ByteArrayOutputStream()
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                    val data = baos.toByteArray()
                    storageRef.putBytes(data).addOnSuccessListener {
                        storageRef.downloadUrl.addOnSuccessListener {
                            dibujarImagen(it)
                        }
                    }.addOnFailureListener {
                        Snackbar.make(binding.root, "${it.message}", Snackbar.LENGTH_LONG).show()
                    }
                }
            }else if( codigoArchivo == 2 ){
                val data = result.data
                if(data!=null){
                    val selectedImageUri: Uri? = data.data
                    if(selectedImageUri!=null){
                        storageRef.putFile(selectedImageUri).addOnSuccessListener {
                            storageRef.downloadUrl.addOnSuccessListener {
                                dibujarImagen(it)
                            }
                        }.addOnFailureListener {
                            Snackbar.make(binding.root, "${it.message}", Snackbar.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }

    fun dibujarImagen(url: Uri){
        setDialog(false)
        imagenes.add(url.toString())

        var imagen = ImageView(baseContext)
        imagen.layoutParams = LinearLayout.LayoutParams(300, 310)
        binding.imagenesSeleccionadas.addView(imagen)

        Glide.with( baseContext )
            .load(url.toString())
            .into(imagen)
    }

    private fun setDialog(show: Boolean) {
        if (show) dialog.show() else dialog.dismiss()
    }

    fun loadCities(){
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

                var city = Cities.obtener(place!!.idCity, cities)
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

    fun updatePlace(){
        var name = binding.nombreLocalEdit.text.toString()
        var description = binding.descripcionEdit.text.toString()
        var phone = binding.telefonoEdit.text.toString()
        var address = binding.direccionEdit.text.toString()
        var idCity = cities[cityPosition].key
        var idCategory  = categories[categoryPosition].key

        setDialog(true)

        if(name.isEmpty()){
            name = binding.nombreLayout.hint.toString()
        }

        if(description.isEmpty()){
            description = binding.descripcionLayout.hint.toString()
        }

        if(phone.isEmpty()){
            phone = binding.telefonoLayout.hint.toString()
        }

        if(address.isEmpty()){
            address = binding.campoDireccionLayout.hint.toString()
        }

        if(name.isNotEmpty() && description.isNotEmpty() && phone.isNotEmpty() && address.isNotEmpty() &&idCity != "" && idCategory !=""){
            if(position!= null){
                if(imagenes.isNotEmpty() && horarios.isNotEmpty()){
                    val newPlace = Place(place!!.id,name,description,user!!.uid, place!!.status,idCategory,position!!,address,idCity)
                    val phones:ArrayList<String> = ArrayList()
                    phones.add(phone)
                    newPlace.images = imagenes
                    newPlace.schedules = horarios
                    newPlace.phones= phones
                    newPlace.key = place!!.key
                    Firebase.firestore
                        .collection("placesF")
                        .document(place!!.key)
                        .set(newPlace)
                        .addOnSuccessListener {
                            Snackbar.make(binding.root,getString(R.string.lugar_actualizado), Toast.LENGTH_LONG).show()
                            setDialog(false)
                            Handler(Looper.getMainLooper()).postDelayed({
                                finish()
                            },4000)
                        }
                }
            }else{
                Snackbar.make(binding.root,getString(R.string.campo_vacio), Toast.LENGTH_LONG).show()
                setDialog(false)
            }
        }else{
            Snackbar.make(binding.root,getString(R.string.campo_vacio), Toast.LENGTH_LONG).show()
            setDialog(false)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        gMap = googleMap
        gMap.uiSettings.isZoomControlsEnabled = true

        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation,15f))

        gMap.setOnMapClickListener {
            if(position == null){
                position = Position()
            }
            position!!.lng = it.longitude
            position!!.lat = it.latitude

            gMap.clear()
            gMap.addMarker(MarkerOptions().position(it).title(R.string.lugar_aca.toString()))}
    }

    fun mostrarDialogo(){
        val dialog = DialogSchedulesFragment()
        dialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogoTitulo)
        dialog.listener = this
        dialog.show(supportFragmentManager, "Agregar")

    }

}