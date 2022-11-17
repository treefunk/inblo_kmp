package com.colinjp.inblo.interactors.message

import com.colinjp.inblo.datasource.network.message.MessageService
import com.colinjp.inblo.domain.model.Message
import com.colinjp.inblo.domain.util.CommonFlow
import com.colinjp.inblo.domain.util.asCommonFlow
import com.colinjp.inblo.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class GetAllMessages constructor(
    private val messageService: MessageService
    )
{
    fun invoke(
        userId: Int
    ): CommonFlow<DataState<List<Message>>> = flow {
        emit(DataState.Loading(true))
        try {
            val messagesDto = messageService.getAllMessagesByUserId(userId).data
            emit(DataState.Data(messagesDto.map { Message.createFromDto(it) }))
        }catch (exception: Exception){
            emit(DataState.Error(exception))
        }
    }.asCommonFlow()
}