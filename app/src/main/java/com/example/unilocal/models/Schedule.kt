package com.example.unilocal.models

class Schedule (var dayOfWeek:ArrayList<DayWeek>,
                var startTime: Int,
                var finishTime: Int) {

    var id:Int = 0

    constructor( id: Int,
                 dayOfWeek:ArrayList<DayWeek>,
                 startTime: Int,
                 finishTime: Int):this(dayOfWeek,startTime,finishTime){
                     this.id = id

                }

    override fun toString(): String {
        return "Schedule(dayOfWeek=$dayOfWeek, startTime=$startTime, finishTime=$finishTime, id=$id)"
    }


}