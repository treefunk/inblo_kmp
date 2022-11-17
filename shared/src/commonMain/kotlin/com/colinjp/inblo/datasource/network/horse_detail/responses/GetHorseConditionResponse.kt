package com.colinjp.inblo.datasource.network.horse_detail.responses

import com.colinjp.inblo.datasource.network.MetaResponse
import com.colinjp.inblo.datasource.network.horse_detail.HorseDailyDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class GetHorseConditionResponse(
    @SerialName("data")
    val data: HorseDailyDto,
    @SerialName("meta")
    val meta: MetaResponse
)