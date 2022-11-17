package com.colinjp.inblo.datasource.network.calendar

import com.colinjp.inblo.datasource.network.horse_list.HorseDto
import com.colinjp.inblo.datasource.network.login.UserDto
import com.colinjp.inblo.domain.model.Horse
import com.colinjp.inblo.domain.model.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EventDTO(

    @SerialName("id")
    val id: Int,

    @SerialName("horse_id")
    val horseId: Int?,

    @SerialName("user_id")
    val userId: Int?,

    @SerialName("date")
    val date: String,

    @SerialName("date_end")
    val dateEnd: String? = null,

    @SerialName("title")
    val title: String,

    @SerialName("event_type")
    val eventType: String,

    @SerialName("start")
    val start: String,

    @SerialName("end")
    val end: String,

    @SerialName("memo")
    val notes: String?,

    @SerialName("user")
    val user: UserDto? = null,

    @SerialName("horse")
    val horse: HorseDto? = null,

    @SerialName("created_at")
    val createdAt: String? = null,

    @SerialName("updated_at")
    val updatedAt: String? = null
)