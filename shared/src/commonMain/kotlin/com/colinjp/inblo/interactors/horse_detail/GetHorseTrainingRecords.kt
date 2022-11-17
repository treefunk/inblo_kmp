package com.colinjp.inblo.interactors.horse_detail

import com.colinjp.inblo.datasource.network.horse_detail.HorseDetailServiceImpl
import com.colinjp.inblo.domain.model.HorseTrainingRecord
import com.colinjp.inblo.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetHorseTrainingRecords(
    private val horseDetailServiceImpl: HorseDetailServiceImpl
) {
    fun invoke(
        horseId: String
    ): Flow<DataState<List<HorseTrainingRecord>>> = flow {
        emit(DataState.Loading(true))
        try {
            val horseConditions = horseDetailServiceImpl.getHorseTrainingRecords(
                horseId
            ).data
            emit(DataState.Data(horseConditions.map { HorseTrainingRecord.createFromDto(it) }))
        }catch (exception: Exception){
            emit(DataState.Error(exception))
        }
    }
}