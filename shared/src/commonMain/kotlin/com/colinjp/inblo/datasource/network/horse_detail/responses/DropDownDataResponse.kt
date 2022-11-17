package com.colinjp.inblo.datasource.network.horse_detail.responses

import com.colinjp.inblo.datasource.network.MetaResponse
import com.colinjp.inblo.datasource.network.horse_detail.DropdownData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DropDownDataResponse(
    @SerialName("data")
    val data: List<DropdownData>,
    @SerialName("meta")
    val meta: MetaResponse
)