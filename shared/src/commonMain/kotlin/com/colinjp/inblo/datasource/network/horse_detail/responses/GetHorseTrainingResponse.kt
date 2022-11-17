package com.colinjp.inblo.datasource.network.horse_detail.responses

import com.colinjp.inblo.datasource.network.MetaResponse
import com.colinjp.inblo.datasource.network.horse_detail.HorseTrainingRecordDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class GetHorseTrainingResponse(
    @SerialName("data")
    val data: HorseTrainingRecordDto,
    @SerialName("meta")
    val meta: MetaResponse
)