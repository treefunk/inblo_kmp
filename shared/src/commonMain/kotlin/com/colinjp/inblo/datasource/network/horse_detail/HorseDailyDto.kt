package com.colinjp.inblo.datasource.network.horse_detail

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class HorseDailyDto (
    @SerialName("id")
    val id: Int,
    @SerialName("horse_id")
    val horseId: Int,
    @SerialName("date")
    val date: String,
    @SerialName("body_temperature")
    val bodyTemperature: Double,
    @SerialName("horse_weight")
    val horseWeight: Double,
    @SerialName("condition_group")
    val conditionGroup: String,
    @SerialName("rider_name")
    val riderName: String,
    @SerialName("training_type_name")
    val trainingTypeName: String,
    @SerialName("rider")
    val rider: DropdownData? = null,
    @SerialName("training_type")
    val trainingType: DropdownData? = null,
    @SerialName("training_amount")
    val trainingAmount: String,
    @SerialName("5f_time")
    val time5f: Double,
    @SerialName("4f_time")
    val time4f: Double,
    @SerialName("3f_time")
    val time3f: Double,
    @SerialName("attached_files")
    val attachedFiles: List<AttachedFileDto>? = null,
    @SerialName("memo")
    val memo: String,
    @SerialName("updated_at")
    val updatedAt: String,
    @SerialName("created_at")
    val createdAt: String
)