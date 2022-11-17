package com.colinjp.inblo.interactors.horse_detail

import com.colinjp.inblo.datasource.network.horse_detail.HorseDetailServiceImpl
import com.colinjp.inblo.datasource.network.horse_detail.responses.DropDownDataResponse
import com.colinjp.inblo.domain.util.CommonFlow
import com.colinjp.inblo.domain.util.asCommonFlow
import com.colinjp.inblo.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetHorseStables(
    private val horseDetailServiceImpl: HorseDetailServiceImpl
){
    fun invoke(): CommonFlow<DataState<DropDownDataResponse>> = flow {
        emit(DataState.Loading(true))
        try{
            val response = horseDetailServiceImpl.getHorseStables()
            emit(DataState.Data(response))
        }catch (exception: Exception){
            emit(DataState.Error(exception))
        }
    }.asCommonFlow()
}