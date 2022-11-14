package com.example.unilocal.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import com.example.unilocal.R
import com.example.unilocal.adapter.ModeratorAdapter
import com.example.unilocal.adapter.PlaceAdapter
import com.example.unilocal.bd.*
import com.example.unilocal.databinding.ActivityDetalleLugarUsuarioBinding
import com.example.unilocal.fragments.InicioFragment
import com.example.unilocal.models.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class DetalleLugarUsuarioActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetalleLugarUsuarioBinding
    lateinit var placeAdapter: PlaceAdapter
    lateinit var cities: ArrayList<City>
    lateinit var categories: ArrayList<Category>
    var codePlace:String? = ""
    var codeUser:String? = ""
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
        codePlace =  intent.extras!!.getString("code")
        categories = ArrayList()
        cities = ArrayList()
        user = FirebaseAuth.getInstance().currentUser
        placeAdapter = PlaceAdapter(placesByUser, "usuario")
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
                           binding.nombreLayout.hint = place!!.name
                           binding.telefonoLayout.hint = "311"
                           binding.campoDireccionLayout.hint= place!!.address
                           binding.descripcionLayout.hint = place!!.description
                       }

                   }
               }

        }
        loadCategories()
        loadCities()
         binding.btnEliminarLugarUsuario.setOnClickListener{deletePlace()}
        //binding.btnGuardarCambiosLugarUsuario.setOnClickListener { updatePlace() }

    }

    fun deletePlace(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.txt_eliminar_lugar)
        builder.setMessage(R.string.txt_eliminar_lugar_pregunta)

        builder.setPositiveButton(R.string.txt_si) { dialogInterface, which ->
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
                    val category = doc.toObject(Category::class.java)
                    category.key = doc.id
                    categories.add(category)
                }
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

//        if(name.isNotEmpty() && description.isNotEmpty() && phone.isNotEmpty() && address.isNotEmpty() &&idCity != "" && idCategory !=""){
//            val newPlace = Place(place!!.id,name,description,1, place!!.status,idCategory,0f,address,0f,idCity)
//
//            val phones:ArrayList<String> = ArrayList()
//            phones.add(phone)
//            newPlace.phones= phones
//            Places.updatePlace(place!!.id, newPlace)
//            this.finish()
//        }
    }

}