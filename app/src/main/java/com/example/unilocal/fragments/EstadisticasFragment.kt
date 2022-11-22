package com.example.unilocal.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.unilocal.R
import com.example.unilocal.databinding.FragmentEstadisticasBinding
import com.example.unilocal.models.Category
import com.example.unilocal.models.City
import com.example.unilocal.models.Place
import com.example.unilocal.models.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


class EstadisticasFragment : Fragment() {
    lateinit var binding:FragmentEstadisticasBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEstadisticasBinding.inflate(inflater, container, false)
        stockCities()
        stockCategories()
        stockPlaces()
        stockUsers()
        return binding.root
    }


    fun stockCities(){
        val cities:ArrayList<City> = ArrayList()
        Firebase.firestore
            .collection("citiesF")
            .get()
            .addOnSuccessListener {
                for(doc in it){
                    val city = doc.toObject(City::class.java)
                    city.key = doc.id
                    cities.add(city)
                }
                binding.stockCitiesNumber.text = cities.size.toString()
            }
    }

    fun stockCategories(){
        val categories:ArrayList<Category> = ArrayList()
        Firebase.firestore
            .collection("categoriesF")
            .get()
            .addOnSuccessListener {
                for(doc in it){
                    val category = doc.toObject(Category::class.java)
                    category.key = doc.id
                    categories.add(category)
                }
                binding.stockCategoriesNumber.text = categories.size.toString()
            }
    }

    fun stockUsers(){
        val users:ArrayList<User> = ArrayList()
        Firebase.firestore
            .collection("users")
            .get()
            .addOnSuccessListener {
                for(doc in it){
                    val user = doc.toObject(User::class.java)
                    user.key = doc.id
                    users.add(user)
                }
                binding.stockUsersNumber.text = users.size.toString()
            }
    }

    fun stockPlaces(){
        val places:ArrayList<Place> = ArrayList()
        Firebase.firestore
            .collection("placesF")
            .get()
            .addOnSuccessListener {
                for(doc in it){
                    val place = doc.toObject(Place::class.java)
                    place.key = doc.id
                    places.add(place)
                }
                binding.stockPlacesNumber.text = places.size.toString()
            }
    }

}