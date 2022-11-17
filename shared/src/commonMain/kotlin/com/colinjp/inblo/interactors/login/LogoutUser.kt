package com.colinjp.inblo.interactors.login

import com.colinjp.inblo.datasource.network.login.LoginResponse
import com.colinjp.inblo.datasource.network.login.LoginService
import com.colinjp.inblo.domain.util.CommonFlow
import com.colinjp.inblo.domain.util.asCommonFlow
import com.colinjp.inblo.util.DataState
import kotlinx.coroutines.flow.flow

class LogoutUser(
    private val loginService: LoginService
) {
    fun invoke(
        userId: Int,
    ): CommonFlow<DataState<LoginResponse>> {
        return flow {
            emit(DataState.Loading(true))
            try {
                val response = loginService.logout(
                   userId
                )
                emit(DataState.Data(response))
            }catch(exception: Exception){
                emit(DataState.Error(exception))
            }
            emit(DataState.Loading(false))
        }.asCommonFlow()
    }
}