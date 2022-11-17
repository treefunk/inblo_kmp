package com.colinjp.inblo.interactors.horse_detail

import com.colinjp.inblo.datasource.network.BooleanResponse
import com.colinjp.inblo.datasource.network.horse_detail.HorseDetailServiceImpl
import com.colinjp.inblo.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DeleteHorseTraining(
    private val horseDetailService: HorseDetailServiceImpl
) {
    suspend fun invoke(horseTrainingId: String): Flow<DataState<BooleanResponse>> = flow {
        emit(DataState.Loading(true))
        try{
            val response = horseDetailService.removeHorseTraining(horseTrainingId)
            emit(DataState.Data(response))
        }catch (exception: Exception){
            emit(DataState.Error(exception))
        }
    }
}