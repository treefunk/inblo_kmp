package com.colinjp.inblo.interactors.login

import com.colinjp.inblo.datasource.network.login.LoginResponse
import com.colinjp.inblo.datasource.network.login.LoginService
import com.colinjp.inblo.domain.model.User
import com.colinjp.inblo.domain.util.CommonFlow
import com.colinjp.inblo.domain.util.asCommonFlow
import com.colinjp.inblo.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class RegisterUser(
    private val loginService: LoginService
) {
    fun invoke(
        firstName: String,
        lastName: String,
        email: String,
        username: String,
        password: String,
        userType: Int,
        stableCode: String
//        stableId: Int
    ): CommonFlow<DataState<LoginResponse>> {
        return flow {
            emit(DataState.Loading(true))
            try {
                val response = loginService.register(
                    firstName, lastName, email, username, password, userType, stableCode
                )
                emit(DataState.Data(response))
            }catch(exception: Exception){
                emit(DataState.Error(exception))
            }
            emit(DataState.Loading(false))
        }.asCommonFlow()
    }
}