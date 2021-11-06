package com.shatrovmaxim.myapplication.repository

import com.shatrovmaxim.myapplication.repository.entity.EventEntity
import java.util.*

/*
* Интерфейс репозитория объектов EventEntity
 */
interface EventRepository {
    /*
    * Получение всех дел из репозитория
    * @return MutableList<EventEntity> - возвращаемый List
     */
    fun findAll(): MutableList<EventEntity>

    /*
    * Получить дело по его id
    * @param id запрашиваемого EventEntity
    * @return запрашиваемый EventEntity
     */
    fun getById(id: Int): EventEntity

    /*
    * Получить дела начинающиеся(!) в определенный день
    * @param calendar - Дата в которую нужно получить дела
    * @return MutableList<EventEntity> - список найденных в этот день дел
     */
    fun getAllByCalendarDate(calendar: Calendar): MutableList<EventEntity>

    /*
    * Сохранить дело в репозиторий
    * @param event - дело, которое нужно записать
    * @return EventEntity - дело записанное в репозиторий с учетом его id в репозитории
     */
    fun save(event: EventEntity): EventEntity

    /*
    *Удаление дела из репозитория
    * @param event - удаляемое дело
     */
    fun delete(event: EventEntity)

    /*
    * Проверка наличия дел в репозитории
    * @return Boolean - true, если в репе пусто
     */
    fun isEmpty(): Boolean
}