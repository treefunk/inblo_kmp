package com.colinjp.inblo.interactors.horse_list

import com.colinjp.inblo.datasource.network.BooleanResponse
import com.colinjp.inblo.datasource.network.horse_list.HorseService
import com.colinjp.inblo.domain.util.CommonFlow
import com.colinjp.inblo.domain.util.asCommonFlow
import com.colinjp.inblo.util.DataState
import kotlinx.coroutines.flow.flow

class RestoreArchivedHorse (
    private val horseService: HorseService
){
    fun invoke(
        horseId: String
    ): CommonFlow<DataState<BooleanResponse>> = flow {
        emit(DataState.Loading(true))
        try {
                val response = horseService.restoreArchivedHorse(horseId)
                emit(DataState.Data(response))
        }catch (exception: Exception){
                emit(DataState.Error(exception))
        }
    }.asCommonFlow()
}