package com.colinjp.inblo.domain.model

import com.colinjp.inblo.datasource.network.horse_detail.HorseTrainingRecordDto
import com.colinjp.inblo.util.AndroidParcel
import com.colinjp.inblo.util.AndroidParcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@AndroidParcelize
class HorseTrainingRecord(
    val id: Int,
    val horseId: Int,
    val trainingTypeId: Int,
    val trainingDetailId: Int,
    val trainingType: String?,
    val trainingDetail: String?,
    val date: String,
    val weather: String,
    val time6F: Double,
    val time5F: Double,
    val time4F: Double,
    val time3F: Double,
    val time2F: Double,
    val time1F: Double,
    val memo: String,
    val updatedAt: String,
    val createdAt: String,
): AndroidParcel {
    companion object {
        fun createFromDto(
            horseTrainingRecordDto: HorseTrainingRecordDto
        ): HorseTrainingRecord {
            return HorseTrainingRecord(
                id = horseTrainingRecordDto.id,
                horseId = horseTrainingRecordDto.horseId,
                trainingTypeId = horseTrainingRecordDto.trainingTypeId,
                trainingDetailId = horseTrainingRecordDto.trainingDetailId,
                trainingType = horseTrainingRecordDto.trainingType,
                trainingDetail = horseTrainingRecordDto.trainingDetail,
                date = horseTrainingRecordDto.date,
                weather = horseTrainingRecordDto.weather,
                time6F = horseTrainingRecordDto.time6F,
                time5F = horseTrainingRecordDto.time5F,
                time4F = horseTrainingRecordDto.time4F,
                time3F = horseTrainingRecordDto.time3F,
                time2F = horseTrainingRecordDto.time2F,
                time1F = horseTrainingRecordDto.time1F,
                memo = horseTrainingRecordDto.memo,
                updatedAt = horseTrainingRecordDto.updatedAt,
                createdAt = horseTrainingRecordDto.createdAt
            )
        }
    }
}