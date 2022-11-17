package com.colinjp.inblo.datasource.network

import com.colinjp.inblo.datasource.network.MetaResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class BooleanResponse (
    @SerialName("data")
    val data: Boolean,
    @SerialName("meta")
    val meta: MetaResponse
)