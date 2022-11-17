package com.colinjp.inblo.android.di

import com.colinjp.inblo.datasource.network.message.MessageService
import com.colinjp.inblo.datasource.network.message.MessageServiceImpl
import com.colinjp.inblo.interactors.message.GetAllMessages
import com.colinjp.inblo.interactors.message.MarkMessageAsRead
import com.colinjp.inblo.interactors.message.RemoveMessage
import com.colinjp.inblo.interactors.message.SendMessageToUser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import io.ktor.client.*
import javax.inject.Scope


@Module
@InstallIn(ActivityRetainedComponent::class)
class MessageModule {

    @Provides
    @ActivityRetainedScoped
    fun provideMessageService(
        httpClient: HttpClient,
        @BaseURL baseURL: String
    ): MessageService {
        return MessageServiceImpl(httpClient,baseURL)
    }

    @Provides
    @ActivityRetainedScoped
    fun provideGetAllMessages(
        messageService: MessageService
    ): GetAllMessages {
        return GetAllMessages(messageService)
    }

    @Provides
    @ActivityRetainedScoped
    fun provideSendMessageToUser(
        messageService: MessageService
    ): SendMessageToUser {
        return SendMessageToUser(messageService)
    }

    @Provides
    @ActivityRetainedScoped
    fun provideMarkAsRead(
        messageService: MessageService
    ): MarkMessageAsRead {
        return MarkMessageAsRead(messageService)
    }

    @Provides
    @ActivityRetainedScoped
    fun provideRemoveMessage(
        messageService: MessageService
    ): RemoveMessage {
        return RemoveMessage(messageService)
    }


}