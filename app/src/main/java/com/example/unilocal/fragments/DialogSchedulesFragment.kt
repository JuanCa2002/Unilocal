package com.example.unilocal.fragments

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.example.unilocal.bd.Categories
import com.example.unilocal.bd.Schedules
import com.example.unilocal.databinding.FragmentDialogSchedulesBinding
import com.example.unilocal.models.DayWeek
import com.example.unilocal.models.Schedule

class DialogSchedulesFragment : DialogFragment() {
    lateinit var binding: FragmentDialogSchedulesBinding
    lateinit var listener: DialogSchedulesFragment.onHorarioCreadoListener
    var selectionDay: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDialogSchedulesBinding.inflate(inflater,container, false)
        loadDaysOfWeek()
        binding.agregarHorario.setOnClickListener { addSchedule()}
        return binding.root
    }


    fun addSchedule(){
        val dia = selectionDay
        val horaInicio = binding.horaInicio.text.toString()
        val horaCierre = binding.horaFin.text.toString()

        if(dia!= -1 && horaInicio.isNotEmpty() && horaCierre.isNotEmpty() ){
            val horarios: ArrayList<DayWeek> = ArrayList()
            horarios.add(DayWeek.values()[dia])
            val horario = Schedules.agregarHorario(Schedule(horarios, horaInicio.toInt(),horaCierre.toInt()))
            listener.elegirHorario(horario)
            dismiss()
        }
    }

    fun loadDaysOfWeek(){
        var list = DayWeek.values()
        var adapter= ArrayAdapter(requireContext(), R.layout.simple_spinner_item,list)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.diaSemana.adapter= adapter
        binding.diaSemana.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectionDay = p2
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    interface onHorarioCreadoListener{
        fun elegirHorario(horario: Schedule)
    }
}