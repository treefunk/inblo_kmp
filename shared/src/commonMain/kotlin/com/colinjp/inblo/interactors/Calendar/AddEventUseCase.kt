package com.colinjp.inblo.interactors.Calendar

import com.colinjp.inblo.datasource.network.calendar.CalendarEventService
import com.colinjp.inblo.domain.model.Event
import com.colinjp.inblo.domain.util.CommonFlow
import com.colinjp.inblo.domain.util.asCommonFlow
import com.colinjp.inblo.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AddEventUseCase(
    private val calendarEventService: CalendarEventService
) {

    fun invoke(
        horseId: String?,
        userId: Int,
        stableId: Int,
        date: String,
        dateEnd: String?,
        title: String,
        eventType: String,
        start: String,
        end: String,
        memo: String,
        eventId: String?
    ): CommonFlow<DataState<Event>> = flow {
        emit(DataState.Loading(true))
        val eventId = if(eventId == "0") null else eventId?.toInt()
        val horseId = if(horseId == "0") null else horseId?.toInt()
        try {
            val event = calendarEventService.addEvent(
                horseId, userId, stableId, date, dateEnd, title, eventType, start, end, memo,
                eventId
            ).event
            emit(DataState.Data(Event.createFromDto(event)))
        }catch (exception: Exception){
            emit(DataState.Error(exception))
        }
    }.asCommonFlow()
}

