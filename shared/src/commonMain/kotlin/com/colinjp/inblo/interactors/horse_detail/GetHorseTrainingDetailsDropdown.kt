package com.colinjp.inblo.interactors.horse_detail

import com.colinjp.inblo.datasource.network.horse_detail.DropdownData
import com.colinjp.inblo.datasource.network.horse_detail.HorseDetailServiceImpl
import com.colinjp.inblo.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetHorseTrainingDetailsDropdown(
    private val horseDetailServiceImpl: HorseDetailServiceImpl
) {
    fun invoke(): Flow<DataState<List<DropdownData>>> = flow {
        emit(DataState.Loading(true))
        try {
            val trainingDetails = horseDetailServiceImpl.getHorseTrainingDetails().data
            emit(DataState.Data(trainingDetails))
        }catch (exception: Exception){
            emit(DataState.Error(exception))
        }
    }
}