package com.colinjp.inblo.datasource.network.horse_list

import com.colinjp.inblo.datasource.network.horse_detail.DropdownData
import com.colinjp.inblo.datasource.network.login.UserDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class HorseDto (
    @SerialName("id")
    val id: Int,

    @SerialName("stable_id")
    val stableId: Int?,

    @SerialName("farm_id")
    val farmId: Int?,

    @SerialName("training_farm_id")
    val trainingFarmId: Int?,

    @SerialName("user")
    val user: UserDto?,

    @SerialName("stable")
    val stable: DropdownData?,

    @SerialName("stable_name")
    val stableName: String?,

    @SerialName("owner_name")
    val ownerName: String?,

    @SerialName("farm_name")
    val farmName: String?,

    @SerialName("training_farm_name")
    val trainingFarmName: String?,

    @SerialName("birth_date")
    val birthDate: String?,

    @SerialName("sex")
    val sex: String,

    @SerialName("age")
    val age: Int,

    @SerialName("name")
    val name: String,

    @SerialName("color")
    val color: String,

    @SerialName("class")
    val class_: String,

    @SerialName("father_name")
    val fatherName: String,

    @SerialName("mother_name")
    val motherName: String,

    @SerialName("mother_father_name")
    val motherFatherName: String,

    @SerialName("total_stake")
    val totalStake: Double,

    @SerialName("memo")
    val notes: String?,

    @SerialName("created_at")
    val createdAt: String?,

    @SerialName("updated_at")
    val updatedAt: String?
)

