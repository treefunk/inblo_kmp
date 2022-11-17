package com.colinjp.inblo.datasource.network.horse_list

import com.colinjp.inblo.datasource.network.BooleanResponse
import com.colinjp.inblo.datasource.network.horse_list.responses.GetHorseListResponse
import com.colinjp.inblo.datasource.network.horse_list.responses.GetHorseResponse
import io.ktor.client.*
import io.ktor.client.request.*

class HorseServiceImpl(
    private val httpClient: HttpClient,
    private val baseUrl: String
): HorseService {

    override suspend fun getHorses(offset: Int?, query: String?,isArchived: Boolean,stableId: Int): GetHorseListResponse {
        return httpClient.get {
            var url = "$baseUrl/horses/stable/${stableId}"
            if(isArchived){
                url += "?archived=1"
            }
            url(url)
        }
    }

    override suspend fun addHorse(
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
    ): GetHorseResponse {
        return if(horseId == null){
            httpClient.post {
                url("$baseUrl/horses")
                parameter("name",name)
                parameter("sex",sex)
                parameter("color",color)
                parameter("class",_class)
                if(birthDate.isNotBlank()){
                    parameter("birth_date",birthDate)
                }
                parameter("father_name",father)
                parameter("mother_name",mother)
                parameter("mother_father_name",motherFatherName)
                parameter("total_stake",totalStake)
                if(userId > 0){
                    parameter("user_id", userId)
                }
                parameter("stable_id",stableId)
                parameter("owner_name", ownerName)
                parameter("farm_name", farmName)
                parameter("training_farm_name",trainingFarmName)
                parameter("memo",notes)
            }
        }else{
            httpClient.put {
                url("$baseUrl/horses/$horseId")
                parameter("name",name)
                parameter("sex",sex)
                parameter("color",color)
                parameter("class",_class)
                if(birthDate.isNotBlank()){
                    parameter("birth_date",birthDate)
                }
                parameter("father_name",father)
                parameter("mother_name",mother)
                parameter("mother_father_name",motherFatherName)
                parameter("total_stake",totalStake)
                if(userId > 0){
                    parameter("user_id", userId)
                }
                parameter("stable_id",stableId)
                parameter("owner_name", ownerName)
                parameter("farm_name", farmName)
                parameter("training_farm_name",trainingFarmName)
                parameter("memo",notes)

            }
        }
    }

    override suspend fun archiveHorse(horseId: String): BooleanResponse {
        return httpClient.get {
            url("$baseUrl/horses/archive/${horseId}")
        }
    }

    override suspend fun restoreArchivedHorse(horseId: String): BooleanResponse {
        return httpClient.get {
            url("$baseUrl/horses/restore/${horseId}")
        }
    }


}