package com.example.unilocal.activities

import android.graphics.Canvas
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.unilocal.R
import com.example.unilocal.adapter.PlaceAdapter
import com.example.unilocal.bd.Places
import com.example.unilocal.databinding.ActivityModeratorBinding
import com.example.unilocal.models.Place
import com.example.unilocal.models.StatusPlace
import com.google.android.material.snackbar.Snackbar
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

class ModeratorActivity : AppCompatActivity() {
    lateinit var binding:ActivityModeratorBinding
    lateinit var places: ArrayList<Place>
    lateinit var adapterPlace: PlaceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModeratorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        places = Places.listByStatus(StatusPlace.SIN_REVISAR)

        adapterPlace = PlaceAdapter(places,"Busqueda")
        binding.listPlaces.adapter = adapterPlace
        binding.listPlaces.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)

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
                val codePlace = places[pos].id
                val place = Places.obtener(codePlace)
                when(direction){
                    ItemTouchHelper.LEFT -> {
                        Places.changeStatus(codePlace,StatusPlace.ACEPTADO)
                        places.remove(place)
                        adapterPlace.notifyItemRemoved(pos)
                      Snackbar.make(binding.listPlaces, "Lugar aceptado",Snackbar.LENGTH_LONG)
                          .setAction("Deshacer", View.OnClickListener {
                              Places.changeStatus(codePlace,StatusPlace.SIN_REVISAR)
                              places.add(pos,place!!)
                              adapterPlace.notifyItemInserted(pos)
                          }).show()
                    }
                    ItemTouchHelper.RIGHT -> {
                        Places.changeStatus(codePlace,StatusPlace.RECHAZADO)
                        places.remove(place)
                        adapterPlace.notifyItemRemoved(pos)
                        Snackbar.make(binding.listPlaces, "Lugar rechazado",Snackbar.LENGTH_LONG)
                            .setAction("Deshacer", View.OnClickListener {
                                Places.changeStatus(codePlace,StatusPlace.SIN_REVISAR)
                                places.add(pos,place!!)
                                adapterPlace.notifyItemInserted(pos)
                            }).show()
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
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(baseContext, R.color.green))
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(baseContext,R.color.red))
                    .addSwipeLeftLabel("Aceptar")
                    .addSwipeRightLabel("Rechazar")
                    .create()
                    .decorate()
            }
        }
        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(binding.listPlaces)


    }
}