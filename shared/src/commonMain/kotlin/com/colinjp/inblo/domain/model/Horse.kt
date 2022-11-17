package com.colinjp.inblo.domain.model

import com.colinjp.inblo.datasource.network.horse_detail.DropdownData
import com.colinjp.inblo.datasource.network.horse_list.HorseDto
import com.colinjp.inblo.datasource.network.login.UserDto
import com.colinjp.inblo.util.AndroidParcel
import com.colinjp.inblo.util.AndroidParcelize



@AndroidParcelize
class Horse (
    var id: Int? = null,

    var stableId: Int? = null,

    var farmId: Int? = null,

    var trainingFarmId: Int? = null,

    var user: User? = null,

    var stable: DropdownData? = null,

    var ownerName: String? = null,

    var farmName: String? = null,

    var trainingFarmName: String? = null,

    var birthDate: String? = null,

    var sex: String? = null,

    var age: Int? = null,

    var name: String? = null,

    var color: String? = null,

    var class_: String? = null,

    var fatherName: String? = null,

    var motherName: String? = null,

    var motherFatherName: String? = null,

    var totalStake: Double? = null,

    var notes: String? = null,

    var createdAt: String? = null,

    var updatedAt: String?
): AndroidParcel {

    companion object {
        fun createFromDto(
            horseDto: HorseDto?
        ): Horse {
            return Horse (
                id = horseDto?.id,
                stableId = horseDto?.stableId,
                farmId = horseDto?.farmId,
                trainingFarmId = horseDto?.trainingFarmId,
                user = User.createFromDto(horseDto?.user),
                stable = horseDto?.stable,
                ownerName = horseDto?.ownerName,
                farmName = horseDto?.farmName,
                trainingFarmName = horseDto?.trainingFarmName,
                birthDate = horseDto?.birthDate,
                sex = horseDto?.sex,
                age = horseDto?.age,
                name = horseDto?.name,
                color = horseDto?.color,
                class_ = horseDto?.class_,
                fatherName = horseDto?.fatherName,
                motherName = horseDto?.motherName,
                motherFatherName = horseDto?.motherFatherName,
                totalStake = horseDto?.totalStake,
                notes = horseDto?.notes,
                createdAt = horseDto?.createdAt,
                updatedAt = horseDto?.updatedAt
            )
        }
    }
}
