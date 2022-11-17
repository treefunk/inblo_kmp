package com.colinjp.inblo.di

import com.colinjp.inblo.interactors.message.GetAllMessages
import com.colinjp.inblo.interactors.message.RemoveMessage
import com.colinjp.inblo.interactors.message.SendMessageToUser

class MessagesModule(
    val networkModule: NetworkModule
) {
    val getMessages: GetAllMessages by lazy {
        GetAllMessages(
            messageService = networkModule.messageService
        )
    }

    val sendMessageUser: SendMessageToUser by lazy {
        SendMessageToUser(
            messageService = networkModule.messageService
        )
    }

    val removeMessage: RemoveMessage by lazy {
        RemoveMessage(
            messageService = networkModule.messageService
        )
    }
}