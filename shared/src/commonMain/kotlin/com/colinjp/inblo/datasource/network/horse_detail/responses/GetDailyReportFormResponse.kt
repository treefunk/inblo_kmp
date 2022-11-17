package com.colinjp.inblo.datasource.network.horse_detail.responses

import com.colinjp.inblo.datasource.network.MetaResponse
import com.colinjp.inblo.datasource.network.horse_detail.DropdownData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class GetDailyReportFormResponse(
    @SerialName("data")
    val data: DailyReportFormData,
    @SerialName("meta")
    val response: MetaResponse
)

@Serializable
class DailyReportFormData(
    @SerialName("training_types")
    val trainingTypes: List<DropdownData>,
    @SerialName("riders")
    val riders: List<DropdownData>
)