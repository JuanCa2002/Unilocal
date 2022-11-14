package com.example.unilocal.models

class Schedule () {

    var id:Int = 0

    constructor( dayOfWeek:ArrayList<DayWeek>,  startTime: Int, finishTime: Int):this(){
        this.dayOfWeek = dayOfWeek
        this.startTime = startTime
        this.finishTime = finishTime
    }

    constructor( id: Int,
                 dayOfWeek:ArrayList<DayWeek>,
                 startTime: Int,
                 finishTime: Int):this(dayOfWeek,startTime,finishTime){
                     this.id = id

                }

    var dayOfWeek:ArrayList<DayWeek> = ArrayList()
    var startTime: Int = 0
    var finishTime: Int = 0

    override fun toString(): String {
        return "Schedule(dayOfWeek=$dayOfWeek, startTime=$startTime, finishTime=$finishTime, id=$id)"
    }


}