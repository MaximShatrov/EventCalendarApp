package com.shatrovmaxim.myapplication.repository

import android.content.Context.MODE_PRIVATE
import com.shatrovmaxim.myapplication.repository.entity.EventEntity
import com.shatrovmaxim.myapplication.repository.exceptions.EventNotFoundException
import com.shatrovmaxim.myapplication.utils.SubApplication
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList


class EventJsonFileRepository : EventRepository {

    private val FILE_NAME = "Events.json"
    private lateinit var eventsMutableList: MutableList<EventEntity>
    private val idSet: MutableSet<Int> = TreeSet<Int>()
    private var idPointer: Int = 0


    init {
        initEventslist()
    }

    override fun findAll(): MutableList<EventEntity> {
        return eventsMutableList
    }

    @Throws(EventNotFoundException::class)
    override fun getById(id: Int): EventEntity {
        eventsMutableList.forEach { if (it.id == id) return it }
        throw   EventNotFoundException("Event with id:${id} not found!")
    }

    @Throws(EventNotFoundException::class)
    override fun getAllByCalendarDate(calendar: Calendar): MutableList<EventEntity> {
        val resultEvents: MutableList<EventEntity> = ArrayList()
        eventsMutableList.forEach {
            val eventCalendar: Calendar = Calendar.getInstance()
            eventCalendar.timeInMillis = it.date_start.time
            if (LocalDateTime.ofInstant(calendar.toInstant(), calendar.timeZone.toZoneId())
                    .toLocalDate() == LocalDateTime.ofInstant(
                    eventCalendar.toInstant(),
                    eventCalendar.timeZone.toZoneId()
                ).toLocalDate()
            ) {
                resultEvents.add(it)
            }
        }
        return if (resultEvents.isNotEmpty()) resultEvents
        else {
            ArrayList()
            //throw EventNotFoundException("No events ${calendar.time}!")
        }
    }

    override fun save(event: EventEntity): EventEntity {
        if (eventsMutableList.contains(event)) {
            eventsMutableList.remove(event)
            eventsMutableList.add(event)
            saveEventsToFile()
            return event
        } else {
            if (superimpositionCheck(event)) {
                return event
            }
            event.id = idPointer++
            eventsMutableList.add(event)
            saveEventsToFile()
            return event
        }
    }

    override fun delete(event: EventEntity) {
        eventsMutableList.remove(event)
        saveEventsToFile()
        updateIdPointer()
    }

    override fun isEmpty(): Boolean {
        return eventsMutableList.size == 0
    }

    private fun initEventslist() {
        var fileExists = false
        val files = SubApplication.applicationContext().fileList()
        files.forEach {
            if (it == FILE_NAME) {
                fileExists = true
            }
        }
        if (fileExists) {
            readEventsFromFile()
            updateIdPointer()
        } else {
            readEventsFromAssets()
            updateIdPointer()
            saveEventsToFile()
        }
    }

    private fun readEventsFromFile() {
        val jsonString: String
        SubApplication.applicationContext().openFileInput(FILE_NAME).use {
            val bytes = ByteArray(it.available())
            it.read(bytes)
            jsonString = String(bytes)
        }
        eventsMutableList = Json.decodeFromString(jsonString)
    }

    private fun readEventsFromAssets() {
        val jsonString: String
        SubApplication.applicationContext().assets.open(FILE_NAME).use {
            val bytes = ByteArray(it.available())
            it.read(bytes)
            jsonString = String(bytes)
        }
        eventsMutableList = Json.decodeFromString(jsonString)
    }

    private fun saveEventsToFile() {
        val jsonString: String = Json.encodeToString(eventsMutableList)
        SubApplication.applicationContext().openFileOutput(FILE_NAME, MODE_PRIVATE).use {
            it.write(jsonString.toByteArray())
        }
    }

    private fun updateIdPointer() {
        eventsMutableList.forEach { idSet.add(it.id) }
        idPointer = idSet.last() + 1
    }

    private fun superimpositionCheck(event: EventEntity): Boolean {
        if (eventsMutableList.isNotEmpty()) {
            var imposition: Boolean
            eventsMutableList.forEach {
                imposition =
                    ((event.date_start.time >= it.date_start.time) && (event.date_start.time < it.date_finish.time)) ||
                            ((event.date_finish.time > it.date_start.time) && (event.date_finish.time <= it.date_finish.time)) ||
                            event.date_start.time <= it.date_start.time && event.date_finish.time >= it.date_finish.time
                if (imposition) return imposition
            }
        }
        return false
    }
}