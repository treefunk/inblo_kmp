package com.colinjp.inblo.datasource.network.horse_list.responses

import com.colinjp.inblo.datasource.network.MetaResponse
import com.colinjp.inblo.datasource.network.horse_list.HorseDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetHorseResponse(
    @SerialName("data")
    val data: HorseDto,
    @SerialName("meta")
    val meta: MetaResponse
)