package com.colinjp.inblo.datasource.network.horse_detail

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class HorseTrainingRecordDto(
    @SerialName("id")
    val id: Int,
    @SerialName("horse_id")
    val horseId: Int,
    @SerialName("training_type_id")
    val trainingTypeId: Int,
    @SerialName("training_detail_id")
    val trainingDetailId: Int,
    @SerialName("training_type")
    val trainingType: String,
    @SerialName("training_detail")
    val trainingDetail: String?,
    @SerialName("date")
    val date: String,
    @SerialName("weather")
    val weather: String,
    @SerialName("6f_time")
    val time6F: Double,
    @SerialName("5f_time")
    val time5F: Double,
    @SerialName("4f_time")
    val time4F: Double,
    @SerialName("3f_time")
    val time3F: Double,
    @SerialName("2f_time")
    val time2F: Double,
    @SerialName("1f_time")
    val time1F: Double,
    @SerialName("memo")
    val memo: String,
    @SerialName("updated_at")
    val updatedAt: String,
    @SerialName("created_at")
    val createdAt: String,
)