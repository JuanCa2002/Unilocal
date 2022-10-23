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
import com.example.unilocal.activities.MainActivity
import com.example.unilocal.bd.Places
import com.example.unilocal.databinding.FragmentInicioBinding
import com.example.unilocal.models.StatusPlace
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class InicioFragment : Fragment(), OnMapReadyCallback {

    lateinit var binding: FragmentInicioBinding
    var code:Int = -1
    lateinit var gMap:GoogleMap
    private var tienePermiso = false
    private val defaultLocation = LatLng(4.550923, -75.6557201)
    var bundle:Bundle = Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        Places.listByStatus(StatusPlace.ACEPTADO).forEach{
            gMap.addMarker(MarkerOptions().position(LatLng(it.position.lat, it.position.lng)).title(it.name).visible(true))
        }
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
                            gMap.addMarker(MarkerOptions().position(latLng).title("Marcador por defecto"))
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



}