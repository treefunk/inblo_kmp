package com.colinjp.inblo.android.di

import com.colinjp.inblo.ApiSettings
import com.colinjp.inblo.datasource.network.KtorClientFactory
import com.colinjp.inblo.datasource.network.login.LoginService
import com.colinjp.inblo.datasource.network.login.LoginServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import javax.inject.Qualifier
import javax.inject.Singleton


//private const val BASE_URL = "https://colinphl.xsrv.jp/inblo-api/api"
//private const val STAGING_BASE_URL = "https://colinphl.xsrv.jp/inblo-api-staging/api"
//private const val LOCAL_HOST = "http://localhost/api"


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {


    @Singleton
    @Provides
    fun provideHttpClient(): HttpClient {
        return KtorClientFactory()
            .build()
    }

    @BaseURL
    @Singleton
    @Provides
    fun provideBaseUrl(): String {
        return ApiSettings.activeUrl
    }

}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BaseURL