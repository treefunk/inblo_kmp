package com.colinjp.inblo.datasource.network.login

interface LoginService {

    suspend fun login(
        loginName: String,
        password: String
    ): LoginResponse

    suspend fun register(
        firstName: String,
        lastName: String,
        email: String,
        username: String,
        password: String,
        userType: Int,
        stableCode: String
//        stableId: Int
    ): LoginResponse

    suspend fun logout(
        userId: Int
    ): LoginResponse
}