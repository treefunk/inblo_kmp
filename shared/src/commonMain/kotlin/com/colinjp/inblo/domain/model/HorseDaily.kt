package com.colinjp.inblo.domain.model

import com.colinjp.inblo.datasource.network.horse_detail.AttachedFileDto
import com.colinjp.inblo.datasource.network.horse_detail.DropdownData
import com.colinjp.inblo.datasource.network.horse_detail.HorseDailyDto
import com.colinjp.inblo.util.AndroidParcel
import com.colinjp.inblo.util.AndroidParcelize

@AndroidParcelize
class HorseDaily (
    val id: Int,
    val horseId: Int,
    val date: String,
    val bodyTemperature: Double,
    val horseWeight: Double,
    val conditionGroup: String,
    val riderName: String,
    val trainingTypeName: String,
    val rider: DropdownData? = null,
    val trainingType: DropdownData? = null,
    val trainingAmount: String,
    val time5f: Double,
    val time4f: Double,
    val time3f: Double,
    val attachedFiles: List<AttachedFileDto>? = null,
    val memo: String,
    val updatedAt: String,
    val createdAt: String
): AndroidParcel {
    companion object {
        fun createFromDto(horseDailyDto: HorseDailyDto): HorseDaily {
            return HorseDaily(
                id = horseDailyDto.id,
                horseId = horseDailyDto.horseId,
                date = horseDailyDto.date,
                bodyTemperature = horseDailyDto.bodyTemperature,
                horseWeight = horseDailyDto.horseWeight,
                conditionGroup = horseDailyDto.conditionGroup,
                riderName = horseDailyDto.riderName,
                trainingTypeName = horseDailyDto.trainingTypeName,
                rider = horseDailyDto.rider,
                trainingType = horseDailyDto.trainingType,
                trainingAmount = horseDailyDto.trainingAmount,
                time5f = horseDailyDto.time5f,
                time4f = horseDailyDto.time4f,
                time3f = horseDailyDto.time3f,
                attachedFiles = horseDailyDto.attachedFiles,
                memo = horseDailyDto.memo,
                updatedAt = horseDailyDto.updatedAt,
                createdAt = horseDailyDto.createdAt
            )
        }
    }
}