package com.colinjp.inblo.interactors.horse_list

import com.colinjp.inblo.datasource.network.horse_list.HorseService
import com.colinjp.inblo.domain.model.Horse
import com.colinjp.inblo.domain.util.CommonFlow
import com.colinjp.inblo.domain.util.asCommonFlow
import com.colinjp.inblo.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AddHorse(
    private val horseServiceImpl: HorseService
) {
    fun invoke(
        name: String,
        userId: Int,
        stableId: Int,
        ownerName: String,
        farmName: String,
        trainingFarmName: String,
        sex: String,
        color: String,
        _class: String,
        birthDate: String,
        father: String,
        mother: String,
        motherFatherName: String,
        totalStake: Double,
        notes: String,
        horseId: String?
    ): CommonFlow<DataState<Horse>> = flow {
        emit(DataState.Loading(true))
        try {
            val horseDto = horseServiceImpl.addHorse(
                name,
                userId,
                stableId,
                ownerName,
                farmName,
                trainingFarmName,
                sex,
                color,
                _class,
                birthDate,
                father,
                mother,
                motherFatherName,
                totalStake,
                notes,
                horseId?.toInt(),
            ).data
            emit(DataState.Data(Horse.createFromDto(horseDto)))
        }catch (exception: Exception){
            emit(DataState.Error(exception))
        }
    }.asCommonFlow()
}