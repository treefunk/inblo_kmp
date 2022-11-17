package com.colinjp.inblo.datasource.network.login

import io.ktor.client.*
import io.ktor.client.request.*

class LoginServiceImpl constructor(
    private val httpClient: HttpClient,
    private val baseUrl: String
): LoginService {

    override suspend fun login(loginName: String, password: String): LoginResponse {
        return httpClient.post<LoginResponse> {
            url("$baseUrl/login")
            parameter("username",loginName)
            parameter("password",password)
        }
    }

    override suspend fun register(
        firstName: String,
        lastName: String,
        email: String,
        username: String,
        password: String,
        userType: Int,
        stableCode: String
//        stableId: Int
    ): LoginResponse {
        return httpClient.post<LoginResponse> {
            url("$baseUrl/register")
            parameter("first_name", firstName)
            parameter("last_name", lastName)
            parameter("email", email)
            parameter("username", username)
            parameter("password", password)
            parameter("user_type",userType)
//            parameter("stable_id", stableId)
            parameter("stable_code", stableCode)
        }
    }

    override suspend fun logout(userId: Int): LoginResponse {
        return httpClient.post<LoginResponse> {
            url("$baseUrl/logout")
            parameter("user_id", userId.toString())
        }
    }

}