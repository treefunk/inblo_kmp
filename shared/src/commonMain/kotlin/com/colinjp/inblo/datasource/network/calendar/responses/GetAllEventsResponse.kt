package com.colinjp.inblo.datasource.network.calendar.responses

import com.colinjp.inblo.datasource.network.MetaResponse
import com.colinjp.inblo.datasource.network.calendar.EventDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetAllEventsResponse(
    @SerialName("data")
    val events: List<EventDTO>,
    @SerialName("meta")
    val meta: MetaResponse
)