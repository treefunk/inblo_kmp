package com.colinjp.inblo.datasource.network.horse_detail.responses

import com.colinjp.inblo.datasource.network.MetaResponse
import com.colinjp.inblo.datasource.network.horse_detail.HorseTrainingRecordDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class GetHorseTrainingListResponse(
    @SerialName("data")
    val data: List<HorseTrainingRecordDto>,
    @SerialName("meta")
    val meta: MetaResponse
)