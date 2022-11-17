package com.colinjp.inblo.android.di

import com.colinjp.inblo.datasource.network.horse_list.UserService
import com.colinjp.inblo.datasource.network.horse_list.UserServiceImpl
import com.colinjp.inblo.datasource.network.login.LoginService
import com.colinjp.inblo.datasource.network.login.LoginServiceImpl
import com.colinjp.inblo.interactors.horse_list.GetUsernames
import com.colinjp.inblo.interactors.login.LoginUser
import com.colinjp.inblo.interactors.login.LogoutUser
import com.colinjp.inblo.interactors.login.RegisterUser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import io.ktor.client.*
import javax.inject.Singleton


@Module
@InstallIn(ActivityRetainedComponent::class)
class LoginModule {

    @ActivityRetainedScoped
    @Provides
    fun provideLoginService(
        httpClient: HttpClient,
        @BaseURL baseURL: String
    ): LoginService {
        return LoginServiceImpl(
            httpClient = httpClient,
            baseUrl = baseURL
        )
    }

    @ActivityRetainedScoped
    @Provides
    fun provideRegisterUser(
        loginService: LoginService
    ): RegisterUser {
        return RegisterUser(loginService)
    }

    @ActivityRetainedScoped
    @Provides
    fun provideLoginUser(
        loginService: LoginService
    ): LoginUser {
        return LoginUser(loginService)
    }

    @ActivityRetainedScoped
    @Provides
    fun provideLogoutUser(
        loginService: LoginService
    ): LogoutUser {
        return LogoutUser(loginService)
    }

    @ActivityRetainedScoped
    @Provides
    fun provideUserService(
        httpClient: HttpClient,
        @BaseURL baseURL: String
    ): UserService {
        return UserServiceImpl(
            httpClient = httpClient,
            baseUrl = baseURL
        )
    }

    @ActivityRetainedScoped
    @Provides
    fun provideGetAllUsernames(
        userService: UserService
    ): GetUsernames {
        return GetUsernames(userService)
    }
}