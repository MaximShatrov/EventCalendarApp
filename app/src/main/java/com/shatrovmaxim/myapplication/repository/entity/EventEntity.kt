package com.shatrovmaxim.myapplication.repository.entity


import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.sql.Timestamp

/**
 * Класс дела/события/записи в ежедневнике
 */
@Serializable
data class EventEntity(
    /**ID записи для идентификации уникальности в хранилище*/
    var id: Int,
    @Serializable(with = TimstampAsLongSerializer::class)
    /**Timestamp начала дела*/
    var date_start: Timestamp,  //in milliseconds
    @Serializable(with = TimstampAsLongSerializer::class)
    /**Timestamp окончания дела*/
    var date_finish: Timestamp, //in milliseconds
    /**Название дела*/
    var name: String,
    /**Описание дела*/
    var description: String
) : Comparable<EventEntity>, java.io.Serializable {


    override fun compareTo(other: EventEntity): Int {
        if (this.date_start.time < other.date_start.time) return 1
        if (this.date_start.time < other.date_start.time) return -1
        return 0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as EventEntity
        if (id != other.id) return false
        return true
    }

    override fun hashCode(): Int {
        return id
    }

    /**
     * Implements kotlinx.serialization.KSerializer. Необходим для сериализации типа Timestamp
     */
    object TimstampAsLongSerializer : KSerializer<Timestamp> {
        override val descriptor: SerialDescriptor =
            PrimitiveSerialDescriptor("Timestamp", PrimitiveKind.STRING)

        override fun serialize(encoder: Encoder, value: Timestamp) =
            encoder.encodeString(value.time.toString())

        override fun deserialize(decoder: Decoder): Timestamp =
            Timestamp(decoder.decodeString().toLong())
    }
}



