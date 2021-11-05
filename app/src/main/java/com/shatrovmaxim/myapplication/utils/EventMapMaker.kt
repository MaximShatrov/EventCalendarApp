package com.shatrovmaxim.myapplication.utils

import com.shatrovmaxim.myapplication.repository.entity.EventEntity
import java.util.*

//TODO() Документация
class EventMapMaker() {

    fun getMapOfEvents(events: List<EventEntity>): Map<Int, EventEntity> {
        val eventsMap: MutableMap<Int, EventEntity> = mutableMapOf()
        var calendar: Calendar = Calendar.getInstance()
        if (events.isNotEmpty()) {
            events.forEach {
                var timePointer = it.date_start.time
                calendar.timeInMillis = timePointer
                eventsMap.put(calendar.get(Calendar.HOUR_OF_DAY), it)
                timePointer += HOUR_IN_MILLISECONDS
                while (timePointer < it.date_finish.time) {
                    calendar.timeInMillis = timePointer
                    eventsMap.put(calendar.get(Calendar.HOUR_OF_DAY), it)
                    timePointer += HOUR_IN_MILLISECONDS
                }
            }
        }
        return eventsMap
    }

    companion object {
        val HOUR_IN_MILLISECONDS = 3600000L
    }
}