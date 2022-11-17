package com.colinjp.inblo.interactors.Calendar

import com.colinjp.inblo.datasource.network.calendar.CalendarEventService
import com.colinjp.inblo.domain.model.Event
import com.colinjp.inblo.domain.util.CommonFlow
import com.colinjp.inblo.domain.util.asCommonFlow
import com.colinjp.inblo.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class GetAllEventsByMonthYear constructor(
    private val calendarEventService: CalendarEventService
){
    fun invoke(
        stableId: Int,
        month: String,
        year: String,
        horseId: Int
    ): CommonFlow<DataState<List<Event>>> = flow {
        emit(DataState.Loading(true))
        try{
            val response = calendarEventService.getAllEventsByMonthYear(stableId,month,year,horseId)
            emit(DataState.Data(response.events.map { Event.createFromDto(it) }))
        }catch (exception: Exception){
            emit(DataState.Error(exception))
        }
    }.asCommonFlow()
}