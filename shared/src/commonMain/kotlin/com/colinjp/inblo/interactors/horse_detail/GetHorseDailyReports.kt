package com.colinjp.inblo.interactors.horse_detail

import com.colinjp.inblo.datasource.network.horse_detail.HorseDetailServiceImpl
import com.colinjp.inblo.datasource.network.horse_detail.responses.GetHorseDailyListResponse
import com.colinjp.inblo.domain.model.HorseDaily
import com.colinjp.inblo.domain.util.CommonFlow
import com.colinjp.inblo.domain.util.asCommonFlow
import com.colinjp.inblo.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetHorseDailyReports(
    private val horseDetailServiceImpl: HorseDetailServiceImpl
) {
//    fun invoke(
//        horseId: String
//    ): CommonFlow<DataState<List<HorseDaily>>> = flow {
//        emit(DataState.Loading(true))
//        try {
//            val horseConditions = horseDetailServiceImpl.getHorseDailyReports(
//                horseId
//            ).data
//            emit(DataState.Data(horseConditions.map { HorseDaily.createFromDto(it) }))
//        }catch (exception: Exception){
//            emit(DataState.Error(exception))
//        }
//    }.asCommonFlow()
        fun invoke(
        horseId: String
    ): CommonFlow<DataState<GetHorseDailyListResponse>> = flow {
        emit(DataState.Loading(true))
        try {
            val horseDailyReports = horseDetailServiceImpl.getHorseDailyReports(
                horseId
            )
//            emit(DataState.Data(horseConditions.map { HorseDaily.createFromDto(it) }))
            emit(DataState.Data(horseDailyReports))
        }catch (exception: Exception){
            emit(DataState.Error(exception))
        }
    }.asCommonFlow()
}