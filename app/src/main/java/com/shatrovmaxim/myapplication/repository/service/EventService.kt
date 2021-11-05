package com.shatrovmaxim.myapplication.repository.service

import com.shatrovmaxim.myapplication.repository.entity.EventEntity
import java.io.Serializable
import java.util.*

//TODO() Документация
interface EventService : Serializable {
    //getAll
    fun findAll(): List<EventEntity>

    //getByDate
    fun getEventsByCalendarDate(calendar: Calendar): List<EventEntity>

    //create
    fun createEvent(eventEntity: EventEntity):EventEntity

    //read
    fun readEventById(id: Int): EventEntity

    //update
    fun updateEvent(eventEntity: EventEntity)

    //delete
    fun deleteEvent(eventEntity: EventEntity)
}