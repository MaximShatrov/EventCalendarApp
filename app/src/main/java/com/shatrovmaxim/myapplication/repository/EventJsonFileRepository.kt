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

/**
 * Имлпементация репозитория EventRepository.
 * Хранит записи в json формате на устройстве в /data/data/com.shatrovmaxim.myapplication/files/Events.json
 */
class EventJsonFileRepository : EventRepository {

    private val FILE_NAME = "Events.json"
    private lateinit var eventsMutableList: MutableList<EventEntity>
    private val idSet: MutableSet<Int> = TreeSet<Int>()
    private var idPointer: Int = 0

    init {
        initEventsList()
    }

    /**
     * Получение всех дел из репозитория
     * @return MutableList<EventEntity> - возвращаемый List
     */
    override fun findAll(): MutableList<EventEntity> {
        return eventsMutableList
    }

    /**
     * Получить дело по его id
     * @param id запрашиваемого EventEntity
     * @return запрашиваемый EventEntity
     * @throws EventNotFoundException если такой записи нет
     */
    @Throws(EventNotFoundException::class)
    override fun getById(id: Int): EventEntity {
        eventsMutableList.forEach { if (it.id == id) return it }
        throw   EventNotFoundException("Event with id:${id} not found!")
    }


    /**
     * Получить дела начинающиеся(!) в определенный день
     * @param calendar - Дата в которую нужно получить дела
     * @return MutableList<EventEntity> - список найденных в этот день дел
     */
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
        return resultEvents
    }

    /**
     * Сохранение дела в репозиторий. Если в списке уже имеется дело с таким ID, обновляет его.
     * Если нет - проверяет на пересечение с другими и записывает его в файл, если перечечения нет.
     * Если дело имеет пересечения с другими, не записывает его в файл и возвращает в неизменном виде (не изменяет его id)
     * @param event - дело, которое нужно записать
     * @return EventEntity - возвращаемая запись.
     */
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

    /**
     * Удаление дела из eventsMutableList, сохранение eventsMutableList в файл, обновление ID указателя
     * @param event - удаляемое дело
     */
    override fun delete(event: EventEntity) {
        eventsMutableList.remove(event)
        saveEventsToFile()
        updateIdPointer()
    }

    /**
     * Проверка наличия дел в репозитории
     * @return Boolean - true, если в репе пусто
     */
    override fun isEmpty(): Boolean {
        return eventsMutableList.size == 0
    }

    private fun initEventsList() {
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