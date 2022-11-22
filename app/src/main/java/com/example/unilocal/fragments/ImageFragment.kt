package com.example.unilocal.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.unilocal.R
import com.example.unilocal.activities.DetalleLugarUsuarioActivity
import com.example.unilocal.databinding.FragmentImageBinding
import com.example.unilocal.models.Place
import com.example.unilocal.models.User
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class ImageFragment : Fragment() {

    private var param1: String? = null
    var origen: String? = null
    var imageReference: String? = null
    var key: String? = null
    private lateinit var binding:FragmentImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString("url_image")
            origen = it.getString("origen")
            imageReference = it.getString("imageReference")
            key = it.getString("keyPlace")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentImageBinding.inflate(inflater, container, false)

        Glide.with( this )
            .load(param1)
            .into(binding.urlImage)

        if(origen == "Visualizacion"){
            binding.btnEliminarImagen.visibility = View.GONE
        }else{
            binding.btnEliminarImagen.setOnClickListener { eliminarImagen() }
            binding.btnEliminarImagen.visibility = View.VISIBLE
        }

        return binding.root
    }

    fun eliminarImagen(){
        Firebase.firestore
            .collection("placesF")
            .document(key!!)
            .get()
            .addOnSuccessListener {
                val place = it.toObject(Place::class.java)
                place!!.key = it.id
                val imagesReferences = place.imageReference
                if(imagesReferences.size > 1){

                    val imagesUrls = place.images
                    FirebaseStorage.getInstance().reference.child(imageReference!!).delete()
                    imagesReferences.removeAt(imagesReferences.indexOf(imageReference))
                    imagesUrls.removeAt(imagesUrls.indexOf(param1))

                    place.images = imagesUrls
                    place.imageReference =imagesReferences
                    Firebase.firestore
                        .collection("placesF")
                        .document(key!!)
                        .set(place)
                        .addOnSuccessListener {
                            Snackbar.make(binding.root, getString(R.string.imagen_correcta), Snackbar.LENGTH_LONG).show()
                            val intent = Intent(requireContext(), DetalleLugarUsuarioActivity::class.java)
                            intent.putExtra("code", key)
                            startActivity(intent)
                        }
                }else{
                    Snackbar.make(binding.root, getString(R.string.lugar_sin_imagen), Snackbar.LENGTH_LONG).show()
                }
            }
    }

    companion object {
       fun newInstance(param1: String, origen:String, imageReference: String, key:String) =
                ImageFragment().apply {
                    arguments = Bundle().apply {
                        putString("origen", origen)
                        putString("url_image",param1)
                        putString("imageReference", imageReference)
                        putString("keyPlace",key )
                    }
                }
    }
}