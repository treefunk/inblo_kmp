package com.colinjp.inblo.interactors.Calendar

import com.colinjp.inblo.datasource.network.BooleanResponse
import com.colinjp.inblo.datasource.network.calendar.CalendarEventService
import com.colinjp.inblo.domain.model.Event
import com.colinjp.inblo.domain.util.CommonFlow
import com.colinjp.inblo.domain.util.asCommonFlow
import com.colinjp.inblo.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DeleteEventUseCase (
    private val calendarEventService: CalendarEventService
) {
    fun invoke(
        eventId: Int
    ): CommonFlow<DataState<BooleanResponse>> = flow {
        emit(DataState.Loading(true))
        try {
            val data = calendarEventService.deleteEvent(
                eventId
            )
            emit(DataState.Data(data))
        }catch (exception: Exception){
            emit(DataState.Error(exception))
        }
    }.asCommonFlow()
}

