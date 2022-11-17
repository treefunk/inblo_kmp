package com.colinjp.inblo.interactors.horse_detail

import com.colinjp.inblo.datasource.network.horse_detail.HorseDetailServiceImpl
import com.colinjp.inblo.domain.model.HorseDaily
import com.colinjp.inblo.domain.util.CommonFlow
import com.colinjp.inblo.domain.util.asCommonFlow
import com.colinjp.inblo.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AddHorseDailyReport(
    private val horseDetailServiceImpl: HorseDetailServiceImpl
) {
    fun invoke(
        horseId: String,
        date: String,
        bodyTemperature: String,
        horseWeight: String,
        conditionGroup: String,
        riderName: String,
        trainingType: String,
        riderId: String?,
        trainingTypeId: String?,
        trainingAmount: String,
        time5f: String,
        time4f: String,
        time3f: String,
        memo: String,
        dailyAttachedIds: List<String>?,
        dailyReportId: String?
    ): CommonFlow<DataState<HorseDaily>> = flow {
        emit(DataState.Loading(true))
//        val riderId = if(riderId == "0" || riderId == null) null else riderId
//        val trainingTypeId = if(trainingTypeId == "0" || trainingTypeId == null) null else trainingTypeId

        try {
            val horseCondition = horseDetailServiceImpl.addDailyReport(
                horseId, date, bodyTemperature, horseWeight, conditionGroup, riderName, trainingType, riderId, trainingTypeId, trainingAmount, time5f, time4f, time3f, memo, dailyAttachedIds,dailyReportId
            ).data
            emit(DataState.Data(HorseDaily.createFromDto(horseCondition)))
        }catch (exception: Exception){
            emit(DataState.Error(exception))
        }
    }.asCommonFlow()
}