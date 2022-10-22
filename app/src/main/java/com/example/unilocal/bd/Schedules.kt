package com.example.unilocal.bd

import com.example.unilocal.models.DayWeek
import com.example.unilocal.models.Schedule

object Schedules {

    val schedules:ArrayList<Schedule> = ArrayList()

    fun obtenerTodos():ArrayList<DayWeek>{
        val todosDias:ArrayList<DayWeek> = ArrayList()
        todosDias.addAll(DayWeek.values())
        return todosDias
    }

    fun obtenerFinSemana():ArrayList<DayWeek>{
        val todosDias:ArrayList<DayWeek> = ArrayList()
        todosDias.add(DayWeek.VIERNES)
        todosDias.add(DayWeek.SABADO)
        return todosDias
    }

    fun obtenerEntresSemana():ArrayList<DayWeek>{
        val todosDias:ArrayList<DayWeek> = ArrayList()
        todosDias.add(DayWeek.LUNES)
        todosDias.add(DayWeek.MARTES)
        todosDias.add(DayWeek.MIERCOLES)
        todosDias.add(DayWeek.JUEVES)
        todosDias.add(DayWeek.VIERNES)
        todosDias.add(DayWeek.SABADO)
        todosDias.add(DayWeek.DOMINGO)
        return todosDias
    }

    fun agregarHorario(horario:Schedule):Schedule{
        horario.id = schedules.size +1
        schedules.add(horario)
        return horario
    }
}