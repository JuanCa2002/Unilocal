package com.example.unilocal.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DetalleLugarActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetalleLugarBinding
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
                    }
                }
        }
    }

}