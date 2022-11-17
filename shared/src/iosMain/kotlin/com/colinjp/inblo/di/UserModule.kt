package com.colinjp.inblo.di

import com.colinjp.inblo.interactors.horse_detail.GetHorseStables
import com.colinjp.inblo.interactors.login.LoginUser
import com.colinjp.inblo.interactors.login.LogoutUser
import com.colinjp.inblo.interactors.login.RegisterUser

class UserModule(
    val networkModule: NetworkModule
){
    val registerUser by lazy {
        RegisterUser(
            networkModule.loginService
        )
    }

    val loginUser by lazy {
        LoginUser(
            networkModule.loginService
        )
    }

    val logoutUser by lazy {
        LogoutUser(
            networkModule.loginService
        )
    }

    val getHorseStables by lazy {
        GetHorseStables(
            networkModule.horseDetailService
        )
    }
}