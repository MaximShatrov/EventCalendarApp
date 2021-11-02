package com.shatrovmaxim.myapplication

import com.shatrovmaxim.myapplication.repository.entity.EventEntity
import kotlinx.serialization.Serializable
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@Serializable
class DebugClass(val name: String, val language: String)

fun main() {
    val eventEntity: EventEntity = EventEntity(
        10, Timestamp(1636362000000),
        Timestamp(1636394400000), "Test event2", "Создан по кнопке!"
    )

    val calendar: Calendar = Calendar.getInstance()
    println(calendar.timeInMillis)
    val timestamp = Timestamp(calendar.timeInMillis)
    println(timestamp.time.toString())


    /*
    val date: Date = Date(eventEntity.date_start.time)
    println(date)
    println(date.time)

    val timestamp: Timestamp = eventEntity.date_start
    println(timestamp)

    println(getDateFromTimestamp(timestamp.time))
*/
    //timestamp.toLocalDateTime().toLocalDate()
    /*val timestampLong: Long = timestamp.getTime()
    val triggerTime = LocalDateTime.ofInstant(
        Instant.ofEpochMilli(timestampLong),
        TimeZone.getDefault().toZoneId()
    )*/


}


fun getDateTimeFromTimestamp(timestamp: Long): LocalDateTime? {
    return if (timestamp == 0L) null else LocalDateTime.ofInstant(
        Instant.ofEpochMilli(timestamp), TimeZone
            .getDefault().toZoneId()
    )
}

fun getDateFromTimestamp(timestamp: Long): LocalDate? {
    val date = getDateTimeFromTimestamp(timestamp)
    return date?.toLocalDate()
}
