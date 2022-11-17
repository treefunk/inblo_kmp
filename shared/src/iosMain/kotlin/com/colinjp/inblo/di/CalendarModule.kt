package com.colinjp.inblo.di

import com.colinjp.inblo.interactors.Calendar.AddEventUseCase
import com.colinjp.inblo.interactors.Calendar.DeleteEventUseCase
import com.colinjp.inblo.interactors.Calendar.GetAllEventsByMonthYear

class CalendarModule(
    val networkModule: NetworkModule
) {
    val getAllEventsByMonthYear: GetAllEventsByMonthYear by lazy {
        GetAllEventsByMonthYear(networkModule.calendarService)
    }

    val addEvent: AddEventUseCase by lazy {
        AddEventUseCase(networkModule.calendarService)
    }

    val deleteEvent: DeleteEventUseCase by lazy {
        DeleteEventUseCase(networkModule.calendarService)
    }


}