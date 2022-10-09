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

class DetalleLugarUsuarioActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetalleLugarUsuarioBinding
    var codePlace:Int = -1
    var codeUser:Int = -1
    var pos: Int = -1
    var categoryPosition: Int = -1
    lateinit var placeAdapter: PlaceAdapter
    lateinit var cities: ArrayList<City>
    var cityPosition: Int = -1
    lateinit var categories: ArrayList<Category>
    var place: Place? = null
    var placesByUser: ArrayList<Place> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleLugarUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sp = getSharedPreferences("sesion", Context.MODE_PRIVATE)
        codeUser = sp.getInt("id",0)
        pos = intent.extras!!.getInt("position")
        codePlace= intent.extras!!.getInt("code")
        placesByUser = Places.listByUser(codeUser, placesByUser)
        placeAdapter = PlaceAdapter(placesByUser,"usuario")
        place = Places.obtener(codePlace)

        if(place != null){
            binding.nombreLayout.hint = place!!.name
            binding.telefonoLayout.hint = "311"
            binding.campoDireccionLayout.hint= place!!.address
            binding.descripcionLayout.hint = place!!.description
        }
        loadCategories()
        loadCities()
        binding.btnEliminarLugarUsuario.setOnClickListener{deletePlace()}
        binding.btnGuardarCambiosLugarUsuario.setOnClickListener { updatePlace() }

    }

    fun deletePlace(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Eliminar Lugar")
        builder.setMessage("¿Esta seguro de eliminar este lugar?")

        builder.setPositiveButton("Si") { dialogInterface, which ->
            Places.deletePlace(codePlace)
            placesByUser.remove(place)
            placeAdapter.notifyItemRemoved(pos)
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("code",codeUser)
            startActivity(intent)
        }

        builder.setNeutralButton("Cancel"){dialogInterface , which -> }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    fun loadCategories(){
        categories = Categories.listar()
        Log.e("category nueva", place!!.idCategory.toString())
        var category = Categories.getById(place!!.idCategory)
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

    fun loadCities(){
        cities = Cities.listar()
        var city = Cities.obtener(place!!.idCity)
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

    fun updatePlace(){
        var name = binding.nombreLocalEdit.text.toString()
        var description = binding.descripcionEdit.text.toString()
        var phone = binding.telefonoEdit.text.toString()
        var address = binding.direccionEdit.text.toString()
        var idCity = cities[cityPosition].id
        var idCategory  = categories[categoryPosition].id

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

        if(name.isNotEmpty() && description.isNotEmpty() && phone.isNotEmpty() && address.isNotEmpty() &&idCity != -1 && idCategory !=-1){
            val newPlace = Place(place!!.id,name,description,1,
                place!!.status,idCategory,0f,address,0f,idCity)

            val phones:ArrayList<String> = ArrayList()
            phones.add(phone)
            newPlace.phones= phones
            Places.updatePlace(place!!.id, newPlace)
            this.finish()
        }
    }

}