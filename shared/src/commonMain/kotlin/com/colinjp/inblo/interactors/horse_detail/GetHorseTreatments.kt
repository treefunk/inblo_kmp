package com.colinjp.inblo.interactors.horse_detail

import com.colinjp.inblo.datasource.network.horse_detail.HorseDetailServiceImpl
import com.colinjp.inblo.domain.model.HorseTreatment
import com.colinjp.inblo.domain.util.CommonFlow
import com.colinjp.inblo.domain.util.asCommonFlow
import com.colinjp.inblo.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetHorseTreatments(
    private val horseDetailServiceImpl: HorseDetailServiceImpl
) {
    fun invoke(
        horseId: String
    ): CommonFlow<DataState<List<HorseTreatment>>> = flow {
        emit(DataState.Loading(true))
        try {
            val horseConditions = horseDetailServiceImpl.getHorseTreatmentRecords(
                horseId
            ).data
            emit(DataState.Data(horseConditions.map { HorseTreatment.createFromDto(it) }))
        }catch (exception: Exception){
            emit(DataState.Error(exception))
        }
    }.asCommonFlow()
}