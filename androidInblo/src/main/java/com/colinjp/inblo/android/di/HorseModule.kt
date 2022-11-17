package com.colinjp.inblo.android.di

import com.colinjp.inblo.android.presentation.ui.horse_detail.tabs.service.HorseAttachFilesService
import com.colinjp.inblo.android.presentation.ui.horse_detail.tabs.service.UploadDailyAttachedFile
import com.colinjp.inblo.datasource.network.horse_detail.HorseDetailServiceImpl
import com.colinjp.inblo.datasource.network.horse_list.HorseService
import com.colinjp.inblo.datasource.network.horse_list.HorseServiceImpl
import com.colinjp.inblo.interactors.horse_detail.*
import com.colinjp.inblo.interactors.horse_list.AddHorse
import com.colinjp.inblo.interactors.horse_list.ArchiveHorse
import com.colinjp.inblo.interactors.horse_list.RestoreArchivedHorse
import com.colinjp.inblo.interactors.horse_list.SearchHorses
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import io.ktor.client.*

@Module
@InstallIn(ActivityRetainedComponent::class)
class HorseModule {

    @ActivityRetainedScoped
    @Provides
    fun provideHorseService(
        httpClient: HttpClient,
        @BaseURL baseURL: String
    ): HorseService {
        return HorseServiceImpl(
            httpClient,baseURL
        )
    }

    @ActivityRetainedScoped
    @Provides
    fun provideHorseDetailService(
        httpClient: HttpClient,
        @BaseURL baseURL: String,
    ): HorseDetailServiceImpl {
        return HorseDetailServiceImpl(
            httpClient, baseURL
        )
    }

    @ActivityRetainedScoped
    @Provides
    fun provideHorseAttachFilesService(
        httpClient: HttpClient,
        @BaseURL baseURL: String
    ): HorseAttachFilesService {
        return HorseAttachFilesService(
            httpClient, baseURL
        )
    }


    // Use cases for horse
    @ActivityRetainedScoped
    @Provides
    fun provideSearchHorses(
        horseService: HorseService
    ): SearchHorses {
        return SearchHorses(horseService)
    }


    /**
     *  USE CASES FOR HORSE DETAILS
     */

    @ActivityRetainedScoped
    @Provides
    fun provideGetHorseConditions(
        horseDetailService: HorseDetailServiceImpl
    ): GetHorseDailyReports {
        return GetHorseDailyReports(
            horseDetailService
        )
    }

    @ActivityRetainedScoped
    @Provides
    fun provideGetHorseTrainingRecords(
        horseDetailService: HorseDetailServiceImpl
    ): GetHorseTrainingRecords {
        return GetHorseTrainingRecords(
            horseDetailService
        )
    }

    @ActivityRetainedScoped
    @Provides
    fun provideGetTreatments(
        horseDetailService: HorseDetailServiceImpl
    ): GetHorseTreatments {
        return GetHorseTreatments(
            horseDetailService
        )
    }

    @ActivityRetainedScoped
    @Provides
    fun provideAddHorseCondition(
        horseDetailService: HorseDetailServiceImpl
    ): AddHorseCondition {
        return AddHorseCondition(
            horseDetailService
        )
    }

    @ActivityRetainedScoped
    @Provides
    fun provideAddHorseDailyReport(
        horseDetailService: HorseDetailServiceImpl
    ): AddHorseDailyReport {
        return AddHorseDailyReport(
            horseDetailService
        )
    }

    @ActivityRetainedScoped
    @Provides
    fun provideDeleteHorseCondition(
        horseDetailService: HorseDetailServiceImpl
    ): DeleteHorseDailyReport {
        return DeleteHorseDailyReport(
            horseDetailService
        )
    }

    @ActivityRetainedScoped
    @Provides
    fun provideDeleteHorseTraining(
        horseDetailService: HorseDetailServiceImpl
    ): DeleteHorseTraining {
        return DeleteHorseTraining(
            horseDetailService
        )
    }

    @ActivityRetainedScoped
    @Provides
    fun provideDeleteHorseTreatment(
        horseDetailService: HorseDetailServiceImpl
    ): DeleteHorseTreatment {
        return DeleteHorseTreatment(
            horseDetailService
        )
    }

    @ActivityRetainedScoped
    @Provides
    fun provideAddHorseTrainingRecord(
        horseDetailService: HorseDetailServiceImpl
    ): AddHorseTrainingRecord {
        return AddHorseTrainingRecord(
            horseDetailService
        )
    }

    @ActivityRetainedScoped
    @Provides
    fun provideAddHorseTreatmentCondition(
        horseDetailService: HorseDetailServiceImpl
    ): AddHorseTreatment {
        return AddHorseTreatment(
            horseDetailService
        )
    }

    @ActivityRetainedScoped
    @Provides
    fun provideUploadDailyAttachFile(
        horseAttachFilesService: HorseAttachFilesService
    ): UploadDailyAttachedFile {
        return UploadDailyAttachedFile(horseAttachFilesService)
    }

    @ActivityRetainedScoped
    @Provides
    fun provideGetTrainingTypes(
        horseDetailService: HorseDetailServiceImpl
    ): GetHorseTrainingTypesDropdown {
        return GetHorseTrainingTypesDropdown(
            horseDetailService
        )
    }

    @ActivityRetainedScoped
    @Provides
    fun provideGetTrainingDetails(
        horseDetailService: HorseDetailServiceImpl
    ): GetHorseTrainingDetailsDropdown {
        return GetHorseTrainingDetailsDropdown(
            horseDetailService
        )
    }

    @ActivityRetainedScoped
    @Provides
    fun provideGetHorseStables(
        horseDetailService: HorseDetailServiceImpl
    ): GetHorseStables {
        return GetHorseStables(
            horseDetailService
        )
    }

    @ActivityRetainedScoped
    @Provides
    fun provideAddHorse(
        horseServiceImpl: HorseService
    ): AddHorse {
        return AddHorse(
            horseServiceImpl
        )
    }

    @ActivityRetainedScoped
    @Provides
    fun provideArchiveHorse(
        horseServiceImpl: HorseService
    ): ArchiveHorse {
        return ArchiveHorse(
            horseServiceImpl
        )
    }

    @ActivityRetainedScoped
    @Provides
    fun provideRestoreArchivedHorse(
        horseServiceImpl: HorseService
    ): RestoreArchivedHorse {
        return RestoreArchivedHorse(
            horseServiceImpl
        )
    }

    @ActivityRetainedScoped
    @Provides
    fun provideGetDailyReportForm(
        horseDetailService: HorseDetailServiceImpl
    ): GetDailyReportForm {
        return GetDailyReportForm(
            horseDetailService
        )
    }









}