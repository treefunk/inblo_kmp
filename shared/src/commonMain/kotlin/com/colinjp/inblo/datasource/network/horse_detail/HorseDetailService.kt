package com.colinjp.inblo.datasource.network.horse_detail

import com.colinjp.inblo.datasource.network.BooleanResponse
import com.colinjp.inblo.datasource.network.horse_detail.responses.*


interface HorseDetailService {

    suspend fun getHorseDailyReports(
        horseId: String
    ): GetHorseDailyListResponse

    suspend fun getHorseConditions(
        horseId: String
    ): GetHorseConditionListResponse

    suspend fun getHorseTrainingRecords(
        horseId: String
    ): GetHorseTrainingListResponse

    suspend fun getHorseTreatmentRecords(
        horseId: String,
    ): GetHorseTreatmentListResponse

    suspend fun getHorseStables(): DropDownDataResponse

    suspend fun getDailyReportForm(
        stableId: String
    ): GetDailyReportFormResponse

    suspend fun addDailyReport(
        horseId: String,
        date: String,
        bodyTemperature: String,
        horseWeight: String,
        conditionGroup: String,
        riderName: String, // ignored
        trainingType: String, // ignored
        riderId: String?,
        trainingTypeId: String?,
        trainingAmount: String,
        time5f: String,
        time4f: String,
        time3f: String,
        memo: String,
        dailyAttachedIds: List<String>?,
        dailyReportId: String?,
    ): GetHorseDailyResponse


    suspend fun addHorseCondition(
        horseId: String,
        date: String,
        weather: String,
        bodyTemperature: String,
        horseWeight: String,
        accidentSite: String,
        accidentPart: String,
        accidentType: String,
        memo: String,
        horseConditionId: String?
    ): GetHorseConditionResponse

    suspend fun removeHorseDaily(horseDailyId: String): BooleanResponse

    suspend fun addHorseTrainingRecord(
        horseId: String,
        date: String,
        weather: String,
        trainingType: String,
        trainingDetail: String,
        memo: String,
        time6F: String,
        time5F: String,
        time4F: String,
        time3F: String,
        time2F: String,
        time1F: String,
        horseTrainingId: String?
    ): GetHorseTrainingResponse

    suspend fun removeHorseTraining(
        horseTrainingId: String
    ): BooleanResponse

    suspend fun addHorseTreatmentRecord(
        horseId: String,
        date: String,
        vetName: String,
        treatmentDetail: String,
        injuredPart: String,
        occasionType: String,
        medicineName: String,
        memo: String,
        dailyAttachedIds: List<String>?,
        horseTreatmentId: String?
    ): GetHorseTreatmentResponse

    suspend fun removeHorseTreatment(
        horseTreatmentId: String
    ): BooleanResponse

    suspend fun getHorseTrainingTypes(): DropDownDataResponse

    suspend fun getHorseTrainingDetails(): DropDownDataResponse




}