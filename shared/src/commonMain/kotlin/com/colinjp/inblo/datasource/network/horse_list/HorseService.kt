package com.colinjp.inblo.datasource.network.horse_list

import com.colinjp.inblo.datasource.network.BooleanResponse
import com.colinjp.inblo.datasource.network.horse_list.responses.GetHorseListResponse
import com.colinjp.inblo.datasource.network.horse_list.responses.GetHorseResponse

interface HorseService {
    suspend fun getHorses(
        offset: Int?,
        query: String?,
        isArchived: Boolean,
        stableId: Int,
    ): GetHorseListResponse

    suspend fun addHorse(
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
        horseId: Int?
    ): GetHorseResponse

    suspend fun archiveHorse(
        horseId: String
    ): BooleanResponse

    suspend fun restoreArchivedHorse(
        horseId: String
    ): BooleanResponse


}