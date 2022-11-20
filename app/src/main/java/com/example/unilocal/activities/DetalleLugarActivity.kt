package com.example.unilocal.activities

import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.unilocal.R
import com.example.unilocal.adapter.ImagesViewPager
import com.example.unilocal.adapter.PlaceAdapter
import com.example.unilocal.adapter.ViewPagerAdapter
import com.example.unilocal.bd.Places
import com.example.unilocal.bd.Usuarios
import com.example.unilocal.databinding.ActivityDetalleLugarBinding
import com.example.unilocal.fragments.FavoritesFragment
import com.example.unilocal.models.Place
import com.example.unilocal.models.User
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DetalleLugarActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetalleLugarBinding
    lateinit var dialog: Dialog
    var codePlace:String?= ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleLugarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        codePlace = intent.extras!!.getString("code")

        if(codePlace != null){
            Firebase.firestore
                .collection("placesF")
                .document(codePlace!!)
                .get()
                .addOnSuccessListener {
                    var place = it.toObject(Place::class.java)
                    if(place != null) {
                        place.key = it.id
                        binding.namePlace.text = place!!.name
                        binding.viewPaper.adapter = ViewPagerAdapter(this, codePlace!!)
                        TabLayoutMediator(binding.tabs, binding.viewPaper){tab, pos ->
                            when(pos){
                                0 -> tab.text = getString(R.string.txt_info_lugar)
                                1 -> tab.text = getString(R.string.txt_comentario)
                            }
                        }.attach()
                        binding.imagesList.adapter = ImagesViewPager(this, place.images)
                        Firebase.firestore
                            .collection("users")
                            .document(place.idCreator)
                            .get()
                            .addOnSuccessListener { u->
                                val image = binding.imageCreator
                                Glide.with( baseContext )
                                    .load(u.toObject(User::class.java)!!.imageUri)
                                    .into(image)
                            }
                    }
                }
        }
    }

}