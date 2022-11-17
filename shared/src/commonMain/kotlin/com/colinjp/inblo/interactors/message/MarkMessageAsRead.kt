package com.colinjp.inblo.interactors.message

import com.colinjp.inblo.datasource.network.message.MessageService
import com.colinjp.inblo.domain.model.Message
import com.colinjp.inblo.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MarkMessageAsRead constructor(
    private val messageService: MessageService
)
{
    fun invoke(
        messageId: Int
    ): Flow<DataState<List<Message>>> = flow {
        emit(DataState.Loading(true))
        try {
            val messagesDto = messageService.markMessageAsRead(messageId).data
            emit(DataState.Data(messagesDto.map { Message.createFromDto(it) }))
        }catch (exception: Exception){
            emit(DataState.Error(exception))
        }
    }
}