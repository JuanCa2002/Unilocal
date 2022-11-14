package com.example.unilocal.activities

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
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
import com.example.unilocal.bd.Categories
import com.example.unilocal.bd.Cities
import com.example.unilocal.bd.Places
import com.example.unilocal.databinding.ActivityCrearLugarBinding
import com.example.unilocal.fragments.DialogSchedulesFragment
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
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.text.FieldPosition
import java.util.*
import kotlin.collections.ArrayList

class CrearLugarActivity : AppCompatActivity(),DialogSchedulesFragment.onHorarioCreadoListener,
    OnMapReadyCallback {
    lateinit var binding: ActivityCrearLugarBinding
    lateinit var categories: ArrayList<Category>
    lateinit var cities: ArrayList<City>
    var cityPosition: Int = -1
    var newPlace:Place? = null
    var imagenes:ArrayList<String> = ArrayList()
    var codigoArchivo: Int = 0
    var user:FirebaseUser? = null
    var categoryPosition: Int = -1
    lateinit var horarios: ArrayList<Schedule>
    lateinit var gMap:GoogleMap
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private val defaultLocation = LatLng(4.550923, -75.6557201)
    private var tienePermiso = false
    private var position:Position? = null
    lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrearLugarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        horarios = ArrayList()
        cities = ArrayList()
        categories = ArrayList()
        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult() ) {
            onActivityResult(it.resultCode, it)
        }
        user = FirebaseAuth.getInstance().currentUser

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setView(R.layout.dialogo_progreso)
        dialog = builder.create()

        loadCities()
        loadCategories()
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapa_crear_lugar) as SupportMapFragment
        mapFragment.getMapAsync(this)
        binding.btnCreatePlace.setOnClickListener { createPlace() }
        binding.btnAsignarHorario.setOnClickListener { mostrarDialogo()}

        binding.btnTomarFoto.setOnClickListener { tomarFoto() }
        binding.btnSelArchivo.setOnClickListener { seleccionarFoto() }
    }

    fun tomarFoto(){
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                resultLauncher.launch(takePictureIntent)
                codigoArchivo = 1
            }
        }

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

    fun dibujarImagen(url:Uri){
        setDialog(false)
        imagenes.add(url.toString())

        var imagen = ImageView(baseContext)
        imagen.layoutParams = LinearLayout.LayoutParams(300, 310)
        binding.imagenesSeleccionadas.addView(imagen)

        Glide.with( baseContext )
            .load(url.toString())
            .into(imagen)
    }


    fun mostrarDialogo(){
        val dialog = DialogSchedulesFragment()
        dialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogoTitulo)
        dialog.listener = this
        dialog.show(supportFragmentManager, "Agregar")

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
                var adapter= ArrayAdapter(this,android.R.layout.simple_spinner_item,cities)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.cityPlace.adapter= adapter
                binding.cityPlace.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        cityPosition = p2
                    }
                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }
                }
            }
    }

    fun loadCategories(){
        categories.clear()
        Firebase.firestore
            .collection("categoriesF")
            .get()
            .addOnSuccessListener {
                for(doc in it){
                    val category = doc.toObject(Category::class.java)
                    category.key = doc.id
                    categories.add(category)
                }
                var adapter= ArrayAdapter(this,android.R.layout.simple_spinner_item,categories)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.categoryPlace.adapter= adapter
                binding.categoryPlace.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        categoryPosition = p2
                    }
                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }
                }
            }
    }

    fun createPlace(){
        val name = binding.placeName.text.toString()
        val description = binding.placeDescription.text.toString()
        val phone = binding.placePhone.text.toString()
        val address = binding.addressPlace.text.toString()
        val idCity = cities[cityPosition].key
        val idCategory  = categories[categoryPosition].key

        if(name.isEmpty()){
            binding.placeNameLayout.error = getString(R.string.txt_obligatorio)
        }else{
            binding.placeNameLayout.error = null
        }

        if(description.isEmpty()){
            binding.placeDescriptionLayout.error = getString(R.string.txt_obligatorio)
        }else{
            binding.placeDescriptionLayout.error = null
        }

        if(phone.isEmpty()){
            binding.placePhoneLayout.error = getString(R.string.txt_obligatorio)
        }else{
            binding.placePhoneLayout.error = null
        }

        if(address.isEmpty()){
            binding.addressPlaceLayout.error = getString(R.string.txt_obligatorio)
        }else{
            binding.addressPlaceLayout.error = null
        }

        if(name.isNotEmpty() && description.isNotEmpty() && phone.isNotEmpty() && horarios.isNotEmpty() && address.isNotEmpty() &&idCity != "" && idCategory !=""){
            if(position!= null){
                if(imagenes.isNotEmpty()){
                    newPlace = Place(Places.list().size+1,name,description,user!!.uid,StatusPlace.SIN_REVISAR,idCategory,position!!,address,idCity)
                    val phones:ArrayList<String> = ArrayList()
                    phones.add(phone)
                    newPlace!!.phones= phones
                    newPlace!!.schedules = horarios
                    newPlace!!.images = imagenes

                    Firebase.firestore
                        .collection("placesF")
                        .add(newPlace!!)
                        .addOnSuccessListener {
                            newPlace!!.key = it.id
                            Firebase.firestore
                                .collection("placesF")
                                .document(it.id)
                                .set(newPlace!!)
                                .addOnSuccessListener {
                                    Snackbar.make(binding.root,R.string.lugar_creado_exito,Toast.LENGTH_LONG).show()
                                    Handler(Looper.getMainLooper()).postDelayed({
                                        finish()
                                    },4000)
                                }
                        }
                        .addOnFailureListener{
                            Snackbar.make(binding.root,"${it.message}",Toast.LENGTH_LONG).show()
                        }
                }

            }else{
                Snackbar.make(binding.root,R.string.lugar_creado_fallo,Toast.LENGTH_LONG).show()
            }
        }else{
            Snackbar.make(binding.root,R.string.lugar_creado_fallo,Toast.LENGTH_LONG).show()
        }
    }

    override fun elegirHorario(horario: Schedule) {
        horarios.add(horario)
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

    private fun setDialog(show: Boolean) {
        if (show) dialog.show() else dialog.dismiss()
    }
}