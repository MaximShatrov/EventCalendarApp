package com.shatrovmaxim.myapplication.utils

import com.shatrovmaxim.myapplication.repository.entity.EventEntity
import java.sql.Timestamp
import java.util.*
import kotlin.collections.ArrayList

class EventDayList {
    private var dayList: ArrayList<EventEntity> = ArrayList(24)
    //private val sdf: SimpleDateFormat = SimpleDateFormat("HH:MM")
    private val emptyEvent:EventEntity = EventEntity(0, Timestamp(0),Timestamp(0),"","")

    init {
        for (i in 0..23){
            dayList.add(emptyEvent)
        }
    }

    constructor(events: List<EventEntity>) {
        var calendar: Calendar = Calendar.getInstance()
              if (!events.isEmpty()) {
            events.forEach {
                calendar.timeInMillis = it.date_start.time
                val hour: Int = calendar.get(Calendar.HOUR_OF_DAY)
                dayList[hour] = it
            }
        }
    }

    fun getSize(): Int {
        return dayList.size
    }

    fun getEvent(position:Int):EventEntity{
        return dayList[position]
    }
}