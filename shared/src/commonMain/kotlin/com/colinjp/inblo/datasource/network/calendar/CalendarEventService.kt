package com.colinjp.inblo.datasource.network.calendar

import com.colinjp.inblo.datasource.network.BooleanResponse
import com.colinjp.inblo.datasource.network.calendar.responses.AddEventResponse
import com.colinjp.inblo.datasource.network.calendar.responses.GetAllEventsResponse

interface CalendarEventService {
    suspend fun getAllEventsByMonthYear(
        stableId: Int,
        monthInt: String,
        year: String,
        horseId: Int
    ): GetAllEventsResponse

    suspend fun addEvent(
        horseId: Int?,
        userId: Int,
        stableId: Int,
        date: String,
        dateEnd: String?,
        title: String,
        eventType: String,
        start: String,
        end: String,
        memo: String,
        eventId: Int?
    ): AddEventResponse

    suspend fun deleteEvent(
        eventId: Int
    ): BooleanResponse
}