package com.shatrovmaxim.myapplication.utils

import com.shatrovmaxim.myapplication.repository.entity.EventEntity
import java.util.*

/**
 * Класс утилита, преобразующий List<EventEntity> в Map<Int, EventEntity> для отрисовки в RecyclerView
 */
class EventMapMaker {

    /**
     * Каждый EvenyEntity из исходного List помещается в Map с Int ключом каждого часа в котором "проходит" это дело
     * Например, если event EventEntity начинается в 10:00 и заканчивается в 12:00, то в возвращаемой Map будет три записи относящиеся к event:
     * eventMap<10,event>, eventMap<11,event>, eventMap<12,event>. А значит в RecyclerView будет три блока для этого event с 10 до 13 часов
     * @param List<EventEntity> - исходный List событий
     * @return MutableMap<Int, EventEntity>
     */

    fun getMapOfEvents(events: List<EventEntity>): Map<Int, EventEntity> {
        val eventsMap: MutableMap<Int, EventEntity> = mutableMapOf()
        val calendar: Calendar = Calendar.getInstance()
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
        private val HOUR_IN_MILLISECONDS = 3600000L
    }
}