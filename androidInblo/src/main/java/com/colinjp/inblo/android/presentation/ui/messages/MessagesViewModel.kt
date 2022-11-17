package com.colinjp.inblo.android.presentation.ui.messages

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.colinjp.inblo.android.presentation.ui.horse_list.SortOrder
import com.colinjp.inblo.domain.model.Horse
import com.colinjp.inblo.domain.model.Message
import com.colinjp.inblo.domain.model.User
import com.colinjp.inblo.interactors.horse_list.GetUsernames
import com.colinjp.inblo.interactors.horse_list.SearchHorses
import com.colinjp.inblo.interactors.message.GetAllMessages
import com.colinjp.inblo.interactors.message.MarkMessageAsRead
import com.colinjp.inblo.interactors.message.RemoveMessage
import com.colinjp.inblo.interactors.message.SendMessageToUser
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
import javax.inject.Inject

@HiltViewModel
class MessagesViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getAllMessages: GetAllMessages,
    private val sendMessageToUser: SendMessageToUser,
    private val markMessageAsRead: MarkMessageAsRead,
    private val removeMessage: RemoveMessage,
    private val getUsernames: GetUsernames,
    private val searchHorses: SearchHorses
): ViewModel() {

    val messages = MutableStateFlow<DataState<List<Message>>>(DataState.Empty)

    val horses = MutableStateFlow<DataState<List<Horse>>>(DataState.Empty)
    val users = MutableStateFlow<DataState<List<User>>>(DataState.Empty)

    fun getHorses(sortOrder: SortOrder, stableId: Int){
        viewModelScope.launch(IO){
            searchHorses.invoke(stableId = stableId
            ).onEach {
                horses.value = it
            }.launchIn(viewModelScope)
        }
    }

    fun getUsernames(stableId: String? = null,excludeId: String? = null){
        viewModelScope.launch(IO) {
            getUsernames.invoke(stableId,excludeId).onEach { userResponse ->
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

    private val eventChannel = Channel<MessageEvent>()
    val eventFlow = eventChannel.receiveAsFlow()

    fun getMessages(userId: Int){
        viewModelScope.launch(IO){
            getAllMessages.invoke(userId).onEach {
                messages.value = it
            }.launchIn(viewModelScope)
        }
    }

    fun sendMessage(
        stableId: Int,
        senderId: Int,
        recipientId: Int?,
        horseId: Int?,
        horseName: String,
        notificationType: String,
        title: String,
        memo: String,
        isRead: String,
        messageId: Int?
    ){
        viewModelScope.launch(IO){
            sendMessageToUser.invoke(stableId,senderId, recipientId.toString(), horseId.toString(), horseName, notificationType, title, memo, isRead,messageId.toString())
                .onEach {
                    when(it){
                        is DataState.Data -> {
                            Timber.v(it.toString())

                            if(messageId == null ) eventChannel.send(MessageEvent.Success("Message successfully sent!")) else eventChannel.send(MessageEvent.Success("Message successfully updated!"))
                        }
                        DataState.Empty -> {

                        }
                        is DataState.Error -> {
                            //TODO:

                            Timber.e(it.toString())
                        }
                        is DataState.Loading -> {
                            //TODO:

                        }
                    }
                }.launchIn(viewModelScope)
        }
    }

    fun markMessageAsRead(
        messageId: Int
    ) {
        viewModelScope.launch(IO){
            markMessageAsRead.invoke(messageId).onEach {
                when(it){
                    is DataState.Data -> {
                        Timber.v("Message $messageId marked as read")
                        messages.value = it
                    }
                    DataState.Empty -> {

                    }
                    is DataState.Error -> {
                        //TODO:

                        Timber.e(it.exception)
                    }
                    is DataState.Loading -> {
                        //TODO:

                    }
                }
            }
        }
    }

    fun deleteMessage(
        messageId: Int
    ){
        viewModelScope.launch(IO){
            removeMessage.invoke(messageId).onEach {
                when(it){
                    is DataState.Data -> {
                        Timber.v("Message $messageId removed")
                        eventChannel.send(MessageEvent.Success("Message Successfully removed!"))
                    }
                    DataState.Empty -> {

                    }
                    is DataState.Error -> {
                        //TODO:

                        Timber.e(it.exception)
                    }
                    is DataState.Loading -> {
                        //TODO:

                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    sealed class MessageEvent {
        data class Success(val message: String): MessageEvent()
        data class Error(val errorMessage: String): MessageEvent()
        data class Loading(val isLoading: Boolean): MessageEvent()
    }
}