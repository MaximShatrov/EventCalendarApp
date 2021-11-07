package com.shatrovmaxim.myapplication.repository.service

import com.shatrovmaxim.myapplication.repository.entity.EventEntity
import java.io.Serializable
import java.util.*

/**
 * Интерфейс CRUD сервиса для доступа к EventEntity
 */
interface EventService : Serializable {
    /**
     * Получение всех дел из репозитория
     * @return List<EventEntity> - возвращаемый List
     */
    fun findAll(): List<EventEntity>

    /**
     * Получить дела начинающиеся(!) в определенный день
     * @param calendar - Дата в которую нужно получить дела
     * @return List<EventEntity> - список найденных в этот день дел
     */
    fun getEventsByCalendarDate(calendar: Calendar): List<EventEntity>

    /**
     * Создать новое дело
     * @param event - дело, которое нужно записать
     * @return EventEntity - созданное дело
     */
    fun createEvent(eventEntity: EventEntity): EventEntity

    /**
     * Получить дело по его id
     * @param id запрашиваемого EventEntity
     * @return запрашиваемый EventEntity
     */
    fun readEventById(id: Int): EventEntity

    /**
     * Обновить запись
     * @param event - дело, которое нужно обновить
     * @return EventEntity - обновленное дело
     */
    fun updateEvent(eventEntity: EventEntity)

    /**
     *Удаление дела из репозитория
     * @param event - удаляемое дело
     */
    fun deleteEvent(eventEntity: EventEntity)
}