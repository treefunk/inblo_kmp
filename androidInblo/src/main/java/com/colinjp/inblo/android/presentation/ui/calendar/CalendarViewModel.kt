package com.colinjp.inblo.android.presentation.ui.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.colinjp.inblo.android.domain.util.UserPreferences
import com.colinjp.inblo.android.presentation.ui.horse_list.SortOrder
import com.colinjp.inblo.domain.model.Event
import com.colinjp.inblo.domain.model.Horse
import com.colinjp.inblo.interactors.Calendar.AddEventUseCase
import com.colinjp.inblo.interactors.Calendar.DeleteEventUseCase
import com.colinjp.inblo.interactors.Calendar.GetAllEventsByMonthYear
import com.colinjp.inblo.interactors.horse_list.SearchHorses
import com.colinjp.inblo.util.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val getAllEventsByMonthYear: GetAllEventsByMonthYear,
    private val searchHorses: SearchHorses,
    private val addEventUseCase: AddEventUseCase,
    private val deleteEventUseCase: DeleteEventUseCase,
    private val userPreferencesFlow: Flow<UserPreferences>
): ViewModel() {


    val events = MutableStateFlow<DataState<List<Event>>>(DataState.Empty)
    val horses = MutableStateFlow<DataState<List<Horse>>>(DataState.Empty)
    
    private val addEventChannel = Channel<AddEvent>()
    val addEventFlow = addEventChannel.receiveAsFlow()


    fun fetchEvents(
        stableId: Int,
        month: String,
        year: String,
        horseId: Int,
    ) {
        getAllEventsByMonthYear.invoke(stableId, month, year,horseId).onEach {
            events.value = it
        }.launchIn(viewModelScope)
    }

    fun getHorses(sortOrder: SortOrder, stableId: Int){
        viewModelScope.launch(Dispatchers.IO){
            searchHorses.invoke(stableId = stableId
            ).onEach {
                horses.value = it
            }.launchIn(viewModelScope)
        }
    }

    fun addCalendarEvent(
        horseId: Int?,
        userId: Int,
        stableId: Int,
        date: String,
        dateEnd: String?,
        title: String,
        eventType: String,
        start: String,
        end: String,
        memo: String,
        eventId: Int?
    ){
        viewModelScope.launch(Dispatchers.IO){
            addEventUseCase.invoke(horseId.toString(), userId, stableId, date, dateEnd, title, eventType, start, end, memo, eventId.toString()
            ).onEach {
                when(it){
                    is DataState.Data -> {
                        if(eventId == 0){
                            addEventChannel.send(AddEvent.Success("Event Successfully Created!",date))
                        }else{
                            addEventChannel.send(AddEvent.Success("Event Successfully Updated!",date))
                        }
                        userPreferencesFlow.collect { p ->
                            fetchEvents(p.stableId,date.substring(5,7),date.substring(0,4),horseId ?: 0)
                        }
                    }
                    DataState.Empty -> { }
                    is DataState.Error -> {
                        Timber.v(it.exception)
                    }
                    is DataState.Loading -> { }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun removeEvent(eventId: Int,date: String){
        viewModelScope.launch(Dispatchers.IO){
            deleteEventUseCase.invoke(eventId).onEach {
                when(it){
                    is DataState.Data -> {
                        addEventChannel.send(AddEvent.Success("Event Successfully Deleted",date))
                        userPreferencesFlow.collect { p ->
                            fetchEvents(p.stableId,date.substring(5,7),date.substring(0,4),0)
                        }
                    }
                    DataState.Empty -> { }
                    is DataState.Error -> {
                        Timber.v(it.exception)
                    }
                    is DataState.Loading -> { }
                }
            }.launchIn(viewModelScope)
        }
    }

    sealed class AddEvent {
        data class Success(val message: String, val date: String): AddEvent()
        data class Error(val errorMessage: String): AddEvent()
        data class Loading(val isLoading: Boolean): AddEvent()
    }

}