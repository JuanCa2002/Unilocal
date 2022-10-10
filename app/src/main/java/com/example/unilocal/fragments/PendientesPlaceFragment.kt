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
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

class PendientesPlaceFragment : Fragment() {
    lateinit var binding: FragmentPendientesPlaceBinding
    lateinit var places: ArrayList<Place>
    lateinit var adapterPlace: PlaceAdapter
    var codeModerator:Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(arguments != null){
            codeModerator = requireArguments().getInt("code_moderator")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPendientesPlaceBinding.inflate(inflater,container,false)
        places = Places.listByStatus(StatusPlace.SIN_REVISAR)
        adapterPlace = PlaceAdapter(places,"Busqueda")
        binding.listPlacesPending.adapter = adapterPlace
        binding.listPlacesPending.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)

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
                        Places.changeStatus(codePlace, StatusPlace.ACEPTADO, codeModerator)
                        places.remove(place)
                        adapterPlace.notifyItemRemoved(pos)
                        Snackbar.make(binding.listPlacesPending, R.string.txt_lugar_aceptado, Snackbar.LENGTH_LONG)
                            .setAction(R.string.txt_deshacer, View.OnClickListener {
                                Places.changeStatus(codePlace, StatusPlace.SIN_REVISAR,0)
                                places.add(pos,place!!)
                                adapterPlace.notifyItemInserted(pos)
                            }).show()
                    }
                    ItemTouchHelper.RIGHT -> {
                        Places.changeStatus(codePlace, StatusPlace.RECHAZADO, codeModerator)
                        places.remove(place)
                        adapterPlace.notifyItemRemoved(pos)
                        Snackbar.make(binding.listPlacesPending, R.string.txt_lugar_rechazado, Snackbar.LENGTH_LONG)
                            .setAction(R.string.txt_deshacer, View.OnClickListener {
                                Places.changeStatus(codePlace, StatusPlace.SIN_REVISAR,0)
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
        fun newInstance(codeModerator: Int):PendientesPlaceFragment{
            val args = Bundle()
            args.putInt("code_moderator",codeModerator)
            val fragment = PendientesPlaceFragment()
            fragment.arguments = args
            return fragment
        }
    }
}