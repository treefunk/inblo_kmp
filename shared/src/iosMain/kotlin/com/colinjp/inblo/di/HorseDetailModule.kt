package com.colinjp.inblo.di

import com.colinjp.inblo.interactors.horse_detail.*

class HorseDetailModule(
    val networkModule: NetworkModule
) {

    val getHorseDailyReports: GetHorseDailyReports by lazy {
        GetHorseDailyReports(networkModule.horseDetailService)
    }

    val getHorseTreatments: GetHorseTreatments by lazy {
        GetHorseTreatments(networkModule.horseDetailService)
    }

    val addHorseDailyReport: AddHorseDailyReport by lazy {
        AddHorseDailyReport(networkModule.horseDetailService)
    }

    val addHorseTreatment: AddHorseTreatment by lazy {
        AddHorseTreatment(networkModule.horseDetailService)
    }

    val deleteHorseDailyReport: DeleteHorseDailyReport by lazy {
        DeleteHorseDailyReport(networkModule.horseDetailService)
    }

    val deleteHorseTreatment: DeleteHorseTreatment by lazy {
        DeleteHorseTreatment(networkModule.horseDetailService)
    }

    val getDailyReportForm: GetDailyReportForm by lazy {
        GetDailyReportForm(networkModule.horseDetailService)
    }

}