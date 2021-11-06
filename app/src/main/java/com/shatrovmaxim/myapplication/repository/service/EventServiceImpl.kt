package com.shatrovmaxim.myapplication.repository.service

import com.shatrovmaxim.myapplication.repository.EventRepository
import com.shatrovmaxim.myapplication.repository.entity.EventEntity
import java.util.*

/*
* Имплементация сервиса EventService для работы с EventRepository
 */
class EventServiceImpl(private val eventRepository: EventRepository) : EventService {
    /*
    * Получение всех дел из репозитория
    * @return List<EventEntity> - возвращаемый List
    */
    override fun findAll(): List<EventEntity> {
        return eventRepository.findAll()
    }

    /*
    * Получить дела начинающиеся(!) в определенный день
    * @param calendar - Дата в которую нужно получить дела
    * @return List<EventEntity> - список найденных в этот день дел
    */
    override fun getEventsByCalendarDate(calendar: Calendar): List<EventEntity> {
        return eventRepository.getAllByCalendarDate(calendar)
    }

    /*
    * Создать новое дело
    * @param event - дело, которое нужно записать
    * @return EventEntity - созданное дело
    */
    override fun createEvent(eventEntity: EventEntity): EventEntity {
        return eventRepository.save(eventEntity)
    }


    /*
    * Получить дело по его id
    * @param id запрашиваемого EventEntity
    * @return запрашиваемый EventEntity
     */
    override fun readEventById(id: Int): EventEntity {
        return eventRepository.getById(id)
    }

    /*
    * Обновить запись
    * @param event - дело, которое нужно обновить
    * @return EventEntity - обновленное дело
    */
    override fun updateEvent(eventEntity: EventEntity) {
        eventRepository.save(eventEntity)
    }

    /*
    *Удаление дела из репозитория
    * @param event - удаляемое дело
    */
    override fun deleteEvent(eventEntity: EventEntity) {
        eventRepository.delete(eventEntity)
    }
}