package com.colinjp.inblo.interactors.horse_detail

import com.colinjp.inblo.datasource.network.horse_detail.HorseDetailServiceImpl
import com.colinjp.inblo.datasource.network.horse_detail.responses.GetDailyReportFormResponse
import com.colinjp.inblo.domain.util.CommonFlow
import com.colinjp.inblo.domain.util.asCommonFlow
import com.colinjp.inblo.util.DataState
import kotlinx.coroutines.flow.flow

class GetDailyReportForm(
    private val horseDetailServiceImpl: HorseDetailServiceImpl
){
    fun invoke(
        stableId: String
    ): CommonFlow<DataState<GetDailyReportFormResponse>> = flow {
        emit(DataState.Loading(true))
        try{
            val getDailyReportFormResponse = horseDetailServiceImpl.getDailyReportForm(stableId)
            emit(DataState.Data(getDailyReportFormResponse))
        }catch (exception: Exception){
            emit(DataState.Error(exception))
        }
    }.asCommonFlow()
}