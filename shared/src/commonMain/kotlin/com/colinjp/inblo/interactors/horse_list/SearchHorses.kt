package com.colinjp.inblo.interactors.horse_list

import com.colinjp.inblo.domain.model.Horse
import com.colinjp.inblo.datasource.network.horse_list.HorseService
import com.colinjp.inblo.domain.util.CommonFlow
import com.colinjp.inblo.domain.util.asCommonFlow
import com.colinjp.inblo.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchHorses(
    private val horseService: HorseService
){
    fun invoke(
        offset: Int? = null,
        query: String? = "",
        isArchived: Boolean = false,
        stableId: Int
    ): CommonFlow<DataState<List<Horse>>> = flow {
        emit(DataState.Loading(true))
        try {
            val horsesDTO = horseService.getHorses(
                offset,
                query,
                isArchived,
                stableId
            ).data
            emit(DataState.Data(horsesDTO.map { Horse.createFromDto(it) }))
        }catch (exception: Exception){
            emit(DataState.Error(exception))
        }
//        emit(DataState.Loading(false))
    }.asCommonFlow()
}