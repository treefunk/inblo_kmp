package com.colinjp.inblo.domain.model

import com.colinjp.inblo.datasource.network.horse_detail.AttachedFileDto
import com.colinjp.inblo.datasource.network.horse_detail.HorseTreatmentDto
import com.colinjp.inblo.util.AndroidParcel
import com.colinjp.inblo.util.AndroidParcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@AndroidParcelize
class HorseTreatment (
    val id: Int,
    val date: String,
    val doctorName: String,
    val treatmentDetail: String,
    val injuredPart: String,
    val occasionType: String,
    val medicineName: String,
    val memo: String,
    val horseId: Int,
    val attachedFiles: List<AttachedFileDto>? = null,
    val updatedAt: String,
    val createdAt: String,
): AndroidParcel {
    companion object {
        fun createFromDto(
            horseTreatmentDto: HorseTreatmentDto
        ): HorseTreatment {
            return HorseTreatment(
                id = horseTreatmentDto.id,
                horseId = horseTreatmentDto.horseId,
                date = horseTreatmentDto.date,
                doctorName = horseTreatmentDto.doctorName,
                treatmentDetail = horseTreatmentDto.treatmentDetail,
                injuredPart = horseTreatmentDto.injuredPart,
                occasionType = horseTreatmentDto.occasionType,
                medicineName = horseTreatmentDto.medicineName,
                memo = horseTreatmentDto.memo,
                attachedFiles = horseTreatmentDto.attachedFiles,
                updatedAt = horseTreatmentDto.updatedAt,
                createdAt = horseTreatmentDto.createdAt
            )
        }
    }
}