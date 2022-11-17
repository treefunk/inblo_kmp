package com.colinjp.inblo.datasource.network.horse_detail.responses

import com.colinjp.inblo.datasource.network.MetaResponse
import com.colinjp.inblo.datasource.network.horse_detail.AttachedFileDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class GetDailyAttachedFileResponse (
    @SerialName("data")
    val data: AttachedFileDto,
    @SerialName("meta")
    val meta: MetaResponse
)