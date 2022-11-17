package com.colinjp.inblo.datasource.network.message.responses

import com.colinjp.inblo.datasource.network.MetaResponse
import com.colinjp.inblo.datasource.network.message.MessageDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SingleMessageResponse constructor(
    @SerialName("data")
    val data: MessageDto,
    @SerialName("meta")
    val meta: MetaResponse
)