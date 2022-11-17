package com.colinjp.inblo.datasource.network.horse_detail.responses

import com.colinjp.inblo.datasource.network.MetaResponse
import com.colinjp.inblo.datasource.network.horse_detail.HorseDailyDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class GetHorseDailyListResponse(
    @SerialName("data")
    val data: List<HorseDailyDto>,
    @SerialName("meta")
    val meta: MetaResponse,
    @SerialName("hidden_columns")
    val hiddenColumns: String
)