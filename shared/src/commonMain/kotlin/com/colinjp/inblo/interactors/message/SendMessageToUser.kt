package com.colinjp.inblo.interactors.message

import com.colinjp.inblo.datasource.network.message.MessageService
import com.colinjp.inblo.domain.model.Message
import com.colinjp.inblo.domain.util.CommonFlow
import com.colinjp.inblo.domain.util.asCommonFlow
import com.colinjp.inblo.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SendMessageToUser constructor(
    private val messageService: MessageService
)
{
    fun invoke(
        stableId: Int,
        senderId: Int,
        recipientId: String?,
        horseId: String?,
        horseName: String,
        notificationType: String,
        title: String,
        memo: String,
        isRead: String,
        messageId: String?
    ): CommonFlow<DataState<Message>> = flow {
        emit(DataState.Loading(true))

        val recipientIdInt = recipientId?.toInt()
        val horseIdInt = if(horseId == "0") null else horseId?.toInt()
        val messageIdInt = if(messageId == "0") null else messageId?.toInt()

        try {
            val messageDto = messageService.sendMessage(
                stableId, senderId, recipientIdInt, horseIdInt, horseName, notificationType, title, memo, isRead,messageIdInt
            ).data
            emit(DataState.Data(Message.createFromDto(messageDto)))
        }catch (exception: Exception){
            emit(DataState.Error(exception))
        }
    }.asCommonFlow()
}