package com.colinjp.inblo.interactors.horse_detail

import com.colinjp.inblo.datasource.network.horse_detail.HorseDetailServiceImpl
import com.colinjp.inblo.domain.model.HorseTreatment
import com.colinjp.inblo.domain.util.CommonFlow
import com.colinjp.inblo.domain.util.asCommonFlow
import com.colinjp.inblo.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AddHorseTreatment(
    private val horseDetailServiceImpl: HorseDetailServiceImpl
) {
    fun invoke(
        horseId: String,
        date: String,
        vetName: String,
        treatmentDetail: String,
        injuredPart: String,
        occasionType: String,
        medicineName: String,
        memo: String,
        dailyAttachedIds: List<String>?,
        horseTreatmentId: String?
    ): CommonFlow<DataState<HorseTreatment>> = flow {
        emit(DataState.Loading(true))
        try {
            val horseTreatment = horseDetailServiceImpl.addHorseTreatmentRecord(
                horseId,
                date,
                vetName,
                treatmentDetail,
                injuredPart,
                occasionType,
                medicineName,
                memo,
                dailyAttachedIds,
                horseTreatmentId
            ).data
            emit(DataState.Data(HorseTreatment.createFromDto(horseTreatment)))
        }catch (exception: Exception){
            emit(DataState.Error(exception))
        }
    }.asCommonFlow()
}