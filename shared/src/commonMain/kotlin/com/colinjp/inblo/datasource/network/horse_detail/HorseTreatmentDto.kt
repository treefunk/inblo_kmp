package com.colinjp.inblo.datasource.network.horse_detail

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class HorseTreatmentDto (
    @SerialName("id")
    val id: Int,
    @SerialName("date")
    val date: String,
    @SerialName("doctor_name")
    val doctorName: String,
    @SerialName("treatment_detail")
    val treatmentDetail: String,
    @SerialName("injured_part")
    val injuredPart: String,
    @SerialName("occasion_type")
    val occasionType: String,
    @SerialName("medicine_name")
    val medicineName: String,
    @SerialName("memo")
    val memo: String,
    @SerialName("horse_id")
    val horseId: Int,
    @SerialName("attached_files")
    val attachedFiles: List<AttachedFileDto>? = null,
    @SerialName("updated_at")
    val updatedAt: String,
    @SerialName("created_at")
    val createdAt: String,
)