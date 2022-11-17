package com.colinjp.inblo.interactors.horse_detail

import com.colinjp.inblo.datasource.network.BooleanResponse
import com.colinjp.inblo.datasource.network.horse_detail.HorseDetailServiceImpl
import com.colinjp.inblo.domain.util.CommonFlow
import com.colinjp.inblo.domain.util.asCommonFlow
import com.colinjp.inblo.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DeleteHorseDailyReport(
    private val horseDetailService: HorseDetailServiceImpl
) {
    fun invoke(horseDailyId: String): CommonFlow<DataState<BooleanResponse>> = flow {
        emit(DataState.Loading(true))
        try{
            val response = horseDetailService.removeHorseDaily(horseDailyId)
            emit(DataState.Data(response))
        }catch (exception: Exception){
            emit(DataState.Error(exception))
        }
    }.asCommonFlow()
}