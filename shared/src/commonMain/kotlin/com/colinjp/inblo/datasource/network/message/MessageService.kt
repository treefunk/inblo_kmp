package com.colinjp.inblo.datasource.network.message

import com.colinjp.inblo.datasource.network.BooleanResponse
import com.colinjp.inblo.datasource.network.message.responses.GetMessageListResponse
import com.colinjp.inblo.datasource.network.message.responses.SingleMessageResponse

interface MessageService {

    suspend fun getAllMessagesByUserId(
        userId: Int
    ): GetMessageListResponse

    suspend fun markMessageAsRead(
        messageId: Int
    ): GetMessageListResponse

    suspend fun sendMessage(
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
    ): SingleMessageResponse

    suspend fun removeMessage(
        messageId: Int
    ): BooleanResponse

}