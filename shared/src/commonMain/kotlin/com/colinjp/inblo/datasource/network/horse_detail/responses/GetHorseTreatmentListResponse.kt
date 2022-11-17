package com.colinjp.inblo.datasource.network.horse_detail.responses

import com.colinjp.inblo.datasource.network.MetaResponse
import com.colinjp.inblo.datasource.network.horse_detail.HorseTreatmentDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class GetHorseTreatmentListResponse(
    @SerialName("data")
    val data: List<HorseTreatmentDto>,
    @SerialName("meta")
    val meta: MetaResponse
)