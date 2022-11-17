package com.colinjp.inblo.android.presentation.ui.horse_list_archive

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.colinjp.inblo.android.domain.util.UserPreferences
import com.colinjp.inblo.domain.model.Horse
import com.colinjp.inblo.domain.model.User
import com.colinjp.inblo.interactors.horse_detail.GetHorseStables
import com.colinjp.inblo.interactors.horse_list.*
import com.colinjp.inblo.util.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class HorseArchiveListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val searchHorses: SearchHorses,
    private val restoreArchivedHorse: RestoreArchivedHorse,
    private val userPreferencesFlow: Flow<UserPreferences>
): ViewModel() {

    val horses = MutableStateFlow<DataState<List<Horse>>>(DataState.Empty)


    fun getHorses(sortOrder: SortOrder,stableId: Int){
        viewModelScope.launch(Dispatchers.IO){
            searchHorses.invoke(stableId = stableId, isArchived = true).onEach {
                horses.value = it
            }.launchIn(viewModelScope)
        }
    }




    // listen to save horse
    private val addEventChannel = Channel<AddEvent>()
    val addEventFlow = addEventChannel.receiveAsFlow()

    sealed class AddEvent {
        data class Success(val message: String): AddEvent()
        data class Error(val errorMessage: String): AddEvent()
        data class Loading(val isLoading: Boolean): AddEvent()
    }


    fun restoreHorse(
        horseId: String
    ){
        viewModelScope.launch(Dispatchers.IO){
            restoreArchivedHorse.invoke(
                horseId
            ).onEach {
                when(it){
                    is DataState.Data -> {
                        Timber.v(it.toString())
                        if(it.data.meta.code == 200){
                            addEventChannel.send(AddEvent.Success("Horse successfully restored!"))
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