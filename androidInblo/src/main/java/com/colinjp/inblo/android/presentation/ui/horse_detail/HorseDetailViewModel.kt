package com.colinjp.inblo.android.presentation.ui.horse_detail

import androidx.lifecycle.*
import com.colinjp.inblo.android.presentation.ui.horse_detail.tabs.service.HorseAttachFilesService
import com.colinjp.inblo.android.presentation.ui.horse_detail.tabs.service.UploadDailyAttachedFile
import com.colinjp.inblo.datasource.network.horse_detail.DropdownData
import com.colinjp.inblo.datasource.network.horse_detail.responses.GetDailyReportFormResponse
import com.colinjp.inblo.datasource.network.horse_detail.responses.GetHorseDailyListResponse
import com.colinjp.inblo.domain.model.HorseDaily
import com.colinjp.inblo.domain.model.HorseTrainingRecord
import com.colinjp.inblo.domain.model.HorseTreatment
import com.colinjp.inblo.interactors.horse_detail.*
import com.colinjp.inblo.util.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow

import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import javax.inject.Inject

@HiltViewModel
class HorseDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getHorseDailyReports: GetHorseDailyReports,
    private val getHorseTrainingRecords: GetHorseTrainingRecords,
    private val getHorseTreatments: GetHorseTreatments,
    private val addHorseCondition: AddHorseCondition,
    private val getDailyReportForm: GetDailyReportForm,
    private val addHorseDailyReport: AddHorseDailyReport,
    private val addHorseTrainingRecord: AddHorseTrainingRecord,
    private val addHorseTreatmentRecord: AddHorseTreatment,
    private val getHorseTrainingTypesDropdown: GetHorseTrainingTypesDropdown,
    private val getHorseTrainingDetailsDropdown: GetHorseTrainingDetailsDropdown,
    private val deleteHorseDailyReport: DeleteHorseDailyReport,
    private val deleteHorseTraining: DeleteHorseTraining,
    private val deleteHorseTreatment: DeleteHorseTreatment,
    private val uploadDailyAttachedFileUseCase: UploadDailyAttachedFile
): ViewModel() {

    // fetch records
    val dailyReportFormData   = MutableStateFlow<DataState<GetDailyReportFormResponse>>(DataState.Empty)
    val horseDailyReports    = MutableStateFlow<DataState<GetHorseDailyListResponse>>(DataState.Empty)
    val horseTrainingRecords = MutableStateFlow<DataState<List<HorseTrainingRecord>>>(DataState.Empty)
    val horseTreatments      = MutableStateFlow<DataState<List<HorseTreatment>>>(DataState.Empty)

    // dropdown data
    val trainingTypes        = MutableStateFlow<DataState<List<DropdownData>>>(DataState.Empty)
    val trainingDetails      = MutableStateFlow<DataState<List<DropdownData>>>(DataState.Empty)

    // listen to save condition/training/treatment
    private val addEventChannel = Channel<AddEvent>()
    val addEventFlow = addEventChannel.receiveAsFlow()

    sealed class AddEvent {
        data class Success(val message: String): AddEvent()
        data class Error(val errorMessage: String): AddEvent()
        data class Loading(val isLoading: Boolean): AddEvent()
    }

    fun initHorse(horseId: Int){
        val id = horseId.toString()
        viewModelScope.launch(IO) {
            fetchHorseDailyReports(id)
            fetchHorseTrainingRecords(id)
            fetchHorseTreatments(id)
        }
    }

    fun fetchDropDownData(stableId: String){
        viewModelScope.launch(IO) {
//            fetchTrainingTypes()
//            fetchTrainingDetails()
            fetchDailyReportFormData(stableId)
        }
    }

    private fun fetchDailyReportFormData(stableId: String){
        viewModelScope.launch(IO){
            getDailyReportForm.invoke(stableId).onEach {
                dailyReportFormData.value = it
            }.launchIn(viewModelScope)
        }
    }

    private fun fetchTrainingTypes(){
        getHorseTrainingTypesDropdown.invoke().onEach {
            trainingTypes.value = it
        }.launchIn(viewModelScope)
    }

    private fun fetchTrainingDetails(){
        getHorseTrainingDetailsDropdown.invoke().onEach {
            trainingDetails.value = it
        }.launchIn(viewModelScope)
    }

    private fun fetchHorseDailyReports(id: String){
        getHorseDailyReports.invoke(id).onEach {
            horseDailyReports.value = it
        }.launchIn(viewModelScope)
    }

    private fun fetchHorseTrainingRecords(id: String){
        getHorseTrainingRecords.invoke(id).onEach {
            horseTrainingRecords.value = it
        }.launchIn(viewModelScope)
    }

    private fun fetchHorseTreatments(id: String){
        getHorseTreatments.invoke(id).onEach {
            horseTreatments.value = it
        }.launchIn(viewModelScope)
    }

    fun uploadDailyAttachfile(
        dailyReportId: String,
        file: File,
        contentType: String
    ){
        viewModelScope.launch(IO) {
            uploadDailyAttachedFileUseCase.invoke(dailyReportId, file, contentType, object: HorseAttachFilesService.UploadListener{
                override fun onUpload(bytesSentTotal: Long, contentLength: Long) {
                    Timber.v("bytes: ${bytesSentTotal} / ${contentLength}")
                }
            })
                .onEach {
                    when(it){
                        is DataState.Data -> {
                            addEventChannel.send(AddEvent.Success("${file.name} successfully uploaded!"))
                        }
                        DataState.Empty -> {

                        }
                        is DataState.Error -> {
                            Timber.v(it.exception.message)
                            addEventChannel.send(AddEvent.Success("file not uploaded"))
                        }
                        is DataState.Loading -> {

                        }
                    }
                }.launchIn(viewModelScope)
        }
    }

    fun addHorseDaily(
        horseId: String,
        date: String,
        bodyTemperature: String,
        horseWeight: String,
        conditionGroup: String,
        riderName: String,
        trainingTypeName: String,
        riderId: Int?,
        trainingTypeId: Int?,
        trainingAmount: String,
        time5f: String,
        time4f: String,
        time3f: String,
        memo: String,
        files: List<File>,
        dailyAttachedIds: List<String>?,
        dailyReportId: String?
    ){

        viewModelScope.launch(IO){
            addHorseDailyReport.invoke(
                horseId, date, bodyTemperature, horseWeight, conditionGroup, riderName, trainingTypeName,riderId?.toString(),trainingTypeId?.toString(), trainingAmount, time5f, time4f, time3f, memo, dailyAttachedIds,dailyReportId
            ).onEach {
                when(it){
                    is DataState.Data -> {
                        addEventChannel.send(AddEvent.Success(if(dailyReportId == null)"Daily Report added successfully!" else "Daily report updated successfully!"))
                        val reportId = it.data.id
                        if(files.isNotEmpty()){
                            files.forEach { f ->
                                uploadDailyAttachfile(reportId.toString(),f,"daily_reports")
                            }
                        }
                        addEventChannel.send(AddEvent.Loading(false))
                    }
                    DataState.Empty -> {

                    }
                    is DataState.Error -> {
                        Timber.e(it.exception)
                        addEventChannel.send(AddEvent.Error("通信に問題があります。後ほどアクセスしてください。"))
                        addEventChannel.send(AddEvent.Loading(false))
                    }
                    is DataState.Loading -> {
                        addEventChannel.send(AddEvent.Loading(true))
                    }
                }

            }.launchIn(viewModelScope)
        }
    }

    fun addHorseCondition(
        horseId: String,
        date: String,
        weather: String,
        bodyTemperature: String,
        horseWeight: String,
        accidentSite: String,
        accidentPart: String,
        accidentType: String,
        memo: String,
        horseConditionId: String?
    ){
        viewModelScope.launch(IO){
            addHorseCondition.invoke(
                horseId, date, weather, bodyTemperature, horseWeight, accidentSite, accidentPart, accidentType, memo, horseConditionId
            ).onEach {
                when(it){
                    is DataState.Data -> {
                        addEventChannel.send(AddEvent.Success(if(horseConditionId == null)"状態を記録しました。" else "状態を修正しました。"))
                        addEventChannel.send(AddEvent.Loading(false))
                    }
                    DataState.Empty -> {

                    }
                    is DataState.Error -> {
                        Timber.e(it.exception)
                        addEventChannel.send(AddEvent.Error("通信に問題があります。後ほどアクセスしてください。"))
                        addEventChannel.send(AddEvent.Loading(false))
                    }
                    is DataState.Loading -> {
                        addEventChannel.send(AddEvent.Loading(true))
                    }
                }

            }.launchIn(viewModelScope)
        }
    }

    fun removeHorseDaily(
        horseConditionId: String
    ){
        viewModelScope.launch(IO){
            deleteHorseDailyReport.invoke(
                horseConditionId
            ).onEach {
                when(it){
                    is DataState.Data -> {
                        addEventChannel.send(AddEvent.Success("状態を削除しました。"))
                        addEventChannel.send(AddEvent.Loading(false))
                    }
                    DataState.Empty -> {

                    }
                    is DataState.Error -> {
                        Timber.e(it.exception)
                        addEventChannel.send(AddEvent.Error("通信に問題があります。後ほどアクセスしてください。"))
                        addEventChannel.send(AddEvent.Loading(false))
                    }
                    is DataState.Loading -> {
                        addEventChannel.send(AddEvent.Loading(true))
                    }
                }

            }.launchIn(viewModelScope)
        }
    }

    fun removeHorseTraining(
        horseTrainingId: String
    ){
        viewModelScope.launch(IO){
            deleteHorseTraining.invoke(
                horseTrainingId
            ).onEach {
                when(it){
                    is DataState.Data -> {
                        addEventChannel.send(AddEvent.Success("調教を削除しました。"))
                        addEventChannel.send(AddEvent.Loading(false))
                    }
                    DataState.Empty -> {

                    }
                    is DataState.Error -> {
                        Timber.e(it.exception)
                        addEventChannel.send(AddEvent.Error("通信に問題があります。後ほどアクセスしてください。"))
                        addEventChannel.send(AddEvent.Loading(false))
                    }
                    is DataState.Loading -> {
                        addEventChannel.send(AddEvent.Loading(true))
                    }
                }

            }.launchIn(viewModelScope)
        }
    }

    fun removeHorseTreatment(
        horseTreatmentId: String
    ){
        viewModelScope.launch(IO){
            deleteHorseTreatment.invoke(
                horseTreatmentId
            ).onEach {
                when(it){
                    is DataState.Data -> {
                        addEventChannel.send(AddEvent.Success("治療を削除しました。"))
                        addEventChannel.send(AddEvent.Loading(false))
                    }
                    DataState.Empty -> {

                    }
                    is DataState.Error -> {
                        Timber.e(it.exception)
                        addEventChannel.send(AddEvent.Error("通信に問題があります。後ほどアクセスしてください。"))
                        addEventChannel.send(AddEvent.Loading(false))
                    }
                    is DataState.Loading -> {
                        addEventChannel.send(AddEvent.Loading(true))
                    }
                }

            }.launchIn(viewModelScope)
        }
    }

    fun addHorseTraining(
        horseId: String,
        date: String,
        weather: String,
        trainingType: String,
        trainingDetail: String,
        memo: String,
        time6F: String,
        time5F: String,
        time4F: String,
        time3F: String,
        time2F: String,
        time1F: String,
        horseTrainingId: String? = null
    ){
        viewModelScope.launch(IO){
            addHorseTrainingRecord.invoke(
                horseId, date, weather, trainingType, trainingDetail, memo, time6F, time5F, time4F, time3F, time2F, time1F,horseTrainingId
            ).onEach {
                when(it){
                    is DataState.Data -> {
                        addEventChannel.send(AddEvent.Success(if(horseTrainingId == null) "調教を記録しました。" else "調教を修正しました。"))
                        addEventChannel.send(AddEvent.Loading(false))
                    }
                    DataState.Empty -> {

                    }
                    is DataState.Error -> {
                        Timber.e(it.exception)
                        addEventChannel.send(AddEvent.Error("通信に問題があります。後ほどアクセスしてください。"))
                        addEventChannel.send(AddEvent.Loading(false))
                    }
                    is DataState.Loading -> {
                        addEventChannel.send(AddEvent.Loading(true))
                    }
                }

            }.launchIn(viewModelScope)
        }
    }

    fun addHorseTreatment(
        horseId: String,
        date: String,
        vetName: String,
        treatmentDetail: String,
        injuredPart: String,
        occasionType: String,
        medicineName: String,
        memo: String,
        files: List<File>,
        dailyAttachedIds: List<String>?,
        horseTreatmentId: String? = null
    ){
        viewModelScope.launch(IO){
            addHorseTreatmentRecord.invoke(
                horseId, date, vetName, treatmentDetail,injuredPart,occasionType, medicineName, memo,dailyAttachedIds, horseTreatmentId
            ).onEach {
                when(it){
                    is DataState.Data -> {
                        addEventChannel.send(AddEvent.Success(if(horseTreatmentId == null) "治療を記録しました。" else "治療を修正しました。"))
                        addEventChannel.send(AddEvent.Loading(false))
                        val treatmentId = it.data.id
                        if(files.isNotEmpty()){
                            files.forEach { f ->
                                uploadDailyAttachfile(treatmentId.toString(),f,"treatments")
                            }
                        }
                    }
                    DataState.Empty -> {

                    }
                    is DataState.Error -> {
                        Timber.e(it.exception)
                        addEventChannel.send(AddEvent.Error("通信に問題があります。後ほどアクセスしてください。"))
                        addEventChannel.send(AddEvent.Loading(false))
                    }
                    is DataState.Loading -> {
                        addEventChannel.send(AddEvent.Loading(true))
                    }
                }

            }.launchIn(viewModelScope)
        }
    }




}