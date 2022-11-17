package com.colinjp.inblo.domain.model

import com.colinjp.inblo.datasource.network.calendar.EventDTO
import com.colinjp.inblo.util.AndroidParcel
import com.colinjp.inblo.util.AndroidParcelize
import kotlinx.serialization.SerialName


@AndroidParcelize
class Event (
    val id: Int,
    val horseId: Int?,
    val userId: Int?,
    val date: String,
    val dateEnd: String?,
    val title: String,
    val eventType: String,
    val start: String,
    val end: String,
    val notes: String?,
    val user: User?,
    val horse: Horse?,
    val createdAt: String?,
    val updatedAt: String?
): AndroidParcel {
    companion object {
        public fun createFromDto(
            eventDTO: EventDTO
        ): Event {
            return Event(
                id = eventDTO.id,
                horseId = eventDTO.horseId,
                userId = eventDTO.userId,
                date = eventDTO.date,
                dateEnd = eventDTO.dateEnd,
                title = eventDTO.title,
                eventType = eventDTO.eventType,
                start = eventDTO.start,
                end = eventDTO.end,
                notes= eventDTO.notes,
                user = User.createFromDto(eventDTO.user),
                horse = Horse.createFromDto(eventDTO.horse),
                createdAt= eventDTO.createdAt,
                updatedAt= eventDTO.updatedAt
            )
        }
    }
}