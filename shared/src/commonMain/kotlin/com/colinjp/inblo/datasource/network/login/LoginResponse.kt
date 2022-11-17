package com.colinjp.inblo.datasource.network.login

import com.colinjp.inblo.datasource.network.MetaResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    @SerialName("data")
    val data : UserDto,
    @SerialName("meta")
    val meta: MetaResponse
)