package com.colinjp.inblo.datasource.network.horse_list.responses

import com.colinjp.inblo.datasource.network.MetaResponse
import com.colinjp.inblo.datasource.network.login.UserDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class GetUsersResponse(
    @SerialName("data")
    val data: List<UserDto>,
    @SerialName("meta")
    val meta: MetaResponse
)