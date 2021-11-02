package com.shatrovmaxim.myapplication.utils

import com.shatrovmaxim.myapplication.repository.entity.EventEntity
import java.util.*

class EventMapMaker() {
    val HOUR_TO_MILLIS = 3600000L

    fun getMapOfEvents(events: List<EventEntity>): Map<Int, EventEntity> {
        val eventsMap: MutableMap<Int, EventEntity> = mutableMapOf()
        var calendar: Calendar = Calendar.getInstance()

        if (!events.isEmpty()) {
            events.forEach {
                var timePointer = it.date_start.time
                calendar.timeInMillis = timePointer
                eventsMap.put(calendar.get(Calendar.HOUR_OF_DAY), it)
                timePointer += HOUR_TO_MILLIS
                while (timePointer<it.date_finish.time){
                    calendar.timeInMillis = timePointer
                    eventsMap.put(calendar.get(Calendar.HOUR_OF_DAY), it)
                    timePointer += HOUR_TO_MILLIS
                }
            }
        }
        return eventsMap
    }
}