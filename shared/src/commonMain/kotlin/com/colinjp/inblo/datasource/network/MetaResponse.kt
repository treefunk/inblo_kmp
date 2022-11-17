package com.colinjp.inblo.datasource.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MetaResponse(
    @SerialName("message")
    var message: String,
    @SerialName("code")
    var code: Int
)