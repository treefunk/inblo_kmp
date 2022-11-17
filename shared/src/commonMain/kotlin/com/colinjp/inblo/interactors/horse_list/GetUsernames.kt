package com.colinjp.inblo.interactors.horse_list

import com.colinjp.inblo.datasource.network.horse_list.responses.GetUsersResponse
import com.colinjp.inblo.datasource.network.horse_list.UserService
import com.colinjp.inblo.domain.util.CommonFlow
import com.colinjp.inblo.domain.util.asCommonFlow
import com.colinjp.inblo.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetUsernames(
    private val userService: UserService
){
    fun invoke(
        stableId: String?,
        excludeId: String?
    ): CommonFlow<DataState<GetUsersResponse>> = flow {
        emit(DataState.Loading(true))
        try{
            val response = userService.getAllUsernames(
                stableId,excludeId
            )
            emit(DataState.Data(response))
        }catch (exception: Exception){
            emit(DataState.Error(exception))
        }
    }.asCommonFlow()
}