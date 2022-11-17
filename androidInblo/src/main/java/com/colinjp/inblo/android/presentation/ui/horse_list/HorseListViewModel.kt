package com.colinjp.inblo.android.presentation.ui.horse_list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.colinjp.inblo.android.domain.util.UserPreferences
import com.colinjp.inblo.datasource.network.horse_detail.DropdownData
import com.colinjp.inblo.domain.model.Horse
import com.colinjp.inblo.domain.model.User
import com.colinjp.inblo.interactors.horse_detail.GetHorseStables
import com.colinjp.inblo.interactors.horse_list.AddHorse
import com.colinjp.inblo.interactors.horse_list.ArchiveHorse
import com.colinjp.inblo.interactors.horse_list.GetUsernames
import com.colinjp.inblo.interactors.horse_list.SearchHorses
import com.colinjp.inblo.util.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class HorseListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val searchHorses: SearchHorses,
    private val addHorseUseCase: AddHorse,
    private val getUsernames: GetUsernames,
    private val getHorseStables: GetHorseStables,
    private val archiveHorse: ArchiveHorse,
    private val userPreferencesFlow: Flow<UserPreferences>
): ViewModel() {

    val sortOrder = MutableStateFlow<SortOrder>(SortOrder.BY_DATE)
    val horses = MutableStateFlow<DataState<List<Horse>>>(DataState.Empty)
    val users = MutableStateFlow<DataState<List<User>>>(DataState.Empty)


    fun getHorses(sortOrder: SortOrder,stableId: Int){
        viewModelScope.launch(IO){
            searchHorses.invoke(stableId = stableId
            ).onEach {
                horses.value = it
            }.launchIn(viewModelScope)
        }
    }

    fun getUsernames(stableId: String? = null,
                     excludeId: String? = null){
        viewModelScope.launch(IO) {
            getUsernames.invoke(
                stableId,excludeId
            ).onEach { userResponse ->
                when(userResponse){
                    is DataState.Data -> {
                        users.value = DataState.Data(userResponse.data.data.map { userDto ->  User.createFromDto(userDto) })
                    }
                    DataState.Empty -> {

                    }
                    is DataState.Error -> {
                        //TODO:

                    }
                    is DataState.Loading -> {
                        //TODO:

                    }
                }
            }.launchIn(viewModelScope)
        }
    }



    // listen to save horse
    private val addEventChannel = Channel<AddEvent>()
    val addEventFlow = addEventChannel.receiveAsFlow()

    private val updateEventChannel = Channel<AddEvent>()
    val updateEventFlow = updateEventChannel.receiveAsFlow()

    sealed class AddEvent {
        data class Success(val message: String): AddEvent()
        data class Error(val errorMessage: String): AddEvent()
        data class Loading(val isLoading: Boolean): AddEvent()
    }

    fun sortHorses(
        sort: SortOrder
    ){
        sortOrder.value = sort
    }

    fun addHorse(
        name: String,
        userId: String,
        stableId: Int,
        ownerName: String,
        farmName: String,
        trainingFarmName: String,
        sex: String,
        color: String,
        _class: String,
        birthDate: String,
        father: String,
        mother: String,
        motherFatherName: String,
        totalStake: Double,
        notes: String,
        horseId: Int?
    ){
        viewModelScope.launch(IO){
            addHorseUseCase.invoke(
                name, userId.toInt(),stableId,ownerName, farmName, trainingFarmName, sex, color, _class, birthDate, father, mother, motherFatherName, totalStake, notes, horseId?.toString()
            ).onEach {
                when(it){
                    is DataState.Data -> {
                        Timber.v(it.toString())
                        if(horseId == null){
                            addEventChannel.send(AddEvent.Success("管理馬の登録が完了しました！"))
                        }else{
                            updateEventChannel.send(AddEvent.Success("管理馬の修正が完了しました！"))
                        }
                        userPreferencesFlow.collect { userPref ->
                            getHorses(SortOrder.BY_DATE,userPref.stableId)
                        }
                    }
                    DataState.Empty -> {

                    }
                    is DataState.Error -> {
                        Timber.e(it.toString())
                        if(horseId == null){
                            addEventChannel.send(AddEvent.Error("通信に問題があります。後ほどアクセスしてください。"))
                        }else{
                            updateEventChannel.send(AddEvent.Success("通信に問題があります。後ほどアクセスしてください。"))
                        }
                    }
                    is DataState.Loading -> {

                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun archiveHorse(
        horseId: String
    ){
        viewModelScope.launch(IO){
            archiveHorse.invoke(
                horseId
            ).onEach {
                when(it){
                    is DataState.Data -> {
                        Timber.v(it.toString())
                        if(it.data.meta.code == 200){
                            addEventChannel.send(AddEvent.Success("Horse successfully archived!"))
                        }
                        userPreferencesFlow.collect { userPref ->
                            getHorses(SortOrder.BY_DATE,userPref.stableId)
                        }
                    }
                    DataState.Empty -> {

                    }
                    is DataState.Error -> {
                        Timber.e(it.toString())
                        addEventChannel.send(AddEvent.Error("Something Went wrong."))
                    }
                    is DataState.Loading -> {

                    }
                }
            }.launchIn(viewModelScope)
        }
    }

}

enum class SortOrder { BY_DATE, BY_SCHEDULE, BY_DECIDED  }