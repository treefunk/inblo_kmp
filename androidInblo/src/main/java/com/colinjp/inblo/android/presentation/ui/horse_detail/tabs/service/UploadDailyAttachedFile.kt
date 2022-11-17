package com.colinjp.inblo.android.presentation.ui.horse_detail.tabs.service

import com.colinjp.inblo.domain.model.DailyAttachedFile
import com.colinjp.inblo.domain.model.HorseTreatment
import com.colinjp.inblo.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File

class UploadDailyAttachedFile (
    private val horseAttachFilesService: HorseAttachFilesService
    ){
    fun invoke(
        dailyReportId: String,
        file: File,
        contentType: String,
        uploadListener: HorseAttachFilesService.UploadListener
    ): Flow<DataState<DailyAttachedFile>> = flow{
        emit(DataState.Loading(true))
        try {
            val dailyAttachedFile = horseAttachFilesService.attachFile(
                dailyReportId, file, contentType,uploadListener
            ).data
            emit(DataState.Data(DailyAttachedFile.createFromDto(dailyAttachedFile)))
        }catch (exception: Exception){
            emit(DataState.Error(exception))
        }
    }
}