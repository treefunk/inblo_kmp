package com.colinjp.inblo.datasource.network.calendar

import com.colinjp.inblo.datasource.network.BooleanResponse
import com.colinjp.inblo.datasource.network.calendar.responses.AddEventResponse
import com.colinjp.inblo.datasource.network.calendar.responses.GetAllEventsResponse
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

class CalendarEventServiceImpl(
    private val httpClient: HttpClient,
    private val baseUrl: String,
): CalendarEventService {
    override suspend fun getAllEventsByMonthYear(
        stableId: Int,
        monthInt: String,
        year: String,
        horseId: Int
    ): GetAllEventsResponse {
        return if(horseId == 0){
            httpClient.get {
                url("$baseUrl/events/stable/${stableId}/month/${monthInt}/year/${year}")
            }
        }else{
            httpClient.get {
                url("$baseUrl/events/horse/${horseId}/month/${monthInt}/year/${year}")
            }
        }

    }

    override suspend fun addEvent(
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
    ): AddEventResponse {
        if(eventId == null) { // insert
            return httpClient.post {
                url("$baseUrl/events")
                parameter("user_id", userId)
                if(horseId != null){
                    parameter("horse_id",horseId)
                }
                parameter("event_type",eventType)
                parameter("title",title)
                parameter("date",date)
                if(dateEnd != null){
                    parameter("date_end",dateEnd)
                }
                parameter("start",start)
                parameter("end",end)
                parameter("memo",memo)
                parameter("stable_id",stableId)
            }
        } else { // update
            return httpClient.put {
                url("$baseUrl/events/$eventId")
                parameter("user_id", userId)
                parameter("event_type",eventType)
                if(horseId != null){
                    parameter("horse_id",horseId)
                }
                parameter("title",title)
                parameter("date",date)
                if(dateEnd != null){
                    parameter("date_end",dateEnd)
                }
                parameter("start",start)
                parameter("end",end)
                parameter("memo",memo)
                parameter("stable_id",stableId)
            }
        }
    }

    override suspend fun deleteEvent(eventId: Int): BooleanResponse {
        return httpClient.delete {
            url("$baseUrl/events/$eventId")
        }
    }
}