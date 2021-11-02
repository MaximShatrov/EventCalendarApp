package com.shatrovmaxim.myapplication.repository

import com.shatrovmaxim.myapplication.repository.entity.EventEntity
import java.util.*

interface EventRepository {
    fun findAll(): MutableList<EventEntity>
    fun getById(id: Int): EventEntity
    fun getAllByCalendarDate(calendar: Calendar): MutableList<EventEntity>
    fun save(event: EventEntity): EventEntity
    fun delete(event: EventEntity)
    fun isEmpty(): Boolean
}