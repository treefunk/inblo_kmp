package com.colinjp.inblo.datasource.network.message.responses

import com.colinjp.inblo.datasource.network.MetaResponse
import com.colinjp.inblo.datasource.network.message.MessageDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class GetMessageListResponse constructor(
    @SerialName("data")
    val data: List<MessageDto>,
    @SerialName("meta")
    val meta: MetaResponse
)