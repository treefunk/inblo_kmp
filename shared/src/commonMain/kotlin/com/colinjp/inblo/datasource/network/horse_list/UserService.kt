package com.colinjp.inblo.datasource.network.horse_list

import com.colinjp.inblo.datasource.network.horse_list.responses.GetUsersResponse

interface UserService {
    suspend fun getAllUsernames(stableId: String?,
                                excludeId: String?): GetUsersResponse
}