package com.example.unilocal.fragments

import android.graphics.Canvas
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.unilocal.R
import com.example.unilocal.adapter.PlaceAdapter
import com.example.unilocal.bd.Places
import com.example.unilocal.databinding.ActivityModeratorBinding
import com.example.unilocal.databinding.FragmentPendientesPlaceBinding
import com.example.unilocal.models.Place
import com.example.unilocal.models.StatusPlace
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

class PendientesPlaceFragment : Fragment() {
    lateinit var binding: FragmentPendientesPlaceBinding
    lateinit var places: ArrayList<Place>
    lateinit var adapterPlace: PlaceAdapter
    var codeModerator:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPendientesPlaceBinding.inflate(inflater,container,false)
        var user = FirebaseAuth.getInstance().currentUser
        if(user != null){
            codeModerator = user.uid
        }
        places = ArrayList()
        Firebase.firestore
            .collection("placesF")
            .whereEqualTo("status", StatusPlace.SIN_REVISAR)
            .get()
            .addOnSuccessListener {
                for(doc in it){
                    val place = doc.toObject(Place::class.java)
                    place.key = doc.id
                    places.add(place)
                }
                adapterPlace = PlaceAdapter(places,"Busqueda")
                binding.listPlacesPending.adapter = adapterPlace
                binding.listPlacesPending.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)

            }
        val simpleCallback: ItemTouchHelper.SimpleCallback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                var pos = viewHolder.adapterPosition
                val codePlace = places[pos].key
                when(direction) {
                    ItemTouchHelper.LEFT -> {
                        Firebase.firestore
                            .collection("placesF")
                            .document(codePlace)
                            .get()
                            .addOnSuccessListener {
                                val place = it.toObject(Place::class.java)
                                place!!.key = it.id
                                place.status = StatusPlace.ACEPTADO
                                place.idModeratorReview = codeModerator
                                Firebase.firestore
                                    .collection("placesF")
                                    .document(it.id)
                                    .set(place)
                                adapterPlace.notifyItemRemoved(pos)
                                Snackbar.make(
                                    binding.listPlacesPending,
                                    R.string.txt_lugar_aceptado,
                                    Snackbar.LENGTH_LONG
                                )
                                    .setAction(R.string.txt_deshacer, View.OnClickListener {
                                       Firebase.firestore
                                           .collection("placesF")
                                           .document(codePlace)
                                           .get()
                                           .addOnSuccessListener {p->
                                               val place = p.toObject(Place::class.java)
                                               place!!.key = p.id
                                               place.status = StatusPlace.SIN_REVISAR
                                               place.idModeratorReview = ""
                                               Firebase.firestore
                                                   .collection("placesF")
                                                   .document(p.id)
                                                   .set(place)
                                               adapterPlace.notifyItemInserted(pos)
                                           }
                                    }).show()
                            }
                    }
                    ItemTouchHelper.RIGHT -> {
                        Firebase.firestore
                            .collection("placesF")
                            .document(codePlace)
                            .get()
                            .addOnSuccessListener {
                                val place = it.toObject(Place::class.java)
                                place!!.key = it.id
                                place.status = StatusPlace.RECHAZADO
                                place.idModeratorReview = codeModerator
                                Firebase.firestore
                                    .collection("placesF")
                                    .document(it.id)
                                    .set(place)
                                adapterPlace.notifyItemRemoved(pos)
                                Snackbar.make(
                                    binding.listPlacesPending,
                                    R.string.txt_lugar_rechazado,
                                    Snackbar.LENGTH_LONG
                                )
                                    .setAction(R.string.txt_deshacer, View.OnClickListener {
                                        Firebase.firestore
                                            .collection("placesF")
                                            .document(codePlace)
                                            .get()
                                            .addOnSuccessListener {p->
                                                val place = p.toObject(Place::class.java)
                                                place!!.key = p.id
                                                place.status = StatusPlace.SIN_REVISAR
                                                place.idModeratorReview = ""
                                                Firebase.firestore
                                                    .collection("placesF")
                                                    .document(p.id)
                                                    .set(place)
                                                adapterPlace.notifyItemInserted(pos)
                                            }
                                    }).show()
                            }
                    }
                }
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                RecyclerViewSwipeDecorator.Builder(c,recyclerView,viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green))
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(requireContext(),R.color.red))
                    .addSwipeLeftLabel(getString(R.string.txt_aceptar))
                    .addSwipeRightLabel(getString(R.string.txt_rechazar))
                    .create()
                    .decorate()
            }
        }
        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(binding.listPlacesPending)
        return binding.root
    }

    companion object{
        fun newInstance(codeModerator: String?):PendientesPlaceFragment{
            val args = Bundle()
            args.putString("code_moderator",codeModerator)
            val fragment = PendientesPlaceFragment()
            fragment.arguments = args
            return fragment
        }
    }
}