package com.colinjp.inblo.di

import com.colinjp.inblo.ApiSettings
import com.colinjp.inblo.datasource.network.KtorClientFactory
import com.colinjp.inblo.datasource.network.calendar.CalendarEventService
import com.colinjp.inblo.datasource.network.calendar.CalendarEventServiceImpl
import com.colinjp.inblo.datasource.network.horse_detail.HorseDetailService
import com.colinjp.inblo.datasource.network.horse_detail.HorseDetailServiceImpl
import com.colinjp.inblo.datasource.network.horse_list.HorseService
import com.colinjp.inblo.datasource.network.horse_list.HorseServiceImpl
import com.colinjp.inblo.datasource.network.horse_list.UserService
import com.colinjp.inblo.datasource.network.horse_list.UserServiceImpl
import com.colinjp.inblo.datasource.network.login.LoginService
import com.colinjp.inblo.datasource.network.login.LoginServiceImpl
import com.colinjp.inblo.datasource.network.message.MessageService
import com.colinjp.inblo.datasource.network.message.MessageServiceImpl


//private const val BASE_URL = "https://colinphl.xsrv.jp/inblo-api/api"
//private const val STAGING_BASE_URL = "https://colinphl.xsrv.jp/inblo-api-staging/api"
//private const val LOCAL_HOST = "http://localhost/api"

class NetworkModule {

    val horseService: HorseService by lazy {
        HorseServiceImpl(
            httpClient = KtorClientFactory().build(),
            baseUrl = ApiSettings.activeUrl
        )
    }

    val userService: UserService by lazy {
        UserServiceImpl(
            httpClient = KtorClientFactory().build(),
            baseUrl = ApiSettings.activeUrl
        )
    }

    val loginService: LoginService by lazy {
        LoginServiceImpl(
            httpClient = KtorClientFactory().build(),
            baseUrl = ApiSettings.activeUrl
        )
    }

    val horseDetailService: HorseDetailServiceImpl by lazy {
        HorseDetailServiceImpl(
            httpClient = KtorClientFactory().build(),
            baseUrl = ApiSettings.activeUrl
        )
    }

    val calendarService: CalendarEventService by lazy {
        CalendarEventServiceImpl(
            httpClient = KtorClientFactory().build(),
            baseUrl = ApiSettings.activeUrl
        )
    }

    val messageService: MessageService by lazy {
        MessageServiceImpl (
            httpClient = KtorClientFactory().build(),
            baseUrl = ApiSettings.activeUrl
        )
    }
}