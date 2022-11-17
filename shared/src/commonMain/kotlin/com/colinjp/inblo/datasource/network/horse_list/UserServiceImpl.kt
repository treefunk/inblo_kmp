package com.colinjp.inblo.datasource.network.horse_list

import com.colinjp.inblo.datasource.network.horse_list.responses.GetUsersResponse
import io.ktor.client.*
import io.ktor.client.request.*

class UserServiceImpl(
    private val httpClient: HttpClient,
    private val baseUrl: String
): UserService {
    override suspend fun getAllUsernames(stableId: String?,
                                         excludeId: String?): GetUsersResponse {
        return httpClient.get {

            var url = "$baseUrl/users?"
            var params = 0
            if(stableId != null){
                url += "stable_id=$stableId"
                params++
            }

            if(excludeId != null){
                if(params > 0){
                    url += "&"
                }
                url += "exclude_id=$excludeId"
                params++
            }

            url(url)
        }
    }
}