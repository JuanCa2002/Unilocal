package com.example.unilocal.fragments

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.checkSelfPermission
import com.example.unilocal.R
import com.example.unilocal.activities.DetalleLugarActivity
import com.example.unilocal.activities.MainActivity
import com.example.unilocal.bd.Categories
import com.example.unilocal.bd.Cities
import com.example.unilocal.bd.Places
import com.example.unilocal.databinding.FragmentInicioBinding
import com.example.unilocal.models.Place
import com.example.unilocal.models.StatusPlace
import com.example.unilocal.sqlite.UniLocalDbHelper
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


class InicioFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener{

    lateinit var bd:UniLocalDbHelper
    lateinit var binding: FragmentInicioBinding
    var code:Int = -1
    lateinit var gMap:GoogleMap
    private var tienePermiso = false
    private val defaultLocation = LatLng(4.550923, -75.6557201)
    var bundle:Bundle = Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bd = UniLocalDbHelper(requireContext())
        getLocationPermission()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInicioBinding.inflate(inflater,container,false)
        code = this.bundle.getInt("code")
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapa_principal) as SupportMapFragment
        mapFragment.getMapAsync(this)
        return binding.root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        gMap = googleMap
        gMap.uiSettings.isZoomControlsEnabled = true
        try {
            if (tienePermiso) {
                gMap.isMyLocationEnabled = true
                gMap.uiSettings.isMyLocationButtonEnabled = true
            } else {
                gMap.isMyLocationEnabled = false
                gMap.uiSettings.isMyLocationButtonEnabled = false
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
        val estado = (requireActivity()as MainActivity).estadoConexion
        if(estado){
            Firebase.firestore
                .collection("placesF")
                .whereEqualTo("status",StatusPlace.ACEPTADO)
                .get()
                .addOnSuccessListener {
                   for(doc in it){
                       var place = doc.toObject(Place::class.java)
                       place.key = doc.id
                       val placeBaseDatos = Place(place.key, place.name, place.description, place.position!!.lat, place.position!!.lng, place.address, place.idCategory, place.idCreator)
                       gMap.addMarker(MarkerOptions().position(LatLng(place.position!!.lat, place.position!!.lng)).title(place.name).visible(true))!!.tag = place.key
                       bd.createPlace(placeBaseDatos)
                   }
                }.addOnFailureListener{
                    Log.e("FAIL_LUGARES",it.message.toString())
                }
        }else{
           bd.listPlaces().forEach{
                gMap.addMarker(MarkerOptions().position(LatLng(it.position!!.lat, it.position!!.lng)).title(it.name).visible(true))!!.tag = it.key
           }
        }
        gMap.setOnInfoWindowClickListener (this)
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 15F))
    }

    private fun getLocationPermission() {
        if (checkSelfPermission(requireContext(),Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED) {
            tienePermiso = true
        } else {
            requestPermissions( arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }
    }

    private fun obtenerUbicacion() {
        try {
            if (tienePermiso) {
                val ubicacionActual =
                    LocationServices.getFusedLocationProviderClient(requireActivity()).lastLocation
                ubicacionActual.addOnCompleteListener(requireActivity()) {
                    if (it.isSuccessful) {
                        val ubicacion = it.result
                        if (ubicacion != null) {
                            val latLng = LatLng(ubicacion.latitude, ubicacion.longitude)
                            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15F)
                            )
                            gMap.addMarker(MarkerOptions().position(latLng).title(R.string.marcador_defecto.toString()))
                        }
                    } else {
                        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation,
                            15F))
                        gMap.uiSettings.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    override fun onInfoWindowClick(p0: Marker) {
        val intent = Intent(requireContext(), DetalleLugarActivity::class.java)
        intent.putExtra("code",p0.tag.toString())
        startActivity(intent)
    }


}