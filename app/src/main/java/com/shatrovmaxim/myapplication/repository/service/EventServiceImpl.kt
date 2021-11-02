package com.shatrovmaxim.myapplication.repository.service

import com.shatrovmaxim.myapplication.repository.EventRepository
import com.shatrovmaxim.myapplication.repository.entity.EventEntity
import java.util.*

class EventServiceImpl(private val eventRepository: EventRepository) : EventService {

    override fun findAll(): List<EventEntity> {
        return eventRepository.findAll()
    }

    override fun getEventsByCalendarDate(calendar: Calendar): List<EventEntity> {
        return eventRepository.getAllByCalendarDate(calendar)
    }

    override fun createEvent(eventEntity: EventEntity) : EventEntity {
        return eventRepository.save(eventEntity)
    }

    override fun readEventById(id: Int): EventEntity {
        return eventRepository.getById(id)
    }

    override fun updateEvent(eventEntity: EventEntity) {
        eventRepository.save(eventEntity)
    }

    override fun deleteEvent(eventEntity: EventEntity) {
        eventRepository.delete(eventEntity)
    }
}