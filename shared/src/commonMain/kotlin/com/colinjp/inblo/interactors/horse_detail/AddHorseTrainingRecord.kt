package com.colinjp.inblo.interactors.horse_detail

import com.colinjp.inblo.datasource.network.horse_detail.HorseDetailServiceImpl
import com.colinjp.inblo.domain.model.HorseTrainingRecord
import com.colinjp.inblo.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class AddHorseTrainingRecord(
    private val horseDetailServiceImpl: HorseDetailServiceImpl
) {
    fun invoke(
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
    ): Flow<DataState<HorseTrainingRecord>> = flow {
        emit(DataState.Loading(true))
        try {
            val horseTrainingRecord = horseDetailServiceImpl.addHorseTrainingRecord(
                horseId, date, weather, trainingType, trainingDetail, memo, time6F, time5F, time4F, time3F, time2F, time1F,horseTrainingId
            ).data
            emit(DataState.Data(HorseTrainingRecord.createFromDto(horseTrainingRecord)))
        }catch (exception: Exception){
            emit(DataState.Error(exception))
        }
    }
}