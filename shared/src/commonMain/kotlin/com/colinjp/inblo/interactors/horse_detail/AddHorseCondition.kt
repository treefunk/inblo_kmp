package com.colinjp.inblo.interactors.horse_detail

import com.colinjp.inblo.datasource.network.horse_detail.HorseDetailServiceImpl
import com.colinjp.inblo.domain.model.HorseDaily
import com.colinjp.inblo.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AddHorseCondition(
    private val horseDetailServiceImpl: HorseDetailServiceImpl
) {
    fun invoke(
        horseId: String,
        date: String,
        weather: String,
        bodyTemperature: String,
        horseWeight: String,
        accidentSite: String,
        accidentPart: String,
        accidentType: String,
        memo: String,
        horseConditionId: String?
    ): Flow<DataState<HorseDaily>> = flow {
        emit(DataState.Loading(true))
        try {
            val horseCondition = horseDetailServiceImpl.addHorseCondition(
                horseId, date, weather, bodyTemperature, horseWeight, accidentSite, accidentPart, accidentType, memo, horseConditionId
            ).data
            emit(DataState.Data(HorseDaily.createFromDto(horseCondition)))
        }catch (exception: Exception){
            emit(DataState.Error(exception))
        }
    }
}