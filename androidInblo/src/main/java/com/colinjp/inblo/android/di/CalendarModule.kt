package com.colinjp.inblo.android.di

import com.colinjp.inblo.datasource.network.calendar.CalendarEventService
import com.colinjp.inblo.datasource.network.calendar.CalendarEventServiceImpl
import com.colinjp.inblo.interactors.Calendar.AddEventUseCase
import com.colinjp.inblo.interactors.Calendar.DeleteEventUseCase
import com.colinjp.inblo.interactors.Calendar.GetAllEventsByMonthYear
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import io.ktor.client.*

@Module
@InstallIn(ActivityRetainedComponent::class)
class CalendarModule {


    @ActivityRetainedScoped
    @Provides
    fun provideCalendarService(
        httpClient: HttpClient,
        @BaseURL baseURL: String
    ): CalendarEventService {
        return CalendarEventServiceImpl(
            httpClient,baseURL
        )
    }

    @ActivityRetainedScoped
    @Provides
    fun provideGetAllEvents(
        calendarService: CalendarEventService
    ): GetAllEventsByMonthYear {
        return GetAllEventsByMonthYear(calendarService)
    }

    @ActivityRetainedScoped
    @Provides
    fun provideAddEvent(
        calendarService: CalendarEventService
    ): AddEventUseCase {
        return AddEventUseCase(calendarService)
    }

    @ActivityRetainedScoped
    @Provides
    fun provideDeleteEvent(
        calendarService: CalendarEventService
    ): DeleteEventUseCase {
        return DeleteEventUseCase(calendarService)
    }


}