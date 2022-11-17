package com.colinjp.inblo.interactors.message

import com.colinjp.inblo.datasource.network.BooleanResponse
import com.colinjp.inblo.datasource.network.message.MessageService
import com.colinjp.inblo.domain.model.Message
import com.colinjp.inblo.domain.util.CommonFlow
import com.colinjp.inblo.domain.util.asCommonFlow
import com.colinjp.inblo.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RemoveMessage constructor(
    private val messageService: MessageService
)
{
    fun invoke(
        messageId: Int
    ): CommonFlow<DataState<BooleanResponse>> = flow {
        emit(DataState.Loading(true))
        try {
            val response = messageService.removeMessage(messageId)
            emit(DataState.Data(response))
        } catch (exception: Exception) {
            emit(DataState.Error(exception))
        }
    }.asCommonFlow()
}